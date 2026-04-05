<template>
  <div class="page">
    <section class="hero"><h2>&#24212;&#32856;&#25351;&#21335;</h2><p>&#38598;&#20013;&#26597;&#38405;&#38598;&#22242;&#32479;&#19968;&#25307;&#32856;&#38376;&#25143;&#30340; PDF &#25351;&#21335;&#25991;&#26723;&#12290;</p></section>
    <section class="panel guide-layout">
      <aside class="guide-list">
        <button v-for="item in guides" :key="item.code" class="guide-item" :class="{ active: item.code === activeCode }" @click="activeCode = item.code">{{ item.title }}</button>
      </aside>
      <div class="guide-viewer"><iframe v-if="activeGuide?.fileUrl" :src="activeGuide.fileUrl" title="guide-pdf" /><el-empty v-else description="&#26242;&#26080;&#25351;&#21335;&#20869;&#23481;" /></div>
    </section>
  </div>
</template>
<script setup>
import { computed, onMounted, ref } from 'vue'
import { useStore } from 'vuex'
const store = useStore(); const activeCode = ref(''); const guides = computed(() => store.state.user.guides || []); const activeGuide = computed(() => guides.value.find((item) => item.code === activeCode.value) || guides.value[0]); onMounted(async () => { await store.dispatch('user/fetchGuides'); activeCode.value = guides.value[0]?.code || '' })
</script>
<style scoped>.page{display:grid;gap:24px}.hero,.panel{padding:28px;border-radius:28px;background:rgba(255,252,247,.95);box-shadow:0 14px 30px rgba(151,110,54,.12)}.guide-layout{display:grid;grid-template-columns:260px 1fr;gap:20px;min-height:760px}.guide-list{display:grid;gap:12px}.guide-item{padding:16px;border:1px solid rgba(223,205,182,.7);border-radius:18px;background:#fff;text-align:left;cursor:pointer}.guide-item.active{color:#2f68d8;border-color:rgba(47,104,216,.4)}.guide-viewer iframe{width:100%;min-height:700px;border:none;border-radius:20px;background:#fff}</style>
