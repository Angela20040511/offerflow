<template>
  <div class="form-block">
    <div class="block-head">
      <h3>教育经历</h3>
      <el-button @click="addItem">新增教育经历</el-button>
    </div>

    <div v-for="(item, index) in model" :key="index" class="entry-card">
      <div class="entry-head">
        <strong>教育经历 {{ index + 1 }}</strong>
        <el-button link type="danger" @click="removeItem(index)">删除</el-button>
      </div>
      <el-form label-position="top" class="grid">
        <el-form-item label="学校">
          <el-input v-model="item.school" />
        </el-form-item>
        <el-form-item label="教育阶段">
          <el-select v-model="item.educationStage" placeholder="请选择教育阶段">
            <el-option v-for="option in educationLevelOptions" :key="option" :label="option" :value="option" />
          </el-select>
        </el-form-item>
        <el-form-item label="专业">
          <el-input v-model="item.major" />
        </el-form-item>
        <el-form-item label="开始时间">
          <el-date-picker v-model="item.startDate" type="month" value-format="YYYY-MM" placeholder="选择月份" />
        </el-form-item>
        <el-form-item label="结束时间">
          <el-date-picker v-model="item.endDate" type="month" value-format="YYYY-MM" placeholder="选择月份" />
        </el-form-item>
        <el-form-item label="主修课程" class="full">
          <el-input v-model="item.courses" type="textarea" :rows="3" />
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { educationLevelOptions } from '@/utils/resume'

const props = defineProps({
  model: { type: Array, required: true }
})

const addItem = () => {
  props.model.push({
    school: '',
    educationStage: '',
    major: '',
    startDate: '',
    endDate: '',
    courses: ''
  })
}

const removeItem = (index) => {
  props.model.splice(index, 1)
}
</script>

<style scoped>
.form-block {
  display: grid;
  gap: 16px;
}

.block-head,
.entry-head {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.entry-card {
  padding: 18px;
  border-radius: 20px;
  border: 1px solid rgba(116, 132, 255, 0.14);
  background: rgba(255, 255, 255, 0.88);
}

.grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 16px;
}

.full {
  grid-column: 1 / -1;
}

@media (max-width: 720px) {
  .grid {
    grid-template-columns: 1fr;
  }
}
</style>
