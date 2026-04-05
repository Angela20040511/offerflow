<template>
  <section v-if="job" class="detail-panel">
    <div class="hero">
      <div>
        <h2>{{ job.title }}</h2>
        <p>{{ groupName }} / {{ job.subsidiaryName || '--' }} / {{ job.businessLine || '--' }}</p>
      </div>
      <div class="hero-side">
        <StatusTag mode="job" :status="job.status" />
        <el-tag v-if="job.isApplied" type="success" effect="light">已投递</el-tag>
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
      <h3>推荐技能</h3>
      <div v-if="skillList.length" class="skill-list">
        <el-tag v-for="item in skillList" :key="item" effect="plain">{{ item }}</el-tag>
      </div>
      <el-empty v-else description="暂无技能要求" :image-size="72" />
    </div>

    <div class="section action-panel">
      <h3>选择投递简历版本</h3>
      <el-select v-model="selectedResumeId" placeholder="请选择简历版本" :disabled="!hasResumes">
        <el-option v-for="item in resumes" :key="item.id" :label="item.resumeName" :value="item.id" />
      </el-select>
      <div class="resume-tip">
        {{ hasResumes ? '投递时将使用当前选中的简历版本。' : '请先在简历中心创建简历版本' }}
      </div>
      <div class="action-row">
        <el-button type="primary" :disabled="!canApply" @click="$emit('apply', { job, resumeId: selectedResumeId })">
          {{ job.isApplied ? '已投递' : '立即投递' }}
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

const parseSkills = (value) => {
  if (Array.isArray(value)) return value
  if (typeof value === 'string' && value.trim()) {
    try {
      const parsed = JSON.parse(value)
      return Array.isArray(parsed) ? parsed : []
    } catch {
      return []
    }
  }
  return []
}

const hasResumes = computed(() => props.resumes.length > 0)
const canApply = computed(() => hasResumes.value && selectedResumeId.value && !props.job?.isApplied)
const locationLabel = computed(() => [props.job?.provinceName, props.job?.cityName].filter(Boolean).join(' / ') || '地点待定')
const workModeLabel = computed(() => workModeMap[props.job?.workMode] || props.job?.workMode || '待沟通')
const durationLabel = computed(() => durationMap[props.job?.durationType] || props.job?.durationType || '时长待定')
const skillList = computed(() => parseSkills(props.job?.requiredSkills?.length ? props.job?.requiredSkills : props.job?.requiredSkillsJson))
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
  background: rgba(255, 252, 247, 0.95);
  box-shadow: 0 14px 28px rgba(151, 110, 54, 0.12);
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
  color: #866846;
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
  color: #8b6d4a;
}

.meta-grid strong {
  display: block;
  margin-top: 12px;
}

.resume-tip {
  margin: 12px 0;
  color: #8b6d4a;
}

@media (max-width: 960px) {
  .meta-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}
</style>
