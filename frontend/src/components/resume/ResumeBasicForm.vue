<template>
  <div class="form-block">
    <h3>个人基本信息</h3>
    <el-form label-position="top" :model="model" class="grid">
      <el-form-item label="姓名">
        <el-input v-model="model.name" placeholder="请输入真实姓名" />
      </el-form-item>
      <el-form-item label="手机">
        <el-input v-model="model.phone" placeholder="请输入手机号" />
      </el-form-item>
      <el-form-item label="邮箱">
        <el-input v-model="model.email" placeholder="请输入邮箱" />
      </el-form-item>
      <el-form-item label="学历">
        <el-select v-model="model.educationLevel" placeholder="请选择学历">
          <el-option v-for="item in educationLevelOptions" :key="item" :label="item" :value="item" />
        </el-select>
      </el-form-item>
      <el-form-item label="性别">
        <el-select v-model="model.gender" placeholder="请选择性别">
          <el-option v-for="item in genderOptions" :key="item" :label="item" :value="item" />
        </el-select>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { educationLevelOptions, genderOptions } from '@/utils/resume'

defineProps({
  model: { type: Object, required: true }
})
</script>

<style scoped>
.form-block {
  display: grid;
  gap: 16px;
}

.grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 16px;
}

@media (max-width: 720px) {
  .grid {
    grid-template-columns: 1fr;
  }
}
</style>
