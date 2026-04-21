<template>
  <section v-if="job" class="detail-panel">
    <div class="hero">
      <div>
        <h2>{{ job.title }}</h2>
        <p>{{ groupName }} / {{ job.subsidiaryName || '--' }} / {{ job.businessLine || '--' }}</p>
      </div>
      <div class="hero-side">
        <StatusTag mode="job" :status="job.status" />
        <StatusTag v-if="job.isApplied" :status="'APPLIED'" />
        <StatusTag v-if="job.isFavorite" :status="'FAVORITE'" label="已收藏" />
      </div>
    </div>

    <div class="meta-grid">
      <article>
        <span>岗位类别</span>
        <strong>{{ job.categoryName || '未设置' }}</strong>
      </article>
      <article>
        <span>工作地点</span>
        <strong>{{ locationLabel }}</strong>
      </article>
      <article>
        <span>工作方式</span>
        <strong>{{ workModeLabel }}</strong>
      </article>
      <article>
        <span>工作时长</span>
        <strong>{{ durationLabel }}</strong>
      </article>
    </div>

    <div class="section">
      <h3>岗位说明</h3>
      <p>{{ job.description || '暂无岗位说明' }}</p>
    </div>

    <div class="section">
      <h3>任职要求</h3>
      <p>{{ job.requirement || '暂无任职要求' }}</p>
    </div>

    <div class="section">
      <h3>岗位标签</h3>
      <div v-if="tagSummary.visible.length" class="skill-list">
        <el-tag v-for="item in tagSummary.visible" :key="item" effect="plain">{{ item }}</el-tag>
        <el-tag v-if="tagSummary.extra" effect="plain">+{{ tagSummary.extra }}</el-tag>
      </div>
      <el-empty v-else description="暂无岗位标签" :image-size="72" />
    </div>

    <div class="section action-panel">
      <h3>{{ job.isApplied ? '当前岗位状态' : '选择投递简历版本' }}</h3>
      <el-select v-model="selectedResumeId" placeholder="请选择简历版本" :disabled="!hasResumes || !canChooseResume">
        <el-option v-for="item in resumes" :key="item.id" :label="item.resumeName" :value="item.id" />
      </el-select>
      <div class="resume-tip">
        {{ actionHint }}
      </div>
      <div class="action-row">
        <el-button type="primary" :disabled="!canApply" @click="$emit('apply', { job, resumeId: selectedResumeId })">
          {{ applyLabel }}
        </el-button>
        <el-button :type="job.isFavorite ? 'warning' : 'default'" @click="$emit('favorite', job)">
          {{ job.isFavorite ? '取消收藏' : '收藏岗位' }}
        </el-button>
      </div>
    </div>
  </section>
</template>

<script setup>
import { computed, ref, watch } from 'vue'
import StatusTag from '@/components/common/StatusTag.vue'
import { formatShortLocation, getTagSummary } from '@/utils/job'

const props = defineProps({
  job: { type: Object, default: null },
  resumes: { type: Array, default: () => [] },
  groupName: { type: String, default: '用友集团' }
})

defineEmits(['apply', 'favorite'])

const selectedResumeId = ref('')

watch(
  () => props.resumes,
  (value) => {
    const defaultResume = value.find((item) => item.isDefault) || value[0]
    selectedResumeId.value = defaultResume?.id || ''
  },
  { immediate: true, deep: true }
)

const workModeMap = {
  ONSITE: '现场办公',
  REMOTE: '远程协同',
  HYBRID: '混合办公'
}

const durationMap = {
  SHORT_TERM: '短期实习',
  LONG_TERM: '长期实习',
  FULL_TIME: '校招全职'
}

const hasResumes = computed(() => props.resumes.length > 0)
const isClosed = computed(() => props.job?.status === 'CLOSED')
const canChooseResume = computed(() => !props.job?.isApplied && !isClosed.value)
const canApply = computed(() => hasResumes.value && selectedResumeId.value && !props.job?.isApplied && !isClosed.value)
const locationLabel = computed(() => formatShortLocation(props.job?.provinceName, props.job?.cityName))
const workModeLabel = computed(() => workModeMap[props.job?.workMode] || props.job?.workMode || '待沟通')
const durationLabel = computed(() => durationMap[props.job?.durationType] || props.job?.durationType || '时长待定')
const tagSummary = computed(() => getTagSummary(props.job?.tags?.length ? props.job.tags : props.job?.requiredSkills?.length ? props.job.requiredSkills : props.job?.requiredSkillsJson))
const applyLabel = computed(() => {
  if (props.job?.isApplied) return '已投递'
  if (isClosed.value) return '岗位已关闭'
  return '立即投递'
})
const actionHint = computed(() => {
  if (isClosed.value) return '该岗位已关闭，可继续查看详情和收藏，但不能投递。'
  if (props.job?.isApplied) return '当前岗位已投递，如已撤回会重新显示投递入口。'
  if (!hasResumes.value) return '请先在简历中心创建简历版本。'
  return '投递时将使用当前选中的简历版本。'
})
</script>

<style scoped>
.detail-panel {
  display: grid;
  gap: 24px;
}

.hero,
.section,
.meta-grid article {
  padding: 24px;
  border-radius: 24px;
  background: rgba(255, 255, 255, 0.92);
  border: 1px solid rgba(106, 122, 255, 0.14);
  box-shadow: 0 14px 28px rgba(17, 33, 84, 0.08);
}

.hero {
  display: flex;
  justify-content: space-between;
  gap: 20px;
}

.hero h2,
.section h3 {
  margin: 0;
}

.hero p {
  margin: 12px 0 0;
  color: var(--muted);
}

.hero-side,
.skill-list,
.action-row {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.meta-grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 16px;
}

.meta-grid span {
  color: var(--muted);
}

.meta-grid strong {
  display: block;
  margin-top: 12px;
}

.resume-tip {
  margin: 12px 0;
  color: var(--muted);
}

@media (max-width: 960px) {
  .meta-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}
</style>
