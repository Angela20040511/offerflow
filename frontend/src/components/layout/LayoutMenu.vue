<template>
  <el-menu :default-active="activePath" class="side-menu" @select="go">
    <el-menu-item v-for="item in items" :key="item.key" :index="item.key">
      {{ item.label }}
    </el-menu-item>
  </el-menu>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'
import { menuByRole } from '@/constants/menu'

const router = useRouter()
const store = useStore()

const normalizeActivePath = (path) => {
  if (path.startsWith('/applicant/resume')) {
    return '/applicant/resume'
  }
  return path
}

const activePath = computed(() => normalizeActivePath(router.currentRoute.value.path))
const items = computed(() => menuByRole[store.getters['user/role']] || [])

const go = (path) => router.push(path)
</script>

<style scoped>
.side-menu {
  height: 100%;
  background: transparent;
}
</style>
