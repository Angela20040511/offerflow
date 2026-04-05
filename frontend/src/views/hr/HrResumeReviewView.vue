<template>
  <PageFrame title="简历评审" desc="统一查看候选人简历、系统匹配结果和 HR 评审意见。">
    <div class="review-grid">
      <div class="left-column">
        <ResumeReviewPanel :detail="detail" />

        <el-card v-if="detail">
          <template #header>匹配结果</template>
          <el-descriptions :column="1" border>
            <el-descriptions-item v-for="item in scoreItems" :key="item.label" :label="item.label">
              {{ item.value }}
            </el-descriptions-item>
            <el-descriptions-item label="命中技能">
              <span v-if="matchedSkills.length">{{ matchedSkills.join(' / ') }}</span>
              <span v-else>--</span>
            </el-descriptions-item>
            <el-descriptions-item label="待补足技能">
              <span v-if="missingSkills.length">{{ missingSkills.join(' / ') }}</span>
              <span v-else>--</span>
            </el-descriptions-item>
          </el-descriptions>
        </el-card>

        <StageActionBar
          v-if="detail"
          :stage="detail.stage"
          :hr-note="detail.hrNote"
          :hr-review-score="detail.hrReviewScore"
          :hr-review-status="detail.hrReviewStatus"
          @update-stage="updateStage"
          @save-note="saveNote"
          @save-review="saveReview"
        />
      </div>

      <div class="right-column">
        <CanvasPdf :url="pdfUrl" />
      </div>
    </div>
  </PageFrame>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { useRoute } from 'vue-router'
import { getResumePdfApi } from '@/api/resume'
import {
  getCandidateDetailApi,
  getMatchScoreApi,
  updateCandidateNoteApi,
  updateCandidateReviewScoreApi,
  updateCandidateStageApi
} from '@/api/candidate'
import CanvasPdf from '@/components/pdf/CanvasPdf.vue'
import ResumeReviewPanel from '@/components/hr/ResumeReviewPanel.vue'
import StageActionBar from '@/components/hr/StageActionBar.vue'
import PageFrame from '@/components/layout/PageFrame.vue'

const route = useRoute()
const detail = ref(null)
const matchScore = ref({})
const pdfUrl = ref('')

const load = async () => {
  const loadedDetail = await getCandidateDetailApi(route.params.id)
  detail.value = loadedDetail
  matchScore.value = await getMatchScoreApi(route.params.id)
  if (loadedDetail?.resumeId) {
    const pdf = await getResumePdfApi(loadedDetail.resumeId)
    pdfUrl.value = pdf?.pdfUrl || ''
  } else {
    pdfUrl.value = ''
  }
}

const updateStage = async (stage) => {
  await updateCandidateStageApi({ applicationId: detail.value.applicationId, stage })
  ElMessage.success('候选人阶段已更新')
  await load()
}

const saveNote = async (hrNote) => {
  await updateCandidateNoteApi({ applicationId: detail.value.applicationId, hrNote })
  ElMessage.success('HR 备注已保存')
  await load()
}

const saveReview = async (payload) => {
  await updateCandidateReviewScoreApi({
    applicationId: detail.value.applicationId,
    hrReviewScore: payload.hrReviewScore,
    hrReviewStatus: payload.hrReviewStatus,
    hrNote: payload.hrNote
  })
  ElMessage.success('HR 评估已保存')
  await load()
}

const scoreItems = computed(() => [
  { label: '系统总匹配分', value: matchScore.value.totalScore ?? detail.value?.systemMatchScore ?? '--' },
  { label: '技能匹配分', value: matchScore.value.skillScore ?? '--' },
  { label: '专业匹配分', value: matchScore.value.majorScore ?? '--' },
  { label: '项目匹配分', value: matchScore.value.projectScore ?? '--' },
  { label: '经历匹配分', value: matchScore.value.experienceScore ?? '--' },
  { label: 'HR 评估分', value: detail.value?.hrReviewScore ?? '--' },
  { label: 'HR 评估状态', value: detail.value?.hrReviewStatus || '--' }
])

const matchedSkills = computed(() => matchScore.value.matchedSkills || [])
const missingSkills = computed(() => matchScore.value.missingSkills || [])

onMounted(load)
</script>

<style scoped>
.review-grid {
  display: grid;
  grid-template-columns: minmax(0, 1fr) minmax(0, 1fr);
  gap: 20px;
}

.left-column,
.right-column {
  display: grid;
  gap: 16px;
  min-width: 0;
}

@media (max-width: 1080px) {
  .review-grid {
    grid-template-columns: 1fr;
  }
}
</style>
