<template>
  <div class="home-frame">
    <aside class="sidebar card-panel"><LayoutMenu /></aside>
    <main class="main-area">
      <LayoutHead />
      <LayoutTabBar />
      <div class="content card-panel">
        <router-view v-slot="{ Component, route }">
          <keep-alive :include="keepAliveNames">
            <component :is="Component" :key="route.fullPath" />
          </keep-alive>
        </router-view>
      </div>
    </main>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useStore } from 'vuex'
import LayoutHead from '@/components/layout/LayoutHead.vue'
import LayoutMenu from '@/components/layout/LayoutMenu.vue'
import LayoutTabBar from '@/components/layout/LayoutTabBar.vue'

const store = useStore()
const keepAliveNames = computed(() => store.state.tabs.keepAliveNames)
</script>

<style scoped>
.home-frame { display: grid; grid-template-columns: 260px 1fr; min-height: 100vh; gap: 18px; padding: 18px; }
.sidebar { padding: 18px 10px; }
.main-area { display: flex; flex-direction: column; gap: 16px; min-width: 0; }
.content { padding: 22px; min-height: 70vh; min-width: 0; overflow: hidden; }
@media (max-width: 960px) { .home-frame { grid-template-columns: 1fr; } }
</style>
