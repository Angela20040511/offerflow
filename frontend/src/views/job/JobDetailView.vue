<template>
  <div class="job-detail-view">
    <section class="hero">
      <h2>岗位详情</h2>
      <p>查看岗位完整信息，并选择最合适的简历版本进行投递。</p>
    </section>

    <JobDetailPanel v-if="job" :job="job" :resumes="resumes" @apply="handleApply" @favorite="handleFavorite" />
    <el-empty v-else-if="!loading" description="岗位详情加载失败或已不存在" :image-size="96" />
  </div>
</template>

<script setup>
import { computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useRoute } from 'vue-router'
import { useStore } from 'vuex'
import JobDetailPanel from '@/components/job/JobDetailPanel.vue'

const store = useStore()
const route = useRoute()

const job = computed(() => store.state.job.detail)
const resumes = computed(() => store.state.resume.list || [])
const loading = computed(() => store.state.job.loading)

const load = async () => {
  await store.dispatch('job/fetchDetail', route.params.id)
  await store.dispatch('resume/fetchMyList')
}

const handleApply = async ({ job, resumeId }) => {
  await store.dispatch('user/applyJob', { jobId: job.id, resumeId })
  await store.dispatch('job/fetchDetail', route.params.id)
  ElMessage.success('投递成功')
}

const handleFavorite = async (payload) => {
  const isFavorite = await store.dispatch('user/toggleFavorite', payload)
  await store.dispatch('job/fetchDetail', route.params.id)
  ElMessage.success(isFavorite ? '岗位已收藏' : '已取消收藏')
}

onMounted(load)
</script>

<style scoped>
.job-detail-view {
  display: grid;
  gap: 24px;
}

.hero {
  padding: 28px;
  border-radius: 28px;
  background: var(--panel);
  border: 1px solid var(--line);
  box-shadow: var(--shadow);
}

.hero p {
  color: var(--muted);
}
</style>
