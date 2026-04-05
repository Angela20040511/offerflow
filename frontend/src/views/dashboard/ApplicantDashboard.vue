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

    <section class="panel">
      <div class="panel-head">
        <h3>最近投递</h3>
      </div>
      <el-table :data="recentApplications" stripe>
        <el-table-column prop="candidateName" label="姓名" min-width="120" />
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

    <section class="panel">
      <div class="panel-head">
        <h3>推荐岗位</h3>
      </div>
      <div class="job-list">
        <JobCard v-for="item in recommendJobs" :key="item.id" :job="item" compact />
      </div>
    </section>
  </div>
</template>

<script setup>
import { computed, onMounted } from 'vue'
import { useStore } from 'vuex'
import JobCard from '@/components/job/JobCard.vue'
import StatusTag from '@/components/common/StatusTag.vue'

const store = useStore()
const overview = computed(() => store.state.user.applicantOverview || {})
const recentApplications = computed(() => overview.value.recentApplications || [])
const recommendJobs = computed(() => overview.value.recommendJobs || [])

onMounted(() => store.dispatch('user/fetchApplicantOverview'))
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
  background: rgba(255, 252, 247, 0.95);
  box-shadow: 0 14px 30px rgba(151, 110, 54, 0.12);
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
  background: #fff;
  border: 1px solid rgba(223, 205, 182, 0.7);
}

.summary-grid span {
  color: #8f7552;
}

.summary-grid strong {
  display: block;
  margin-top: 10px;
  font-size: 2rem;
  color: #2a2f35;
}

.panel-head {
  margin-bottom: 20px;
}

.job-list {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
  gap: 20px;
}
</style>
