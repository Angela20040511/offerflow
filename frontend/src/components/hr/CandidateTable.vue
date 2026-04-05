<template>
  <el-table :data="data" stripe>
    <el-table-column prop="candidateName" label="候选人姓名" min-width="140" />
    <el-table-column prop="jobTitle" label="岗位" min-width="160" />
    <el-table-column prop="subsidiaryName" label="子公司" min-width="180" />
    <el-table-column prop="businessLine" label="业务线" min-width="140" />
    <el-table-column prop="school" label="学校" min-width="140" />
    <el-table-column prop="major" label="专业" min-width="140" />
    <el-table-column prop="systemMatchScore" label="系统匹配分" width="110" />
    <el-table-column prop="hrReviewScore" label="HR 评估分" width="110" />
    <el-table-column prop="stage" label="阶段" width="110">
      <template #default="scope">
        <StatusTag :status="scope.row.stage" />
      </template>
    </el-table-column>
    <el-table-column label="操作" width="120" fixed="right">
      <template #default="scope">
        <el-button text @click="$emit('review', scope.row)">进入评审</el-button>
      </template>
    </el-table-column>
  </el-table>
</template>

<script setup>
import StatusTag from '@/components/common/StatusTag.vue'

defineProps({
  data: { type: Array, default: () => [] }
})

defineEmits(['review'])
</script>
