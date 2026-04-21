<template>
  <span class="status-tag" :class="toneClass">
    {{ displayLabel }}
  </span>
</template>

<script setup>
import { computed } from 'vue'
import { applicationStageLabelMap, jobStatusLabelMap } from '@/constants/status'

const props = defineProps({
  status: { type: String, default: '' },
  mode: { type: String, default: 'application' },
  label: { type: String, default: '' }
})

const reviewLabelMap = {
  PENDING: '待评审',
  PLANNED: '待安排',
  APPROVED: '通过',
  REJECTED: '淘汰'
}

const labelMap = {
  application: applicationStageLabelMap,
  job: jobStatusLabelMap,
  review: reviewLabelMap
}

const toneMap = {
  APPLIED: 'applied',
  SCREENING: 'screening',
  INTERVIEW: 'interview',
  OFFER: 'offer',
  REJECTED: 'rejected',
  WITHDRAWN: 'withdrawn',
  OPEN: 'open',
  DRAFT: 'draft',
  CLOSED: 'closed',
  FAVORITE: 'favorite',
  PENDING: 'screening',
  PLANNED: 'interview',
  APPROVED: 'offer'
}

const displayLabel = computed(() => props.label || labelMap[props.mode]?.[props.status] || props.status || '未设置')
const toneClass = computed(() => `tone-${toneMap[props.status] || 'default'}`)
</script>

<style scoped>
.status-tag {
  min-width: 78px;
  padding: 6px 12px;
  border-radius: 999px;
  font-size: 12px;
  font-weight: 700;
  line-height: 1;
  text-align: center;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  border: 1px solid transparent;
}

.tone-default {
  background: rgba(124, 137, 176, 0.12);
  color: #52617f;
  border-color: rgba(124, 137, 176, 0.2);
}

.tone-open,
.tone-offer {
  background: rgba(28, 196, 124, 0.12);
  color: #0d8f58;
  border-color: rgba(28, 196, 124, 0.22);
}

.tone-applied,
.tone-interview {
  background: rgba(72, 116, 255, 0.12);
  color: #3454dd;
  border-color: rgba(72, 116, 255, 0.2);
}

.tone-screening,
.tone-draft,
.tone-favorite {
  background: rgba(255, 176, 32, 0.14);
  color: #b56c08;
  border-color: rgba(255, 176, 32, 0.24);
}

.tone-rejected,
.tone-withdrawn,
.tone-closed {
  background: rgba(255, 94, 94, 0.12);
  color: #c44c4c;
  border-color: rgba(255, 94, 94, 0.22);
}
</style>
