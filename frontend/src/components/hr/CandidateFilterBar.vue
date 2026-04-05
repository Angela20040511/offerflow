<template>
  <div class="toolbar">
    <el-input v-model="model.keyword" clearable placeholder="输入姓名、岗位或学校" @keyup.enter="$emit('search')" />
    <el-select v-model="model.subsidiaryId" clearable filterable placeholder="筛选子公司" @change="$emit('search')">
      <el-option v-for="item in subsidiaries" :key="item.id" :label="item.subsidiaryName" :value="item.id" />
    </el-select>
    <el-input v-model="model.school" clearable placeholder="学校" @keyup.enter="$emit('search')" />
    <el-input v-model="model.major" clearable placeholder="专业" @keyup.enter="$emit('search')" />
    <el-select v-model="model.stage" clearable placeholder="筛选阶段" @change="$emit('search')">
      <el-option v-for="item in stageOptions" :key="item.value" :label="item.label" :value="item.value" />
    </el-select>
    <el-select v-model="scoreRange" clearable placeholder="系统匹配分区间" @change="handleScoreChange">
      <el-option v-for="item in scoreOptions" :key="item.value" :label="item.label" :value="item.value" />
    </el-select>
    <el-button type="primary" @click="$emit('search')">查询</el-button>
    <el-button @click="$emit('reset')">重置</el-button>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { APPLICATION_STAGE_OPTIONS } from '@/constants/status'

const props = defineProps({
  model: { type: Object, required: true },
  subsidiaries: { type: Array, default: () => [] }
})

const emit = defineEmits(['search', 'reset'])
const stageOptions = APPLICATION_STAGE_OPTIONS
const scoreRange = ref('')

const scoreOptions = [
  { label: '60 分以下', value: '0-59', min: 0, max: 59 },
  { label: '60-79 分', value: '60-79', min: 60, max: 79 },
  { label: '80-89 分', value: '80-89', min: 80, max: 89 },
  { label: '90 分及以上', value: '90-100', min: 90, max: 100 }
]

const handleScoreChange = (value) => {
  const target = scoreOptions.find((item) => item.value === value)
  props.model.scoreMin = target?.min ?? null
  props.model.scoreMax = target?.max ?? null
  emit('search')
}
</script>

<style scoped>
.toolbar {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 12px;
}
</style>
