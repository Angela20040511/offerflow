<template>
  <PageFrame title="候选人管理" desc="按子公司、岗位阶段和匹配分统一管理集团候选人。">
    <CandidateFilterBar :model="filters" :subsidiaries="subsidiaries" @search="load" @reset="reset" />
    <div class="table-wrap">
      <CandidateTable :data="list" @review="goReview" />
    </div>
  </PageFrame>
</template>

<script setup>
import { computed, onMounted, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'
import CandidateFilterBar from '@/components/hr/CandidateFilterBar.vue'
import CandidateTable from '@/components/hr/CandidateTable.vue'
import PageFrame from '@/components/layout/PageFrame.vue'

const store = useStore()
const router = useRouter()

const filters = reactive({
  keyword: '',
  subsidiaryId: '',
  school: '',
  major: '',
  stage: '',
  scoreMin: null,
  scoreMax: null
})

const list = computed(() => store.state.user.hrCandidates || [])
const subsidiaries = computed(() => store.state.common.subsidiaries || [])

const load = () => store.dispatch('user/fetchHrCandidates', { ...filters })

const reset = () => {
  Object.assign(filters, {
    keyword: '',
    subsidiaryId: '',
    school: '',
    major: '',
    stage: '',
    scoreMin: null,
    scoreMax: null
  })
  load()
}

const goReview = (row) => {
  router.push(`/hr/review/${row.applicationId || row.id}`)
}

onMounted(async () => {
  await store.dispatch('common/fetchBaseOptions')
  await load()
})
</script>

<style scoped>
.table-wrap {
  width: 100%;
  min-width: 0;
  overflow-x: auto;
}
</style>
