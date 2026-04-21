<template>
  <div class="job-center">
    <section class="hero">
      <div>
        <h2>岗位中心</h2>
        <p>统一浏览集团及各子公司的招聘岗位，支持筛选、分页、收藏与详情查看。</p>
      </div>
      <div class="hero-switch">
        <span>包含已关闭岗位</span>
        <el-switch v-model="filters.includeClosed" @change="handleStatusToggle" />
      </div>
    </section>

    <section class="panel">
      <JobFilterBar
        v-model="filters"
        :subsidiaries="subsidiaries"
        :categories="categories"
        :provinces="provinces"
        :cities="cities"
        @province-change="handleProvinceChange"
        @search="handleSearch"
        @reset="handleReset"
      />
    </section>

    <section class="content-grid">
      <div class="job-list-panel">
        <div class="job-list">
          <JobCard
            v-for="item in jobs"
            :key="item.id"
            :job="item"
            :selected="selectedJob?.id === item.id"
            @detail="goDetail"
            @favorite="favoriteJob"
            @click="selectJob(item)"
          />
          <el-empty v-if="!jobs.length" description="暂无符合条件的岗位" :image-size="96" />
        </div>
        <PaginationBar :total="total" :page-num="pageNum" :page-size="pageSize" @change="changePage" />
      </div>

      <aside class="preview-panel">
        <JobDetailPanel
          v-if="selectedJob"
          :job="selectedJob"
          :resumes="resumes"
          @apply="applyJob"
          @favorite="favoriteJob"
        />
        <el-empty v-else description="请选择岗位查看详情" :image-size="96" />
      </aside>
    </section>
  </div>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'
import PaginationBar from '@/components/common/PaginationBar.vue'
import JobCard from '@/components/job/JobCard.vue'
import JobDetailPanel from '@/components/job/JobDetailPanel.vue'
import JobFilterBar from '@/components/job/JobFilterBar.vue'

const store = useStore()
const router = useRouter()

const filters = reactive({
  subsidiaryId: '',
  keyword: '',
  categoryId: '',
  provinceCode: '',
  cityCode: '',
  salaryRange: '',
  durationType: '',
  workMode: '',
  includeClosed: false,
  pageNum: 1,
  pageSize: 6
})

const selectedJobId = ref(null)

const jobs = computed(() => store.state.job.list || [])
const total = computed(() => store.state.job.total || 0)
const pageNum = computed(() => store.state.job.filters.pageNum || 1)
const pageSize = computed(() => store.state.job.filters.pageSize || 6)
const resumes = computed(() => store.state.resume.list || [])
const subsidiaries = computed(() => store.state.common.subsidiaries || [])
const categories = computed(() => store.state.common.categories || [])
const provinces = computed(() => store.state.common.provinces || [])
const cities = computed(() => store.getters['common/cityOptions'](filters.provinceCode))
const selectedJob = computed(() => jobs.value.find((item) => item.id === selectedJobId.value) || jobs.value[0] || null)

const syncSelectedJob = () => {
  if (!jobs.value.length) {
    selectedJobId.value = null
    return
  }
  if (!jobs.value.some((item) => item.id === selectedJobId.value)) {
    selectedJobId.value = jobs.value[0].id
  }
}

const buildQuery = (payload = {}) => {
  const next = {
    ...filters,
    ...payload
  }
  next.status = next.includeClosed ? '' : 'OPEN'
  return next
}

const loadJobs = async (payload = {}) => {
  await store.dispatch('job/fetchList', buildQuery(payload))
  syncSelectedJob()
}

const handleProvinceChange = async (provinceCode) => {
  filters.cityCode = ''
  if (provinceCode) {
    await store.dispatch('common/fetchCities', provinceCode)
  }
}

const handleSearch = async (payload) => {
  Object.assign(filters, payload || {})
  filters.pageNum = 1
  await loadJobs({ ...filters, pageNum: 1 })
}

const handleReset = async (payload) => {
  Object.assign(filters, payload || {})
  filters.includeClosed = false
  filters.pageNum = 1
  await loadJobs({ ...filters, pageNum: 1, status: 'OPEN' })
}

const handleStatusToggle = async () => {
  filters.pageNum = 1
  await loadJobs({ ...filters, pageNum: 1 })
}

const changePage = async (nextPage) => {
  filters.pageNum = nextPage
  await loadJobs({ ...filters, pageNum: nextPage })
}

const selectJob = (job) => {
  selectedJobId.value = job.id
}

const goDetail = (job) => {
  router.push(`/applicant/jobs/${job.id}`)
}

const favoriteJob = async (job) => {
  const isFavorite = await store.dispatch('user/toggleFavorite', job)
  await loadJobs({ ...filters, pageNum: pageNum.value })
  selectedJobId.value = job.id
  ElMessage.success(isFavorite ? '岗位已收藏' : '已取消收藏')
}

const applyJob = async ({ job, resumeId }) => {
  if (!resumes.value.length) {
    ElMessage.warning('请先去简历中心创建简历版本')
    return
  }
  await store.dispatch('user/applyJob', { jobId: job.id, resumeId })
  await loadJobs({ ...filters, pageNum: pageNum.value })
  selectedJobId.value = job.id
  ElMessage.success('投递成功')
}

onMounted(async () => {
  await store.dispatch('common/fetchBaseOptions')
  await store.dispatch('resume/fetchMyList')
  await loadJobs({ ...filters, pageNum: 1, pageSize: 6 })
})
</script>

<style scoped>
.job-center {
  display: grid;
  gap: 24px;
  min-width: 0;
}

.hero,
.panel,
.preview-panel,
.job-list-panel {
  padding: 28px;
  border-radius: 28px;
  background: var(--panel);
  border: 1px solid var(--line);
  box-shadow: var(--shadow);
}

.hero {
  display: flex;
  justify-content: space-between;
  gap: 20px;
}

.hero-switch {
  display: flex;
  align-items: center;
  gap: 12px;
  color: var(--muted);
}

.content-grid {
  display: grid;
  grid-template-columns: minmax(0, 1.4fr) minmax(320px, 0.9fr);
  gap: 24px;
  min-width: 0;
  align-items: start;
}

.job-list {
  display: grid;
  gap: 20px;
  min-width: 0;
}

.job-list-panel,
.preview-panel {
  display: grid;
  gap: 20px;
  min-width: 0;
}

@media (max-width: 1200px) {
  .content-grid {
    grid-template-columns: 1fr;
  }

  .hero {
    flex-direction: column;
  }
}
</style>
