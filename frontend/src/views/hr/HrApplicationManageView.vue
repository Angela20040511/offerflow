<template>
  <PageFrame title="投递管理" desc="统一查看各子公司投递记录、简历版本和评估情况。">
    <div class="toolbar">
      <el-input v-model="filters.keyword" clearable placeholder="输入姓名、岗位或子公司" @keyup.enter="load" />
      <el-select v-model="filters.stage" clearable placeholder="筛选阶段" @change="load">
        <el-option v-for="item in stageOptions" :key="item.value" :label="item.label" :value="item.value" />
      </el-select>
      <el-button type="primary" @click="load">查询</el-button>
    </div>

    <div class="table-wrap">
      <el-table :data="list" stripe>
        <el-table-column prop="candidateName" label="候选人姓名" min-width="140" />
        <el-table-column prop="jobTitle" label="岗位" min-width="160" />
        <el-table-column prop="subsidiaryName" label="子公司" min-width="180" />
        <el-table-column prop="businessLine" label="业务线" min-width="140" />
        <el-table-column prop="resumeName" label="简历版本" min-width="160" />
        <el-table-column prop="systemMatchScore" label="系统匹配分" width="110" />
        <el-table-column prop="hrReviewScore" label="HR 评估分" width="110" />
        <el-table-column prop="stage" label="阶段" width="110">
          <template #default="scope">
            <StatusTag :status="scope.row.stage" />
          </template>
        </el-table-column>
        <el-table-column prop="applyTime" label="投递时间" min-width="170" />
      </el-table>
    </div>
  </PageFrame>
</template>

<script setup>
import { computed, onMounted, reactive } from 'vue'
import { useStore } from 'vuex'
import StatusTag from '@/components/common/StatusTag.vue'
import PageFrame from '@/components/layout/PageFrame.vue'
import { applicationStageOptions } from '@/constants/status'

const store = useStore()
const filters = reactive({ keyword: '', stage: '' })
const list = computed(() => store.state.user.hrApplications || [])
const stageOptions = applicationStageOptions

const load = () => store.dispatch('user/fetchHrApplications', { ...filters })

onMounted(load)
</script>

<style scoped>
.toolbar {
  display: flex;
  gap: 12px;
  margin-bottom: 16px;
}

.table-wrap {
  width: 100%;
  min-width: 0;
  overflow-x: auto;
}
</style>
