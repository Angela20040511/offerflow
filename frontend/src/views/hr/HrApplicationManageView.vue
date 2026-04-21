<template>
  <PageFrame title="投递管理" desc="统一查看投递记录、筛选条件与候选人 PDF 简历。">
    <div class="application-grid">
      <section class="panel left-panel">
        <div class="toolbar">
          <el-input v-model="filters.keyword" clearable placeholder="输入姓名、岗位或子公司" @keyup.enter="load" />
          <el-select v-model="filters.stage" clearable placeholder="筛选阶段" @change="load">
            <el-option v-for="item in stageOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
          <el-button type="primary" @click="load">查询</el-button>
        </div>

        <div class="table-wrap">
          <el-table :data="list" stripe highlight-current-row @current-change="handleCurrentChange">
            <el-table-column prop="candidateName" label="候选人姓名" min-width="140" />
            <el-table-column prop="jobTitle" label="岗位" min-width="160" />
            <el-table-column prop="subsidiaryName" label="子公司" min-width="180" />
            <el-table-column prop="businessLine" label="业务线" min-width="140" />
            <el-table-column prop="educationLevel" label="学历" min-width="110" />
            <el-table-column prop="systemMatchScore" label="系统匹配分" width="120" />
            <el-table-column prop="hrReviewScore" label="HR 评估分" width="110" />
            <el-table-column prop="stage" label="阶段" width="110">
              <template #default="scope">
                <StatusTag :status="scope.row.stage" />
              </template>
            </el-table-column>
            <el-table-column prop="applyTime" label="投递时间" min-width="170" />
          </el-table>
        </div>
      </section>

      <aside class="panel right-panel">
        <div class="panel-head">
          <div>
            <h3>{{ currentRow?.candidateName || 'PDF 简历预览' }}</h3>
            <p v-if="currentRow">{{ currentRow.jobTitle }} / {{ currentRow.subsidiaryName }}</p>
            <p v-else>点击左侧投递记录后，这里会同步显示候选人的 PDF 简历。</p>
          </div>
        </div>
        <CanvasPdf :url="previewPdfUrl" empty-text="该候选人暂未上传 PDF 简历。" />
      </aside>
    </div>
  </PageFrame>
</template>

<script setup>
import { computed, onMounted, reactive, ref, watch } from 'vue'
import { useStore } from 'vuex'
import { applicationStageOptions } from '@/constants/status'
import StatusTag from '@/components/common/StatusTag.vue'
import CanvasPdf from '@/components/pdf/CanvasPdf.vue'
import PageFrame from '@/components/layout/PageFrame.vue'

const store = useStore()
const filters = reactive({ keyword: '', stage: '' })
const stageOptions = applicationStageOptions
const list = computed(() => store.state.user.hrApplications || [])
const currentRow = ref(null)
const previewPdfUrl = ref('')

const withCacheBust = (url) => (url ? `${url}${url.includes('?') ? '&' : '?'}t=${Date.now()}` : '')

const syncCurrentRow = () => {
  if (!list.value.length) {
    currentRow.value = null
    previewPdfUrl.value = ''
    return
  }
  const matched = currentRow.value ? list.value.find((item) => item.id === currentRow.value.id) : null
  currentRow.value = matched || list.value[0]
  previewPdfUrl.value = withCacheBust(currentRow.value?.resumePdfUrl || '')
}

const load = async () => {
  await store.dispatch('user/fetchHrApplications', { ...filters })
  syncCurrentRow()
}

const handleCurrentChange = (row) => {
  currentRow.value = row || null
  previewPdfUrl.value = withCacheBust(row?.resumePdfUrl || '')
}

watch(list, syncCurrentRow)

onMounted(load)
</script>

<style scoped>
.application-grid {
  display: grid;
  grid-template-columns: minmax(0, 1.15fr) minmax(360px, 0.85fr);
  gap: 20px;
}

.panel {
  padding: 24px;
  border-radius: 28px;
  background: var(--panel);
  border: 1px solid var(--line);
  box-shadow: var(--shadow);
}

.left-panel,
.right-panel {
  display: grid;
  gap: 16px;
  min-width: 0;
}

.toolbar {
  display: flex;
  gap: 12px;
}

.table-wrap {
  width: 100%;
  min-width: 0;
  overflow-x: auto;
}

.panel-head p {
  color: var(--muted);
  margin-top: 8px;
}

@media (max-width: 1180px) {
  .application-grid {
    grid-template-columns: 1fr;
  }

  .toolbar {
    flex-wrap: wrap;
  }
}
</style>
