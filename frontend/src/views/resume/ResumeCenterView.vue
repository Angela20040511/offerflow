<template>
  <div class="resume-center">
    <section class="hero">
      <h2>简历中心</h2>
      <p>在“可编辑简历”和“PDF 简历”两个子页面之间切换，分别维护结构化内容与附件简历。</p>
    </section>

    <section class="tab-panel">
      <el-radio-group :model-value="currentTab" size="large" @change="switchTab">
        <el-radio-button label="editor">可编辑简历</el-radio-button>
        <el-radio-button label="pdf">上传 PDF 简历</el-radio-button>
      </el-radio-group>
    </section>

    <router-view />
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

const currentTab = computed(() => (route.path.endsWith('/pdf') ? 'pdf' : 'editor'))

const switchTab = (tab) => {
  router.push(tab === 'pdf' ? '/applicant/resume/pdf' : '/applicant/resume/editor')
}
</script>

<style scoped>
.resume-center {
  display: grid;
  gap: 24px;
}

.hero,
.tab-panel {
  padding: 28px;
  border-radius: 28px;
  background: rgba(255, 252, 247, 0.95);
  box-shadow: 0 14px 30px rgba(151, 110, 54, 0.12);
}
</style>
