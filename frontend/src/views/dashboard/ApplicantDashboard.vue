<template>
  <div class="applicant-dashboard">
    <section class="summary-card">
      <div>
        <h2>求职进展总览</h2>
        <p>查看集团岗位投递进度、收藏动态和简历准备情况。</p>
      </div>
      <div class="summary-grid">
        <article><span>投递总数</span><strong>{{ overview.applicationCount || 0 }}</strong></article>
        <article><span>面试中</span><strong>{{ overview.interviewCount || 0 }}</strong></article>
        <article><span>收藏岗位</span><strong>{{ overview.favoriteCount || 0 }}</strong></article>
        <article><span>简历完整度</span><strong>{{ overview.resumeCompleteScore || 0 }}%</strong></article>
      </div>
    </section>

    <section class="panel recommend-panel">
      <div class="panel-head">
        <div>
          <h3>为你推荐</h3>
          <p>精选岗位会自动横向滚动，悬停即可暂停。</p>
        </div>
      </div>
      <div class="carousel-shell">
        <div class="carousel-track" :class="{ animated: recommendJobs.length > 1 }">
          <JobCard
            v-for="(item, index) in carouselJobs"
            :key="`${item.id}-${index}`"
            :job="item"
            compact
            @detail="goDetail"
            @favorite="toggleFavorite"
          />
        </div>
      </div>
    </section>

    <section class="panel">
      <div class="panel-head">
        <h3>最近投递</h3>
      </div>
      <el-table :data="recentApplications" stripe>
        <el-table-column prop="candidateName" label="姓名" width="90" />
        <el-table-column prop="jobTitle" label="岗位" />
        <el-table-column prop="subsidiaryName" label="子公司" />
        <el-table-column prop="stage" label="阶段">
          <template #default="scope">
            <StatusTag :status="scope.row.stage" />
          </template>
        </el-table-column>
        <el-table-column prop="applyTime" label="投递时间" min-width="180" />
      </el-table>
    </section>
  </div>
</template>

<script setup>
import { computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'
import JobCard from '@/components/job/JobCard.vue'
import StatusTag from '@/components/common/StatusTag.vue'

const store = useStore()
const router = useRouter()
const overview = computed(() => store.state.user.applicantOverview || {})
const recentApplications = computed(() => overview.value.recentApplications || [])
const recommendJobs = computed(() => overview.value.recommendJobs || [])
const carouselJobs = computed(() => (recommendJobs.value.length > 1 ? [...recommendJobs.value, ...recommendJobs.value] : recommendJobs.value))

const loadOverview = () => store.dispatch('user/fetchApplicantOverview')

const goDetail = (job) => {
  router.push(`/applicant/jobs/${job.id}`)
}

const toggleFavorite = async (job) => {
  const isFavorite = await store.dispatch('user/toggleFavorite', job)
  await loadOverview()
  ElMessage.success(isFavorite ? '岗位已收藏' : '已取消收藏')
}

onMounted(loadOverview)
</script>

<style scoped>
.applicant-dashboard {
  display: grid;
  gap: 24px;
}

.summary-card,
.panel {
  padding: 28px;
  border-radius: 28px;
  background: var(--panel);
  border: 1px solid var(--line);
  box-shadow: var(--shadow);
}

.summary-grid {
  margin-top: 24px;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
  gap: 20px;
}

.summary-grid article {
  padding: 24px;
  border-radius: 20px;
  background: rgba(255, 255, 255, 0.9);
  border: 1px solid rgba(106, 122, 255, 0.14);
}

.summary-grid span {
  color: var(--muted);
}

.summary-grid strong {
  display: block;
  margin-top: 10px;
  font-size: 2rem;
  color: var(--text);
}

.panel-head {
  margin-bottom: 20px;
}

.panel-head p {
  color: var(--muted);
  margin-top: 8px;
}

.carousel-shell {
  overflow-x: auto;
}

.carousel-track {
  display: flex;
  gap: 18px;
  width: max-content;
}

.carousel-track.animated {
  animation: auto-slide 26s linear infinite;
}

.carousel-shell:hover .carousel-track {
  animation-play-state: paused;
}

@keyframes auto-slide {
  from {
    transform: translateX(0);
  }
  to {
    transform: translateX(calc(-50% - 9px));
  }
}
</style>
