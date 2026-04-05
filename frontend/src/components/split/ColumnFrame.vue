<template>
  <div class="column-frame" :style="{ gridTemplateColumns: `${leftWidth}% 8px 1fr` }">
    <div class="panel"><slot name="left" /></div>
    <div class="divider" @mousedown="startDrag"></div>
    <div class="panel"><slot name="right" /></div>
  </div>
</template>

<script setup>
const leftWidth = ref(48)
let dragging = false
const move = (event) => {
  if (!dragging) return
  leftWidth.value = Math.min(70, Math.max(30, (event.clientX / window.innerWidth) * 100))
}
const stop = () => { dragging = false }
const startDrag = () => { dragging = true }
onMounted(() => {
  window.addEventListener('mousemove', move)
  window.addEventListener('mouseup', stop)
})
onBeforeUnmount(() => {
  window.removeEventListener('mousemove', move)
  window.removeEventListener('mouseup', stop)
})
</script>

<style scoped>
.column-frame{display:grid;gap:12px;min-height:560px;}
.panel{overflow:auto;}
.divider{cursor:col-resize;background:linear-gradient(180deg,var(--primary-soft),rgba(0,0,0,0));border-radius:999px;}
@media (max-width: 960px){.column-frame{grid-template-columns:1fr;}.divider{display:none;}}
</style>
