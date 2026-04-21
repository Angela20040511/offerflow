<template>
  <div class="pager">
    <el-pagination
      background
      layout="total, prev, pager, next"
      :current-page="pageNum"
      :page-size="pageSize"
      :total="total"
      @current-change="$emit('change', $event)"
    />
  </div>
</template>

<script setup>
defineProps({ total: Number, pageNum: Number, pageSize: Number })
</script>

<style scoped>
.pager {
  display: flex;
  justify-content: flex-end;
  padding-top: 12px;
}
</style>
