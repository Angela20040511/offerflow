<template>
  <PageFrame title="招聘概览" desc="统一查看集团各子公司的开放岗位、新增投递与候选人进展。">
    <StatsPanels :items="summaryItems" />

    <el-row :gutter="20">
      <el-col :span="12">
        <el-card>
          <template #header>最新投递</template>
          <el-table :data="overview.latestApplications || []" stripe>
            <el-table-column prop="candidateName" label="候选人姓名" min-width="140" />
            <el-table-column prop="jobTitle" label="岗位" min-width="160" />
            <el-table-column prop="subsidiaryName" label="子公司" min-width="180" />
            <el-table-column prop="businessLine" label="业务线" min-width="140" />
            <el-table-column prop="stage" label="阶段" width="110">
              <template #default="scope">
                <StatusTag :status="scope.row.stage" />
              </template>
            </el-table-column>
            <el-table-column prop="applyTime" label="投递时间" min-width="170" />
          </el-table>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card>
          <template #header>各子公司开放岗位</template>
          <el-table :data="overview.subsidiaryJobDistribution || []" stripe>
            <el-table-column prop="name" label="子公司" min-width="180" />
            <el-table-column prop="value" label="岗位数" width="110" />
          </el-table>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card>
          <template #header>各子公司新增投递</template>
          <el-table :data="overview.subsidiaryApplicationDistribution || []" stripe>
            <el-table-column prop="name" label="子公司" min-width="180" />
            <el-table-column prop="value" label="投递数" width="110" />
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </PageFrame>
</template>

<script setup>
import { computed, onMounted } from 'vue'
import { useStore } from 'vuex'
import StatusTag from '@/components/common/StatusTag.vue'
import PageFrame from '@/components/layout/PageFrame.vue'
import StatsPanels from '@/components/hr/StatsPanels.vue'

const store = useStore()
const overview = computed(() => store.state.user.hrOverview || {})
const summaryItems = computed(() => [
  { label: '开放岗位', value: overview.value.openJobCount || 0 },
  { label: '新增投递数', value: overview.value.newApplicationCount || 0 },
  { label: '筛选中', value: overview.value.screeningCount || 0 },
  { label: '面试中', value: overview.value.interviewCount || 0 }
])

onMounted(() => store.dispatch('user/fetchHrOverview'))
</script>
