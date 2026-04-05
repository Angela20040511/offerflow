<template>
  <div class="resume-center">
    <section class="hero">
      <h2>简历中心</h2>
      <p>维护多份简历版本，分别匹配集团不同子公司与岗位类别。</p>
    </section>

    <section class="layout">
      <aside class="sidebar panel">
        <div class="sidebar-head">
          <h3>简历版本</h3>
          <el-button type="primary" plain @click="createVersion">新建版本</el-button>
        </div>

        <div class="version-list">
          <button
            v-for="item in resumes"
            :key="item.id"
            class="version-item"
            :class="{ active: activeResumeId === item.id }"
            @click="selectResume(item.id)"
          >
            <div>
              <strong>{{ item.resumeName }}</strong>
              <p>{{ item.resumeType || defaultResumeType }}</p>
            </div>
            <el-tag v-if="item.isDefault" type="success" effect="light">默认</el-tag>
          </button>
        </div>

        <div v-if="activeResume" class="sidebar-actions">
          <el-button @click="setDefault">设为默认</el-button>
          <el-button type="danger" plain :disabled="activeResume.isDefault" @click="removeVersion">删除版本</el-button>
        </div>

        <ResumeTemplateSelector v-if="activeResume" v-model="activeResume.templateCode" :templates="templates" />
        <ResumeUploadPanel v-if="activeResume" :file-name="pdfFileName" @select="uploadPdf" />
      </aside>

      <div class="editor-column">
        <section v-if="activeResume" class="panel">
          <div class="section-head">
            <div>
              <h3>{{ activeResume.resumeName }}</h3>
              <p>基础资料、结构化内容和 PDF 简历在这里统一维护。</p>
            </div>
            <el-button type="primary" @click="saveResume">保存简历</el-button>
          </div>
          <ResumeEditor :resume="activeResume" :skill-candidates="skillCandidates" />
        </section>
        <el-empty v-else description="请先创建简历版本" :image-size="96" />
      </div>

      <aside class="preview-column panel">
        <div class="preview-head">
          <h3>在线预览</h3>
          <el-radio-group v-model="previewMode" size="small">
            <el-radio-button label="structured">结构化简历</el-radio-button>
            <el-radio-button label="pdf" :disabled="!currentPdfUrl">PDF 附件</el-radio-button>
          </el-radio-group>
        </div>

        <ResumePreview v-if="activeResume && previewMode === 'structured'" :resume="activeResume" />
        <CanvasPdf v-else-if="previewMode === 'pdf'" :url="currentPdfUrl" />
        <el-empty v-else description="暂无可预览内容" :image-size="96" />
      </aside>
    </section>
  </div>
</template>

<script setup>
import { computed, onMounted, ref, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useStore } from 'vuex'
import CanvasPdf from '@/components/pdf/CanvasPdf.vue'
import ResumeEditor from '@/components/resume/ResumeEditor.vue'
import ResumePreview from '@/components/resume/ResumePreview.vue'
import ResumeTemplateSelector from '@/components/resume/ResumeTemplateSelector.vue'
import ResumeUploadPanel from '@/components/resume/ResumeUploadPanel.vue'
import { resumeTemplates } from '@/constants/resume-template'
import { getPdfFileName } from '@/utils/resume'

const store = useStore()

const activeResumeId = ref(null)
const previewMode = ref('structured')
const templates = resumeTemplates
const defaultResumeType = '通用版'

const resumes = computed(() => store.state.resume.list || [])
const activeResume = computed(() => store.state.resume.currentResume)
const currentPdfUrl = computed(() => store.state.resume.pdfUrl || activeResume.value?.pdfUrl || '')
const skillCandidates = computed(() => store.state.resume.skillCandidates || [])
const pdfFileName = computed(() => (activeResume.value ? getPdfFileName(activeResume.value) : ''))

watch(
  activeResume,
  (value) => {
    activeResumeId.value = value?.id || null
    if (!value?.pdfUrl) {
      previewMode.value = 'structured'
    }
  },
  { immediate: true }
)

const refreshList = async (preferredId) => {
  await store.dispatch('resume/fetchMyList', preferredId)
  if (store.state.resume.currentResume?.id) {
    activeResumeId.value = store.state.resume.currentResume.id
  }
}

const selectResume = async (resumeId) => {
  activeResumeId.value = resumeId
  const target = resumes.value.find((item) => item.id === resumeId)
  store.commit('resume/setCurrentResume', target)
  if (target?.pdfUrl) {
    await store.dispatch('resume/fetchPdf', resumeId)
  }
}

const createVersion = async () => {
  const created = await store.dispatch('resume/createVersion', {
    resumeName: `新简历版本 ${resumes.value.length + 1}`
  })
  activeResumeId.value = created.id
  previewMode.value = created.pdfUrl ? 'pdf' : 'structured'
  ElMessage.success('已创建新的简历版本')
}

const setDefault = async () => {
  if (!activeResume.value) return
  await store.dispatch('resume/setDefault', activeResume.value.id)
  ElMessage.success('默认简历已更新')
}

const removeVersion = async () => {
  if (!activeResume.value || activeResume.value.isDefault) return
  await ElMessageBox.confirm(`确认删除“${activeResume.value.resumeName}”吗？`, '删除简历版本', { type: 'warning' })
  await store.dispatch('resume/deleteVersion', activeResume.value.id)
  ElMessage.success('简历版本已删除')
}

const saveResume = async () => {
  if (!activeResume.value) return
  const saved = await store.dispatch('resume/save', activeResume.value)
  activeResumeId.value = saved.id
  ElMessage.success('简历已保存')
}

const uploadPdf = async (file) => {
  if (!activeResume.value || !file) return
  const result = await store.dispatch('resume/uploadPdf', {
    resumeId: activeResume.value.id,
    file
  })
  previewMode.value = result?.pdfUrl ? 'pdf' : 'structured'
  ElMessage.success('PDF 简历已上传')
}

onMounted(async () => {
  await refreshList()
  if (activeResume.value?.pdfUrl) {
    await store.dispatch('resume/fetchPdf', activeResume.value.id)
  }
})
</script>

<style scoped>
.resume-center {
  display: grid;
  gap: 24px;
}

.hero,
.panel {
  padding: 28px;
  border-radius: 28px;
  background: rgba(255, 252, 247, 0.95);
  box-shadow: 0 14px 30px rgba(151, 110, 54, 0.12);
}

.layout {
  display: grid;
  grid-template-columns: 320px minmax(0, 1.1fr) minmax(320px, 0.9fr);
  gap: 24px;
}

.sidebar,
.editor-column,
.preview-column {
  min-width: 0;
}

.sidebar-head,
.section-head,
.sidebar-actions,
.preview-head {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 12px;
}

.version-list {
  display: grid;
  gap: 12px;
  margin: 20px 0;
}

.version-item {
  display: flex;
  justify-content: space-between;
  gap: 12px;
  padding: 16px;
  border-radius: 20px;
  border: 1px solid rgba(223, 205, 182, 0.8);
  background: #fff;
  cursor: pointer;
}

.version-item.active {
  border-color: rgba(47, 104, 216, 0.4);
}

.version-item p {
  margin: 8px 0 0;
  color: #866846;
}

.preview-column {
  display: grid;
  gap: 16px;
  min-height: 360px;
}

@media (max-width: 1360px) {
  .layout {
    grid-template-columns: 1fr;
  }
}
</style>
