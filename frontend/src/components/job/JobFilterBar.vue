<template>
  <div class="job-filter-bar">
    <div class="filter-grid">
      <el-select v-model="localValue.subsidiaryId" clearable placeholder="&#36873;&#25321;&#23376;&#20844;&#21496;" @change="emitChange"><el-option v-for="item in subsidiaries" :key="item.id" :label="item.subsidiaryName" :value="item.id" /></el-select>
      <el-input v-model="localValue.keyword" clearable placeholder="&#36755;&#20837;&#23703;&#20301;&#20851;&#38190;&#35789;" @keyup.enter="emitChange" />
      <el-select v-model="localValue.categoryId" clearable placeholder="&#36873;&#25321;&#23703;&#20301;&#31867;&#21035;" @change="emitChange"><el-option v-for="item in categories" :key="item.id" :label="item.categoryName" :value="item.id" /></el-select>
      <el-select v-model="localValue.provinceCode" clearable placeholder="&#36873;&#25321;&#30465;&#20221;" @change="handleProvinceChange"><el-option v-for="item in provinces" :key="item.code" :label="item.name" :value="item.code" /></el-select>
      <el-select v-model="localValue.cityCode" clearable placeholder="&#36873;&#25321;&#22478;&#24066;" @change="emitChange"><el-option v-for="item in cities" :key="item.code" :label="item.name" :value="item.code" /></el-select>
      <el-select v-model="localValue.salaryRange" clearable placeholder="&#36873;&#25321;&#34218;&#36164;&#21306;&#38388;" @change="emitChange"><el-option v-for="item in salaryOptions" :key="item.value" :label="item.label" :value="item.value" /></el-select>
      <el-select v-model="localValue.durationType" clearable placeholder="&#36873;&#25321;&#24037;&#20316;&#26102;&#38271;" @change="emitChange"><el-option v-for="item in durationOptions" :key="item.value" :label="item.label" :value="item.value" /></el-select>
      <el-select v-model="localValue.workMode" clearable placeholder="&#36873;&#25321;&#24037;&#20316;&#26041;&#24335;" @change="emitChange"><el-option v-for="item in workModeOptions" :key="item.value" :label="item.label" :value="item.value" /></el-select>
    </div>
    <div v-if="filterTags.length" class="tag-list"><el-tag v-for="tag in filterTags" :key="tag.key" closable @close="removeTag(tag.key)">{{ tag.label }}</el-tag><el-button link type="primary" @click="reset">&#28165;&#31354;&#31579;&#36873;</el-button></div>
    <div class="action-row"><el-button type="primary" @click="emitChange">&#31435;&#21363;&#31579;&#36873;</el-button><el-button @click="reset">&#37325;&#32622;&#26465;&#20214;</el-button></div>
  </div>
</template>
<script setup>
import { computed, reactive, watch } from 'vue'
const props = defineProps({ modelValue: { type: Object, default: () => ({}) }, subsidiaries: { type: Array, default: () => [] }, categories: { type: Array, default: () => [] }, provinces: { type: Array, default: () => [] }, cities: { type: Array, default: () => [] } })
const emit = defineEmits(['update:modelValue', 'change', 'province-change'])
const salaryOptions = [{ label: '150-180 \u5143/\u5929', value: '150-180' }, { label: '180-220 \u5143/\u5929', value: '180-220' }, { label: '220-260 \u5143/\u5929', value: '220-260' }, { label: '260 \u5143/\u5929\u4ee5\u4e0a', value: '260+' }]
const durationOptions = [{ label: '\u77ed\u671f\u5b9e\u4e60', value: 'SHORT_TERM' }, { label: '\u957f\u671f\u5b9e\u4e60', value: 'LONG_TERM' }, { label: '\u6821\u62db\u5168\u804c', value: 'FULL_TIME' }]
const workModeOptions = [{ label: '\u73b0\u573a\u529e\u516c', value: 'ONSITE' }, { label: '\u8fdc\u7a0b\u534f\u540c', value: 'REMOTE' }, { label: '\u6df7\u5408\u529e\u516c', value: 'HYBRID' }]
const localValue = reactive({ subsidiaryId: '', keyword: '', categoryId: '', provinceCode: '', cityCode: '', salaryRange: '', durationType: '', workMode: '' })
watch(() => props.modelValue, (value) => Object.assign(localValue, value || {}), { immediate: true, deep: true })
const optionLabel = (list, key, valueField = 'id', labelField = 'label') => (list.find((item) => String(item[valueField]) === String(key)) || {})[labelField] || ''
const filterTags = computed(() => { const tags = []; if (localValue.subsidiaryId) tags.push({ key: 'subsidiaryId', label: optionLabel(props.subsidiaries, localValue.subsidiaryId, 'id', 'subsidiaryName') }); if (localValue.keyword) tags.push({ key: 'keyword', label: `\u5173\u952e\u8bcd\uff1a${localValue.keyword}` }); if (localValue.categoryId) tags.push({ key: 'categoryId', label: optionLabel(props.categories, localValue.categoryId, 'id', 'categoryName') }); if (localValue.provinceCode) tags.push({ key: 'provinceCode', label: optionLabel(props.provinces, localValue.provinceCode, 'code', 'name') }); if (localValue.cityCode) tags.push({ key: 'cityCode', label: optionLabel(props.cities, localValue.cityCode, 'code', 'name') }); if (localValue.salaryRange) tags.push({ key: 'salaryRange', label: optionLabel(salaryOptions, localValue.salaryRange, 'value', 'label') }); if (localValue.durationType) tags.push({ key: 'durationType', label: optionLabel(durationOptions, localValue.durationType, 'value', 'label') }); if (localValue.workMode) tags.push({ key: 'workMode', label: optionLabel(workModeOptions, localValue.workMode, 'value', 'label') }); return tags })
const syncValue = () => emit('update:modelValue', { ...localValue })
const emitChange = () => { syncValue(); emit('change', { ...localValue }) }
const handleProvinceChange = () => { localValue.cityCode = ''; syncValue(); emit('province-change', localValue.provinceCode); emit('change', { ...localValue }) }
const removeTag = (key) => {
  localValue[key] = ''
  if (key === 'provinceCode') {
    localValue.cityCode = ''
    emit('province-change', '')
  }
  emitChange()
}
const reset = () => { Object.assign(localValue, { subsidiaryId: '', keyword: '', categoryId: '', provinceCode: '', cityCode: '', salaryRange: '', durationType: '', workMode: '' }); emit('province-change', ''); emitChange() }
</script>
<style scoped>.job-filter-bar{display:grid;gap:16px}.filter-grid{display:grid;grid-template-columns:repeat(4,minmax(0,1fr));gap:16px}.tag-list{display:flex;gap:10px;align-items:center;flex-wrap:wrap}.action-row{display:flex;gap:12px}@media(max-width:1200px){.filter-grid{grid-template-columns:repeat(2,minmax(0,1fr))}}@media(max-width:700px){.filter-grid{grid-template-columns:1fr}}</style>
