<template>
  <div class="form-block">
    <div class="block-head">
      <h3>实习经历</h3>
      <el-button @click="addItem">新增实习经历</el-button>
    </div>

    <div v-for="(item, index) in model" :key="index" class="entry-card">
      <div class="entry-head">
        <strong>实习经历 {{ index + 1 }}</strong>
        <el-button link type="danger" @click="removeItem(index)">删除</el-button>
      </div>
      <el-form label-position="top" class="grid">
        <el-form-item label="公司">
          <el-input v-model="item.company" />
        </el-form-item>
        <el-form-item label="岗位">
          <el-input v-model="item.position" />
        </el-form-item>
        <el-form-item label="开始时间">
          <el-input v-model="item.startDate" />
        </el-form-item>
        <el-form-item label="结束时间">
          <el-input v-model="item.endDate" />
        </el-form-item>
        <el-form-item label="地点">
          <el-input v-model="item.location" />
        </el-form-item>
        <el-form-item label="工作内容" class="full">
          <el-input v-model="item.content" type="textarea" :rows="4" />
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
    company: '',
    position: '',
    startDate: '',
    endDate: '',
    location: '',
    content: ''
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
