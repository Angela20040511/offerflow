<template>
  <div class="stage-action-bar">
    <div class="action-row">
      <el-select v-model="localStage" clearable placeholder="选择阶段">
        <el-option v-for="item in stageOptions" :key="item.value" :label="item.label" :value="item.value" />
      </el-select>
      <el-button type="primary" :disabled="!localStage" @click="$emit('update-stage', localStage)">更新阶段</el-button>
    </div>

    <div class="action-row">
      <el-input-number v-model="reviewScore" :min="0" :max="100" controls-position="right" placeholder="HR 评估分" />
      <el-select v-model="reviewStatus" clearable placeholder="HR 评估状态">
        <el-option label="建议推进" value="建议推进" />
        <el-option label="建议约面" value="建议约面" />
        <el-option label="待观察" value="待观察" />
        <el-option label="不推荐" value="不推荐" />
      </el-select>
      <el-button @click="saveReview">保存评估</el-button>
    </div>

    <el-input v-model="note" type="textarea" :rows="4" placeholder="请输入 HR 对候选人的评估意见和跟进建议" />

    <div class="action-row">
      <el-button @click="$emit('save-note', note)">保存备注</el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import { applicationStageOptions } from '@/constants/status'

const props = defineProps({
  stage: { type: String, default: '' },
  hrReviewScore: { type: Number, default: null },
  hrReviewStatus: { type: String, default: '' },
  hrNote: { type: String, default: '' }
})

const emit = defineEmits(['update-stage', 'save-review', 'save-note'])

const localStage = ref('')
const reviewScore = ref(null)
const reviewStatus = ref('')
const note = ref('')
const stageOptions = applicationStageOptions

watch(() => props.stage, (value) => { localStage.value = value || '' }, { immediate: true })
watch(() => props.hrReviewScore, (value) => { reviewScore.value = value ?? null }, { immediate: true })
watch(() => props.hrReviewStatus, (value) => { reviewStatus.value = value || '' }, { immediate: true })
watch(() => props.hrNote, (value) => { note.value = value || '' }, { immediate: true })

const saveReview = () => {
  emit('save-review', {
    hrReviewScore: reviewScore.value ?? null,
    hrReviewStatus: reviewStatus.value || '',
    hrNote: note.value || ''
  })
}
</script>

<style scoped>
.stage-action-bar {
  display: grid;
  gap: 16px;
}

.action-row {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}
</style>
