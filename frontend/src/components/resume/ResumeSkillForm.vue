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
      <div class="custom-skill-row">
        <el-input v-model="customSkill" placeholder="输入技能后按 Enter 或点击新增" @keyup.enter="addSkill" />
        <el-button type="primary" plain @click="addSkill">新增</el-button>
      </div>
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
import { ElMessage } from 'element-plus'

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
  if (!value) {
    ElMessage.warning('技能名称不能为空')
    return
  }
  const allSkills = [...props.model.jobSkills, ...props.model.customSkills]
  if (allSkills.includes(value)) {
    ElMessage.warning('技能不能重复添加')
    return
  }
  props.model.customSkills.push(value)
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

.custom-skill-row {
  display: grid;
  grid-template-columns: minmax(0, 1fr) auto;
  gap: 12px;
}

.tag-list {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
}
</style>
