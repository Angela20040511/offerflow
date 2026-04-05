<template>
  <div class="pdf-box card-panel">
    <el-empty v-if="!url" description="暂无 PDF 可预览" :image-size="88" />
    <div v-else-if="errorMessage" class="empty-text">{{ errorMessage }}</div>
    <canvas v-else ref="canvasRef"></canvas>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import pdfjsLib from '@/utils/pdf'

const props = defineProps({
  url: String,
  page: { type: Number, default: 1 },
  scale: { type: Number, default: 1.2 }
})

const canvasRef = ref(null)
const errorMessage = ref('')

const renderPdf = async () => {
  errorMessage.value = ''
  if (!props.url || !canvasRef.value) {
    return
  }
  try {
    const loadingTask = pdfjsLib.getDocument(props.url)
    const pdf = await loadingTask.promise
    const page = await pdf.getPage(props.page)
    const viewport = page.getViewport({ scale: props.scale })
    const canvas = canvasRef.value
    const context = canvas.getContext('2d')
    canvas.width = viewport.width
    canvas.height = viewport.height
    await page.render({ canvasContext: context, viewport }).promise
  } catch {
    errorMessage.value = 'PDF 加载失败，请稍后重试'
  }
}

watch(() => [props.url, props.page, props.scale], renderPdf, { immediate: true })
</script>

<style scoped>
.pdf-box {
  padding: 12px;
  overflow: auto;
}

canvas {
  max-width: 100%;
  display: block;
  margin: 0 auto;
}

.empty-text {
  color: #866846;
  text-align: center;
  padding: 32px 0;
}
</style>
