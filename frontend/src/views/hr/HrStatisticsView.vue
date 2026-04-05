<template>
  <PageFrame title="统计分析" desc="查看集团招聘阶段分布、子公司投递占比和岗位分布情况。">
    <StatsPanels :items="stats" />

    <div class="stats-grid">
      <el-card>
        <template #header>招聘阶段占比</template>
        <div v-if="stagePie.length" class="pie-wrap">
          <div class="pie-chart" :style="{ background: stagePieGradient }"></div>
          <div class="pie-legend">
            <div v-for="(item, index) in stagePie" :key="`${item.stage}-${index}`" class="legend-row">
              <span class="legend-dot" :style="{ backgroundColor: pieColors[index % pieColors.length] }"></span>
              <span>{{ stageLabel(item.stage) }}</span>
              <strong>{{ item.value }}</strong>
            </div>
          </div>
        </div>
        <el-empty v-else description="暂无阶段统计数据" :image-size="80" />
      </el-card>

      <el-card>
        <template #header>子公司投递占比</template>
        <div v-if="subsidiaryPie.length" class="pie-wrap">
          <div class="pie-chart" :style="{ background: subsidiaryPieGradient }"></div>
          <div class="pie-legend">
            <div v-for="(item, index) in subsidiaryPie" :key="`${item.name}-${index}`" class="legend-row">
              <span class="legend-dot" :style="{ backgroundColor: pieColors[index % pieColors.length] }"></span>
              <span>{{ item.name }}</span>
              <strong>{{ item.value }}</strong>
            </div>
          </div>
        </div>
        <el-empty v-else description="暂无子公司分布数据" :image-size="80" />
      </el-card>
    </div>

    <el-card>
      <template #header>招聘漏斗</template>
      <el-table :data="funnel" border>
        <el-table-column label="阶段" min-width="120">
          <template #default="scope">
            <StatusTag :status="scope.row.stage" />
          </template>
        </el-table-column>
        <el-table-column prop="value" label="数量" width="120" />
      </el-table>
    </el-card>

    <div class="stats-grid">
      <el-card>
        <template #header>各子公司岗位数</template>
        <el-table :data="jobDistribution" border>
          <el-table-column prop="name" label="子公司" min-width="180" />
          <el-table-column prop="value" label="岗位数" width="120" />
        </el-table>
      </el-card>

      <el-card>
        <template #header>各子公司投递数</template>
        <el-table :data="subsidiaryPie" border>
          <el-table-column prop="name" label="子公司" min-width="180" />
          <el-table-column prop="value" label="投递数" width="120" />
        </el-table>
      </el-card>
    </div>
  </PageFrame>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { APPLICATION_STAGE_LABELS } from '@/constants/status'
import StatusTag from '@/components/common/StatusTag.vue'
import StatsPanels from '@/components/hr/StatsPanels.vue'
import PageFrame from '@/components/layout/PageFrame.vue'
import {
  getHrDistributionApi,
  getHrFunnelApi,
  getHrStagePieApi,
  getHrStatsOverviewApi,
  getHrSubsidiaryDistributionApi
} from '@/api/stats'

const pieColors = ['#2563eb', '#0ea5e9', '#14b8a6', '#f59e0b', '#ef4444', '#8b5cf6']
const overview = reactive({})
const funnel = ref([])
const jobDistribution = ref([])
const stagePie = ref([])
const subsidiaryPie = ref([])

const normalizeStageRows = (rows) => (rows || []).map((item) => ({ stage: item.stage, value: item.value ?? item.count ?? 0 }))
const normalizeValueRows = (rows) => (rows || []).map((item) => ({ name: item.name || item.subsidiaryName || item.jobTitle || '--', value: item.value ?? item.count ?? 0 }))

const load = async () => {
  Object.assign(overview, await getHrStatsOverviewApi())
  funnel.value = normalizeStageRows(await getHrFunnelApi())
  jobDistribution.value = normalizeValueRows(await getHrDistributionApi())
  stagePie.value = normalizeStageRows(await getHrStagePieApi())
  subsidiaryPie.value = normalizeValueRows(await getHrSubsidiaryDistributionApi())
}

const stageLabel = (stage) => APPLICATION_STAGE_LABELS[stage] || stage || '--'

const buildPieGradient = (rows) => {
  const total = rows.reduce((sum, item) => sum + (item.value || 0), 0)
  if (!total) {
    return 'conic-gradient(#e5e7eb 0deg 360deg)'
  }
  let current = 0
  const parts = rows.map((item, index) => {
    const ratio = (item.value || 0) / total
    const start = current
    const end = current + ratio * 360
    current = end
    return `${pieColors[index % pieColors.length]} ${start}deg ${end}deg`
  })
  return `conic-gradient(${parts.join(', ')})`
}

const stagePieGradient = computed(() => buildPieGradient(stagePie.value))
const subsidiaryPieGradient = computed(() => buildPieGradient(subsidiaryPie.value))
const stats = computed(() => [
  { label: '开放岗位', value: overview.openJobCount || 0 },
  { label: '投递总数', value: overview.applicationCount || 0 },
  { label: '筛选中', value: overview.screeningCount || 0 },
  { label: '面试中', value: overview.interviewCount || 0 },
  { label: '已发录用', value: overview.offerCount || 0 },
  { label: '已淘汰', value: overview.rejectedCount || 0 }
])

onMounted(load)
</script>

<style scoped>
.stats-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 16px;
  margin-bottom: 16px;
}

.pie-wrap {
  display: grid;
  grid-template-columns: 220px 1fr;
  gap: 18px;
  align-items: center;
}

.pie-chart {
  width: 220px;
  height: 220px;
  border-radius: 50%;
  margin: 0 auto;
}

.pie-legend {
  display: grid;
  gap: 10px;
}

.legend-row {
  display: grid;
  grid-template-columns: 16px 1fr auto;
  gap: 10px;
  align-items: center;
}

.legend-dot {
  width: 12px;
  height: 12px;
  border-radius: 50%;
}

@media (max-width: 1080px) {
  .stats-grid {
    grid-template-columns: 1fr;
  }

  .pie-wrap {
    grid-template-columns: 1fr;
  }
}
</style>
