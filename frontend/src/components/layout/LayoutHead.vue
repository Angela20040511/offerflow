<template>
  <header class="layout-head">
    <div>
      <h1 class="brand">OfferFlow</h1>
      <p class="subtitle">&#38598;&#22242;&#32479;&#19968;&#25307;&#32856;&#38376;&#25143;</p>
    </div>
    <div class="head-actions">
      <el-tag type="primary" effect="light">{{ roleLabel }}</el-tag>
      <span class="username">{{ displayName }}</span>
      <el-button text @click="logout">&#36864;&#20986;&#30331;&#24405;</el-button>
    </div>
  </header>
</template>

<script setup>
import { computed } from 'vue'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'
import { roleLabelMap } from '@/constants/role'

const store = useStore()
const router = useRouter()

const user = computed(() => store.state.user.profile || {})
const roleLabel = computed(() => roleLabelMap[user.value.role] || '\u8bbf\u5ba2')
const displayName = computed(() => user.value.name || user.value.username || '\u672a\u767b\u5f55\u7528\u6237')

const logout = async () => {
  await store.dispatch('user/logout')
  router.push('/login')
}
</script>

<style scoped>
.layout-head {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 28px 32px;
  background: rgba(255, 252, 247, 0.95);
  border-radius: 28px;
  box-shadow: 0 14px 32px rgba(151, 110, 54, 0.12);
}
.brand { margin: 0; font-size: 2rem; color: #2b2f37; }
.subtitle { margin: 8px 0 0; color: #866846; }
.head-actions { display: flex; align-items: center; gap: 16px; color: #6f7683; }
.username { font-size: 1.1rem; }
</style>
