<template><el-button :type="active ? 'warning' : 'default'" @click="$emit('toggle')">{{ active ? '已收藏' : '收藏' }}</el-button></template><script setup>defineProps({ active:Boolean })</script>
