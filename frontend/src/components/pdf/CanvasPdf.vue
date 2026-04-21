<template>
  <div class="pdf-box card-panel">
    <el-empty v-if="!url" :description="emptyText" :image-size="88" />
    <div v-else-if="errorMessage" class="empty-text">{{ errorMessage }}</div>
    <div v-else ref="pagesRef" class="pdf-pages"></div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import pdfjsLib from '@/utils/pdf'

const props = defineProps({
  url: { type: String, default: '' },
  scale: { type: Number, default: 1.2 },
  emptyText: { type: String, default: '暂无 PDF 可预览' }
})

const pagesRef = ref(null)
const errorMessage = ref('')

const renderPdf = async () => {
  errorMessage.value = ''
  if (!props.url || !pagesRef.value) {
    return
  }

  const container = pagesRef.value
  container.innerHTML = ''

  try {
    const loadingTask = pdfjsLib.getDocument(props.url)
    const pdf = await loadingTask.promise
    for (let pageNumber = 1; pageNumber <= pdf.numPages; pageNumber += 1) {
      const page = await pdf.getPage(pageNumber)
      const viewport = page.getViewport({ scale: props.scale })
      const canvas = document.createElement('canvas')
      const caption = document.createElement('div')
      const context = canvas.getContext('2d')

      canvas.width = viewport.width
      canvas.height = viewport.height
      canvas.className = 'pdf-canvas'
      caption.className = 'pdf-caption'
      caption.textContent = `第 ${pageNumber} 页 / 共 ${pdf.numPages} 页`

      container.appendChild(caption)
      container.appendChild(canvas)
      await page.render({ canvasContext: context, viewport }).promise
    }
  } catch {
    errorMessage.value = 'PDF 加载失败，请稍后重试'
  }
}

watch(() => [props.url, props.scale], renderPdf, { immediate: true })
</script>

<style scoped>
.pdf-box {
  padding: 16px;
  overflow: auto;
}

.pdf-pages {
  display: grid;
  gap: 12px;
}

.pdf-pages :deep(.pdf-canvas) {
  max-width: 100%;
  display: block;
  margin: 0 auto;
  border-radius: 16px;
  border: 1px solid rgba(106, 122, 255, 0.14);
  background: #fff;
  box-shadow: 0 12px 24px rgba(11, 20, 66, 0.08);
}

.pdf-pages :deep(.pdf-caption) {
  color: var(--muted);
  font-size: 13px;
  text-align: center;
}

.empty-text {
  color: var(--muted);
  text-align: center;
  padding: 32px 0;
}
</style>
