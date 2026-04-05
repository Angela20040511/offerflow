<template>
  <div class="template-selector">
    <button
      v-for="item in templates"
      :key="item.code"
      class="template-item"
      :class="{ active: modelValue === item.code }"
      @click="$emit('update:modelValue', item.code)"
    >
      <strong>{{ item.name }}</strong>
      <span>{{ item.description }}</span>
    </button>
  </div>
</template>

<script setup>
defineProps({
  modelValue: { type: String, default: 'classic' },
  templates: { type: Array, default: () => [] }
})

defineEmits(['update:modelValue'])
</script>

<style scoped>
.template-selector {
  display: grid;
  gap: 12px;
}

.template-item {
  padding: 18px;
  border-radius: 18px;
  border: 1px solid rgba(223, 205, 182, 0.8);
  background: #fff;
  text-align: left;
  cursor: pointer;
}

.template-item.active {
  border-color: rgba(47, 104, 216, 0.4);
  box-shadow: 0 10px 24px rgba(47, 104, 216, 0.12);
}

.template-item span {
  display: block;
  margin-top: 8px;
  color: #866846;
}
</style>
