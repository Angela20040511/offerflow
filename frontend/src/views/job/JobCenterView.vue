<template>
  <div class="job-center">
    <section class="hero">
      <h2>岗位中心</h2>
      <p>统一浏览集团及各子公司的招聘岗位，支持按子公司、类别、地区和工作方式筛选。</p>
    </section>

    <section class="panel">
      <JobFilterBar
        v-model="filters"
        :subsidiaries="subsidiaries"
        :categories="categories"
        :provinces="provinces"
        :cities="cities"
        @province-change="handleProvinceChange"
        @change="handleFilterChange"
      />
    </section>

    <section class="content-grid">
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
  workMode: ''
})

const selectedJobId = ref(null)

const jobs = computed(() => store.state.job.list || [])
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

const loadJobs = async (payload = {}) => {
  await store.dispatch('job/fetchList', payload)
  syncSelectedJob()
}

const handleProvinceChange = async (provinceCode) => {
  filters.cityCode = ''
  if (provinceCode) {
    await store.dispatch('common/fetchCities', provinceCode)
  }
}

const handleFilterChange = async () => {
  await loadJobs({ ...filters, pageNum: 1 })
}

const selectJob = (job) => {
  selectedJobId.value = job.id
}

const goDetail = (job) => {
  router.push(`/applicant/jobs/${job.id}`)
}

const favoriteJob = async (job) => {
  await store.dispatch('user/toggleFavorite', job)
  await loadJobs({ ...filters })
  selectedJobId.value = job.id
  ElMessage.success(job.isFavorite ? '已取消收藏' : '岗位已收藏')
}

const applyJob = async ({ job, resumeId }) => {
  if (!resumes.value.length) {
    ElMessage.warning('请先去简历中心创建简历版本')
    return
  }
  await store.dispatch('user/applyJob', { jobId: job.id, resumeId })
  await loadJobs({ ...filters })
  selectedJobId.value = job.id
  ElMessage.success('投递成功')
}

onMounted(async () => {
  await store.dispatch('common/fetchBaseOptions')
  await store.dispatch('resume/fetchMyList')
  await loadJobs({ ...filters })
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
.preview-panel {
  padding: 28px;
  border-radius: 28px;
  background: rgba(255, 252, 247, 0.95);
  box-shadow: 0 14px 30px rgba(151, 110, 54, 0.12);
}

.content-grid {
  display: grid;
  grid-template-columns: minmax(0, 1.4fr) minmax(320px, 0.9fr);
  gap: 24px;
  min-width: 0;
}

.job-list,
.preview-panel {
  display: grid;
  gap: 20px;
  min-width: 0;
}

@media (max-width: 1200px) {
  .content-grid {
    grid-template-columns: 1fr;
  }
}
</style>
