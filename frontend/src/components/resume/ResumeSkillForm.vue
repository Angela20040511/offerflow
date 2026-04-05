<template>
  <div class="form-block">
    <h3>技能标签</h3>

    <div class="skill-section">
      <span>岗位相关技能</span>
      <el-checkbox-group v-model="model.jobSkills">
        <el-checkbox v-for="item in safeCandidates" :key="item" :label="item">{{ item }}</el-checkbox>
      </el-checkbox-group>
    </div>

    <div class="skill-section">
      <span>自定义补充技能</span>
      <el-input v-model="customSkill" placeholder="输入后按回车新增技能" @keyup.enter="addSkill" />
      <div class="tag-list">
        <el-tag v-for="item in model.customSkills" :key="item" closable @close="removeSkill(item)">
          {{ item }}
        </el-tag>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, ref, watchEffect } from 'vue'

const props = defineProps({
  model: { type: Object, required: true },
  candidates: { type: Array, default: () => [] }
})

const customSkill = ref('')
const fallbackCandidates = ['Vue 3', 'JavaScript', 'TypeScript', 'SQL', '数据分析', '产品运营']

watchEffect(() => {
  props.model.jobSkills ||= []
  props.model.customSkills ||= []
})

const safeCandidates = computed(() => (props.candidates?.length ? props.candidates : fallbackCandidates))

const addSkill = () => {
  const value = customSkill.value.trim()
  if (!value) return
  if (!props.model.customSkills.includes(value)) {
    props.model.customSkills.push(value)
  }
  customSkill.value = ''
}

const removeSkill = (value) => {
  const index = props.model.customSkills.indexOf(value)
  if (index > -1) {
    props.model.customSkills.splice(index, 1)
  }
}
</script>

<style scoped>
.form-block,
.skill-section {
  display: grid;
  gap: 14px;
}

.tag-list {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
}
</style>
