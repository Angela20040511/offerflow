<template>
  <div class="upload-panel">
    <h3>PDF 附件简历</h3>
    <p>上传 PDF 简历后，可在右侧实时预览。</p>
    <el-upload :show-file-list="false" action="#" :auto-upload="false" :on-change="handleSelect">
      <el-button type="primary" plain>选择 PDF 文件</el-button>
    </el-upload>
    <div class="file-name">{{ fileName || '暂未上传 PDF 简历' }}</div>
  </div>
</template>

<script setup>
defineProps({
  fileName: { type: String, default: '' }
})

const emit = defineEmits(['select'])

const handleSelect = (file) => emit('select', file.raw)
</script>

<style scoped>
.upload-panel {
  display: grid;
  gap: 12px;
}

.file-name {
  color: #866846;
}
</style>
