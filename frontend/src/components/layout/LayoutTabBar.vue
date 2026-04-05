<template>
  <div class="tab-bar card-panel">
    <el-tag
      v-for="tab in tabs"
      :key="tab.path"
      :type="tab.path === active ? '' : 'info'"
      closable
      @click="open(tab.path)"
      @close="close(tab.path)"
    >
      {{ tab.title }}
    </el-tag>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'

const router = useRouter()
const store = useStore()

const tabs = computed(() => store.state.tabs.openedTabs)
const active = computed(() => store.state.tabs.activePath)
const fallback = computed(() => (store.getters['user/role'] === 'HR' ? '/hr/dashboard' : '/applicant/dashboard'))

const open = (path) => {
  if (path) {
    router.push(path)
  }
}

const close = (path) => {
  const currentTabs = [...tabs.value]
  const currentIndex = currentTabs.findIndex((item) => item.path === path)
  const wasActive = active.value === path
  store.commit('tabs/removeTab', path)

  if (!wasActive) {
    return
  }

  const remaining = store.state.tabs.openedTabs
  if (!remaining.length) {
    router.push(fallback.value)
    return
  }

  const nextTab = remaining[currentIndex] || remaining[currentIndex - 1] || remaining[remaining.length - 1]
  router.push(nextTab?.path || fallback.value)
}
</script>

<style scoped>
.tab-bar {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
  padding: 12px 16px;
}
</style>
