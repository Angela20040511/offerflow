<template>
  <div class="job-filter-bar">
    <div class="filter-grid">
      <el-select v-model="localValue.subsidiaryId" clearable placeholder="选择子公司"><el-option v-for="item in subsidiaries" :key="item.id" :label="item.subsidiaryName" :value="item.id" /></el-select>
      <el-input v-model="localValue.keyword" clearable placeholder="输入岗位关键字" @keyup.enter="emitSearch" />
      <el-select v-model="localValue.categoryId" clearable placeholder="选择岗位类别"><el-option v-for="item in categories" :key="item.id" :label="item.categoryName" :value="item.id" /></el-select>
      <el-select v-model="localValue.provinceCode" clearable placeholder="选择省份" @change="handleProvinceChange"><el-option v-for="item in provinces" :key="item.code" :label="item.name" :value="item.code" /></el-select>
      <el-select v-model="localValue.cityCode" clearable placeholder="选择城市"><el-option v-for="item in cities" :key="item.code" :label="item.name" :value="item.code" /></el-select>
      <el-select v-model="localValue.salaryRange" clearable placeholder="选择薪资区间"><el-option v-for="item in salaryOptions" :key="item.value" :label="item.label" :value="item.value" /></el-select>
      <el-select v-model="localValue.durationType" clearable placeholder="选择工作时长"><el-option v-for="item in durationOptions" :key="item.value" :label="item.label" :value="item.value" /></el-select>
      <el-select v-model="localValue.workMode" clearable placeholder="选择工作方式"><el-option v-for="item in workModeOptions" :key="item.value" :label="item.label" :value="item.value" /></el-select>
    </div>
    <div v-if="filterTags.length" class="tag-list"><el-tag v-for="tag in filterTags" :key="tag.key" closable @close="removeTag(tag.key)">{{ tag.label }}</el-tag><el-button link type="primary" @click="reset">清空筛选</el-button></div>
    <div class="action-row"><el-button type="primary" @click="emitSearch">立即筛选</el-button><el-button @click="reset">重置条件</el-button></div>
  </div>
</template>
<script setup>
import { computed, reactive, watch } from 'vue'
const props = defineProps({ modelValue: { type: Object, default: () => ({}) }, subsidiaries: { type: Array, default: () => [] }, categories: { type: Array, default: () => [] }, provinces: { type: Array, default: () => [] }, cities: { type: Array, default: () => [] } })
const emit = defineEmits(['update:modelValue', 'search', 'reset', 'province-change'])
const salaryOptions = [{ label: '150-180 \u5143/\u5929', value: '150-180' }, { label: '180-220 \u5143/\u5929', value: '180-220' }, { label: '220-260 \u5143/\u5929', value: '220-260' }, { label: '260 \u5143/\u5929\u4ee5\u4e0a', value: '260+' }]
const durationOptions = [{ label: '\u77ed\u671f\u5b9e\u4e60', value: 'SHORT_TERM' }, { label: '\u957f\u671f\u5b9e\u4e60', value: 'LONG_TERM' }, { label: '\u6821\u62db\u5168\u804c', value: 'FULL_TIME' }]
const workModeOptions = [{ label: '\u73b0\u573a\u529e\u516c', value: 'ONSITE' }, { label: '\u8fdc\u7a0b\u534f\u540c', value: 'REMOTE' }, { label: '\u6df7\u5408\u529e\u516c', value: 'HYBRID' }]
const localValue = reactive({ subsidiaryId: '', keyword: '', categoryId: '', provinceCode: '', cityCode: '', salaryRange: '', durationType: '', workMode: '' })
watch(() => props.modelValue, (value) => Object.assign(localValue, value || {}), { immediate: true, deep: true })
watch(localValue, () => syncValue(), { deep: true })
const optionLabel = (list, key, valueField = 'id', labelField = 'label') => (list.find((item) => String(item[valueField]) === String(key)) || {})[labelField] || ''
const filterTags = computed(() => { const tags = []; if (localValue.subsidiaryId) tags.push({ key: 'subsidiaryId', label: optionLabel(props.subsidiaries, localValue.subsidiaryId, 'id', 'subsidiaryName') }); if (localValue.keyword) tags.push({ key: 'keyword', label: `\u5173\u952e\u8bcd\uff1a${localValue.keyword}` }); if (localValue.categoryId) tags.push({ key: 'categoryId', label: optionLabel(props.categories, localValue.categoryId, 'id', 'categoryName') }); if (localValue.provinceCode) tags.push({ key: 'provinceCode', label: optionLabel(props.provinces, localValue.provinceCode, 'code', 'name') }); if (localValue.cityCode) tags.push({ key: 'cityCode', label: optionLabel(props.cities, localValue.cityCode, 'code', 'name') }); if (localValue.salaryRange) tags.push({ key: 'salaryRange', label: optionLabel(salaryOptions, localValue.salaryRange, 'value', 'label') }); if (localValue.durationType) tags.push({ key: 'durationType', label: optionLabel(durationOptions, localValue.durationType, 'value', 'label') }); if (localValue.workMode) tags.push({ key: 'workMode', label: optionLabel(workModeOptions, localValue.workMode, 'value', 'label') }); return tags })
const syncValue = () => emit('update:modelValue', { ...localValue })
const emitSearch = () => emit('search', { ...localValue })
const handleProvinceChange = () => { localValue.cityCode = ''; emit('province-change', localValue.provinceCode) }
const removeTag = (key) => {
  localValue[key] = ''
  if (key === 'provinceCode') {
    localValue.cityCode = ''
    emit('province-change', '')
  }
}
const reset = () => {
  Object.assign(localValue, { subsidiaryId: '', keyword: '', categoryId: '', provinceCode: '', cityCode: '', salaryRange: '', durationType: '', workMode: '' })
  emit('province-change', '')
  emit('reset', { ...localValue })
}
</script>
<style scoped>.job-filter-bar{display:grid;gap:16px}.filter-grid{display:grid;grid-template-columns:repeat(4,minmax(0,1fr));gap:16px}.tag-list{display:flex;gap:10px;align-items:center;flex-wrap:wrap}.action-row{display:flex;gap:12px}@media(max-width:1200px){.filter-grid{grid-template-columns:repeat(2,minmax(0,1fr))}}@media(max-width:700px){.filter-grid{grid-template-columns:1fr}}</style>
