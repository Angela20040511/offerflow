<template><div class="empty card-panel"><p>{{ text }}</p></div></template>
<script setup>defineProps({ text: { type: String, default: '暂无数据' } })</script>
<style scoped>.empty{padding:36px;text-align:center;color:var(--muted);}</style>
