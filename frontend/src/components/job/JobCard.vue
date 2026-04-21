<template>
  <article class="job-card" :class="{ compact, selected }">
    <div class="card-head">
      <div class="title-block">
        <h3>{{ job.title }}</h3>
        <p>{{ displayGroupName }} / {{ job.subsidiaryName || '--' }} / {{ job.businessLine || '--' }}</p>
      </div>
      <div class="head-actions">
        <StatusTag mode="job" :status="job.status" />
        <StatusTag v-if="job.isApplied" :status="'APPLIED'" />
        <StatusTag v-if="job.isFavorite" :status="'FAVORITE'" label="已收藏" />
      </div>
    </div>

    <div class="meta-list">
      <span>{{ job.categoryName || '岗位类别待补充' }}</span>
      <span>{{ workModeLabel }}</span>
      <span>{{ durationLabel }}</span>
      <span>{{ locationLabel }}</span>
    </div>

    <div class="salary">{{ salaryLabel }}</div>

    <div v-if="tagSummary.visible.length" class="skill-list">
      <el-tag v-for="tag in tagSummary.visible" :key="tag" effect="plain">{{ tag }}</el-tag>
      <el-tag v-if="tagSummary.extra" effect="plain">+{{ tagSummary.extra }}</el-tag>
    </div>

    <div class="action-row">
      <el-button type="primary" plain @click.stop="$emit('detail', job)">查看详情</el-button>
      <el-button v-if="showFavorite" :type="job.isFavorite ? 'warning' : 'default'" @click.stop="$emit('favorite', job)">
        {{ job.isFavorite ? '取消收藏' : '收藏岗位' }}
      </el-button>
    </div>
  </article>
</template>

<script setup>
import { computed } from 'vue'
import StatusTag from '@/components/common/StatusTag.vue'
import { formatShortLocation, getTagSummary } from '@/utils/job'

const props = defineProps({
  job: { type: Object, required: true },
  compact: { type: Boolean, default: false },
  selected: { type: Boolean, default: false },
  showFavorite: { type: Boolean, default: true },
  groupLabel: { type: String, default: '用友集团' }
})

defineEmits(['detail', 'favorite'])

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

const displayGroupName = computed(() => props.job.groupName || props.groupLabel)
const tagSummary = computed(() => getTagSummary(props.job.tags?.length ? props.job.tags : props.job.requiredSkills?.length ? props.job.requiredSkills : props.job.requiredSkillsJson))
const locationLabel = computed(() => formatShortLocation(props.job.provinceName, props.job.cityName))
const workModeLabel = computed(() => workModeMap[props.job.workMode] || props.job.workMode || '待沟通')
const durationLabel = computed(() => durationMap[props.job.durationType] || props.job.durationType || '时长待定')
const salaryLabel = computed(() => `${props.job.salaryMin ?? '--'}-${props.job.salaryMax ?? '--'} 元/天`)
</script>

<style scoped>
.job-card {
  padding: 24px;
  border-radius: 28px;
  background: rgba(255, 255, 255, 0.92);
  border: 1px solid rgba(106, 122, 255, 0.14);
  box-shadow: 0 14px 28px rgba(17, 33, 84, 0.08);
  display: grid;
  gap: 18px;
}

.job-card.compact {
  min-width: 320px;
}

.job-card.selected {
  border-color: rgba(76, 99, 255, 0.42);
  box-shadow: 0 16px 30px rgba(76, 99, 255, 0.14);
}

.card-head,
.action-row {
  display: flex;
  justify-content: space-between;
  gap: 16px;
  align-items: flex-start;
}

.title-block h3 {
  margin: 0;
  font-size: 1.35rem;
  color: var(--text);
}

.title-block p {
  margin: 10px 0 0;
  color: var(--muted);
}

.head-actions,
.meta-list,
.skill-list {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
}

.meta-list {
  color: var(--muted);
}

.salary {
  font-size: 1.6rem;
  color: var(--primary);
  font-weight: 700;
}

@media (max-width: 640px) {
  .card-head,
  .action-row {
    flex-direction: column;
  }
}
</style>
