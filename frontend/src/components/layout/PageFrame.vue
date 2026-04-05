<template>
  <section class="page-shell">
    <header>
      <h2 class="page-title">{{ title }}</h2>
      <p v-if="desc" class="page-desc">{{ desc }}</p>
    </header>
    <slot />
  </section>
</template>

<script setup>
defineProps({ title: String, desc: String })
</script>

<style scoped>
.page-shell {
  display: grid;
  gap: 16px;
  min-width: 0;
  overflow: hidden;
}
</style>
