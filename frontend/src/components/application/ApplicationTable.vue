<template>
  <el-table :data="data" stripe>
    <el-table-column prop="jobTitle" label="岗位" min-width="180" />
    <el-table-column prop="subsidiaryName" label="子公司" min-width="180" />
    <el-table-column prop="businessLine" label="业务线" min-width="140" />
    <el-table-column prop="resumeName" label="使用简历" min-width="160" />
    <el-table-column prop="systemMatchScore" label="系统匹配分" width="120" />
    <el-table-column prop="hrReviewScore" label="HR评估分" width="120" />
    <el-table-column prop="stage" label="当前阶段" width="120">
      <template #default="scope">
        <StatusTag :status="scope.row.stage" />
      </template>
    </el-table-column>
    <el-table-column prop="updateTime" label="阶段更新时间" min-width="180" />
    <el-table-column label="操作" width="120" fixed="right">
      <template #default="scope">
        <el-button v-if="canWithdraw(scope.row.stage)" link type="danger" @click="$emit('withdraw', scope.row)">
          撤回投递
        </el-button>
        <el-button v-else link disabled>不可撤回</el-button>
      </template>
    </el-table-column>
  </el-table>
</template>

<script setup>
import StatusTag from '@/components/common/StatusTag.vue'

defineProps({
  data: { type: Array, default: () => [] }
})

defineEmits(['withdraw'])

const terminalStages = new Set(['REJECTED', 'WITHDRAWN', 'OFFER'])
const canWithdraw = (stage) => !terminalStages.has(stage)
</script>
