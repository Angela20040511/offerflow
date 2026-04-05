<template><el-card><div class="stat"><span>{{ label }}</span><strong>{{ value }}</strong></div></el-card></template>
<script setup>defineProps({ label: String, value: [String, Number] })</script>
<style scoped>.stat{display:flex;justify-content:space-between;align-items:center;font-size:14px;}strong{font-size:28px;}</style>
