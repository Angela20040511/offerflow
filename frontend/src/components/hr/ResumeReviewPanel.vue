<template>
  <div v-if="detail" class="review-panel">
    <div class="hero">
      <div>
        <h3>{{ detail.candidateName || '未知候选人' }}</h3>
        <p>{{ detail.groupName || '用友集团' }} / {{ detail.subsidiaryName || '--' }} / {{ detail.businessLine || '--' }}</p>
      </div>
      <StatusTag :status="detail.stage" />
    </div>

    <el-descriptions :column="1" border>
      <el-descriptions-item label="岗位">{{ detail.jobTitle || '--' }}</el-descriptions-item>
      <el-descriptions-item label="岗位类别">{{ detail.categoryName || '--' }}</el-descriptions-item>
      <el-descriptions-item label="学校 / 专业">{{ [detail.school, detail.major].filter(Boolean).join(' / ') || '--' }}</el-descriptions-item>
      <el-descriptions-item label="联系方式">{{ [detail.phone, detail.email].filter(Boolean).join(' / ') || '--' }}</el-descriptions-item>
      <el-descriptions-item label="系统匹配分">{{ detail.systemMatchScore ?? '--' }}</el-descriptions-item>
      <el-descriptions-item label="HR 评估分">{{ detail.hrReviewScore ?? '--' }}</el-descriptions-item>
      <el-descriptions-item label="HR 评估状态">{{ detail.hrReviewStatus || '--' }}</el-descriptions-item>
      <el-descriptions-item label="使用简历版本">{{ detail.resumeName || '--' }}</el-descriptions-item>
    </el-descriptions>
  </div>
  <el-empty v-else description="暂无候选人信息" :image-size="88" />
</template>

<script setup>
import StatusTag from '@/components/common/StatusTag.vue'

defineProps({
  detail: {
    type: Object,
    default: null
  }
})
</script>

<style scoped>
.review-panel {
  display: grid;
  gap: 16px;
}

.hero {
  display: flex;
  justify-content: space-between;
  gap: 16px;
  align-items: flex-start;
}

.hero h3 {
  margin: 0 0 8px;
}

.hero p {
  margin: 0;
  color: #866846;
}
</style>
