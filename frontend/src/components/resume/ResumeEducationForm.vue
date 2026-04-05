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
        <el-form-item label="学历">
          <el-input v-model="item.degree" />
        </el-form-item>
        <el-form-item label="专业">
          <el-input v-model="item.major" />
        </el-form-item>
        <el-form-item label="开始时间">
          <el-input v-model="item.startDate" placeholder="例如：2022.09" />
        </el-form-item>
        <el-form-item label="结束时间">
          <el-input v-model="item.endDate" placeholder="例如：2026.06" />
        </el-form-item>
        <el-form-item label="亮点补充" class="full">
          <el-input v-model="item.highlight" type="textarea" :rows="3" />
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
const props = defineProps({
  model: { type: Array, required: true }
})

const addItem = () => {
  props.model.push({
    school: '',
    degree: '',
    major: '',
    startDate: '',
    endDate: '',
    highlight: ''
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
  border: 1px solid rgba(223, 205, 182, 0.8);
  background: #fff;
}

.grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 16px;
}

.full {
  grid-column: 1 / -1;
}
</style>
