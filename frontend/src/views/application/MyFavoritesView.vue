<template>
  <PageFrame title="我的收藏" desc="按子公司和岗位状态查看已收藏的集团岗位。">
    <section class="panel">
      <div class="toolbar">
        <el-select v-model="filters.subsidiaryId" clearable placeholder="筛选子公司" @change="applyFilter">
          <el-option v-for="item in subsidiaries" :key="item.id" :label="item.subsidiaryName" :value="item.id" />
        </el-select>
        <el-switch v-model="filters.openOnly" active-text="只看招聘中岗位" @change="applyFilter" />
      </div>

      <div class="job-list">
        <JobCard
          v-for="item in favorites"
          :key="item.id"
          :job="item"
          @detail="goDetail"
          @favorite="toggleFavorite"
        />
        <el-empty v-if="!favorites.length" description="暂无收藏岗位" :image-size="96" />
      </div>
    </section>
  </PageFrame>
</template>

<script setup>
import { computed, onMounted, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'
import JobCard from '@/components/job/JobCard.vue'
import PageFrame from '@/components/layout/PageFrame.vue'

const store = useStore()
const router = useRouter()

const filters = reactive({
  subsidiaryId: '',
  openOnly: false
})

const subsidiaries = computed(() => store.state.common.subsidiaries || [])
const favorites = computed(() => store.state.user.favorites || [])

const applyFilter = async () => {
  await store.dispatch('user/fetchFavorites', {
    subsidiaryId: filters.subsidiaryId || null,
    status: filters.openOnly ? 'OPEN' : ''
  })
}

const goDetail = (job) => {
  router.push(`/applicant/jobs/${job.id}`)
}

const toggleFavorite = async (job) => {
  await store.dispatch('user/toggleFavorite', job)
  await applyFilter()
}

onMounted(async () => {
  await store.dispatch('common/fetchBaseOptions')
  await applyFilter()
})
</script>

<style scoped>
.panel {
  padding: 28px;
  border-radius: 28px;
  background: rgba(255, 252, 247, 0.95);
  box-shadow: 0 14px 30px rgba(151, 110, 54, 0.12);
}

.toolbar {
  display: flex;
  gap: 16px;
  align-items: center;
  margin-bottom: 20px;
}

.job-list {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
  gap: 20px;
}
</style>
