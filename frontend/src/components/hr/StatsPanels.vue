<template>
  <div class="section-grid">
    <StatCard v-for="item in safeItems" :key="item.label" :label="item.label" :value="item.value" />
  </div>
</template>

<script setup>
import { computed } from 'vue'
import StatCard from '@/components/common/StatCard.vue'

const props = defineProps({
  items: { type: Array, default: () => [] }
})

const safeItems = computed(() => props.items || [])
</script>

<style scoped>
.section-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
  gap: 16px;
}
</style>
