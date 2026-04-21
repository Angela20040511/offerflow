<template>
  <div class="upload-panel">
    <h3>PDF 简历</h3>
    <p>上传后会在右侧显示完整多页预览，HR 端也会复用这份 PDF。</p>
    <el-upload :show-file-list="false" action="#" :auto-upload="false" :on-change="handleSelect">
      <el-button type="primary">{{ hasUploaded ? '更换 PDF' : '上传 PDF' }}</el-button>
    </el-upload>
    <div class="file-name">{{ fileName || '暂未上传 PDF 简历' }}</div>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  fileName: { type: String, default: '' }
})

const emit = defineEmits(['select'])

const hasUploaded = computed(() => Boolean(props.fileName))

const handleSelect = (file) => emit('select', file.raw)
</script>

<style scoped>
.upload-panel {
  display: grid;
  gap: 12px;
}

.upload-panel p,
.file-name {
  color: var(--muted);
}
</style>
