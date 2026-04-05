<template>
  <div class="form-block">
    <h3>个人基础资料</h3>
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
      <el-form-item label="学校">
        <el-input v-model="model.school" placeholder="请输入学校" />
      </el-form-item>
      <el-form-item label="专业">
        <el-input v-model="model.major" placeholder="请输入专业" />
      </el-form-item>
      <el-form-item label="求职方向">
        <el-input v-model="model.careerDirection" placeholder="请输入求职方向" />
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
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
</style>
