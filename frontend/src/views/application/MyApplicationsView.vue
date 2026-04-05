<template>
  <PageFrame title="我的投递" desc="统一查看集团各子公司的投递进展、简历版本和评估结果。">
    <section class="panel">
      <ApplicationFilterBar v-model="filters" @change="loadApplications" />
      <div class="table-wrap">
        <ApplicationTable :data="applications" @withdraw="handleWithdraw" />
      </div>
    </section>
  </PageFrame>
</template>

<script setup>
import { computed, onMounted, reactive } from 'vue'
import { ElMessageBox } from 'element-plus'
import { useStore } from 'vuex'
import ApplicationFilterBar from '@/components/application/ApplicationFilterBar.vue'
import ApplicationTable from '@/components/application/ApplicationTable.vue'
import PageFrame from '@/components/layout/PageFrame.vue'

const store = useStore()
const filters = reactive({ stage: '' })
const applications = computed(() => store.state.user.myApplications || [])

const loadApplications = () => store.dispatch('user/fetchMyApplications', { ...filters })

const handleWithdraw = async (row) => {
  await ElMessageBox.confirm(`确认撤回“${row.jobTitle}”的投递吗？`, '撤回投递', { type: 'warning' })
  await store.dispatch('user/withdrawApplication', row.applicationId || row.id)
}

onMounted(loadApplications)
</script>

<style scoped>
.panel {
  padding: 28px;
  border-radius: 28px;
  background: rgba(255, 252, 247, 0.95);
  box-shadow: 0 14px 30px rgba(151, 110, 54, 0.12);
}

.table-wrap {
  width: 100%;
  min-width: 0;
  overflow-x: auto;
  margin-top: 16px;
}
</style>
