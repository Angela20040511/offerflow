<template>
  <div class="application-filter-bar">
    <el-select v-model="localStage" clearable placeholder="&#31579;&#36873;&#38454;&#27573;" @change="emitChange">
      <el-option v-for="item in stageOptions" :key="item.value" :label="item.label" :value="item.value" />
    </el-select>
    <el-button @click="reset">&#37325;&#32622;</el-button>
  </div>
</template>
<script setup>
import { computed } from 'vue'
import { applicationStageOptions } from '@/constants/status'
const props = defineProps({ modelValue: { type: Object, default: () => ({}) } })
const emit = defineEmits(['update:modelValue', 'change'])
const stageOptions = applicationStageOptions
const localStage = computed({ get: () => props.modelValue.stage || '', set: (value) => emit('update:modelValue', { ...props.modelValue, stage: value }) })
const emitChange = () => emit('change')
const reset = () => { emit('update:modelValue', { stage: '' }); emit('change') }
</script>
<style scoped>.application-filter-bar{display:flex;gap:12px;align-items:center;}</style>
