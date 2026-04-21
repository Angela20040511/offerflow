<template>
  <div class="resume-pdf-page">
    <section class="panel pdf-grid">
      <aside class="left-panel">
        <div class="panel-head">
          <div>
            <h3>版本与上传</h3>
            <p>选择简历版本并上传对应 PDF，右侧会展示完整多页预览。</p>
          </div>
          <div class="head-actions">
            <el-button type="primary" plain @click="openCreateDialog">新增版本</el-button>
            <el-button :disabled="!activeResume" @click="openRenameDialog">重命名版本</el-button>
          </div>
        </div>

        <div class="version-list">
          <button
            v-for="item in resumes"
            :key="item.id"
            class="version-item"
            :class="{ active: activeResume?.id === item.id }"
            @click="selectResume(item.id)"
          >
            <div>
              <strong>{{ item.resumeName }}</strong>
              <p>{{ item.resumeType || '通用版' }}</p>
            </div>
            <el-tag v-if="item.isDefault" type="success" effect="light">默认</el-tag>
          </button>
          <el-empty v-if="!resumes.length" description="暂无简历版本" :image-size="80" />
        </div>

        <div class="action-row">
          <el-button :disabled="!activeResume" @click="setDefault">设为默认</el-button>
          <el-button type="danger" plain :disabled="!activeResume || activeResume.isDefault" @click="removeVersion">删除版本</el-button>
        </div>

        <ResumeUploadPanel v-if="activeResume" :file-name="pdfFileName" @select="uploadPdf" />
      </aside>

      <section class="right-panel">
        <div class="panel-head">
          <div>
            <h3>{{ activeResume?.resumeName || 'PDF 预览' }}</h3>
            <p>支持多页预览，上传或更换后会自动刷新。</p>
          </div>
        </div>
        <CanvasPdf :url="currentPdfUrl" empty-text="当前版本暂未上传 PDF 简历" />
      </section>
    </section>

    <el-dialog v-model="createDialogVisible" title="新增简历版本" width="420px">
      <el-input v-model="createName" maxlength="40" placeholder="请输入简历版本名称" />
      <template #footer>
        <el-button @click="createDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleCreateVersion">确认</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="renameDialogVisible" title="重命名简历版本" width="420px">
      <el-input v-model="renameName" maxlength="40" placeholder="请输入新的简历版本名称" />
      <template #footer>
        <el-button @click="renameDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleRenameVersion">确认</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import CanvasPdf from '@/components/pdf/CanvasPdf.vue'
import ResumeUploadPanel from '@/components/resume/ResumeUploadPanel.vue'
import { useResumeWorkspace } from '@/composables/useResumeWorkspace'

const {
  resumes,
  activeResume,
  currentPdfUrl,
  pdfFileName,
  ensureLoaded,
  selectResume,
  createVersion,
  renameVersion,
  setDefault,
  removeVersion,
  uploadPdf
} = useResumeWorkspace()

const createDialogVisible = ref(false)
const renameDialogVisible = ref(false)
const createName = ref('')
const renameName = ref('')

const openCreateDialog = () => {
  createName.value = ''
  createDialogVisible.value = true
}

const openRenameDialog = () => {
  if (!activeResume.value) return
  renameName.value = activeResume.value.resumeName || ''
  renameDialogVisible.value = true
}

const handleCreateVersion = async () => {
  const value = createName.value.trim()
  if (!value) {
    ElMessage.warning('版本名称不能为空')
    return
  }
  await createVersion(value)
  createDialogVisible.value = false
}

const handleRenameVersion = async () => {
  const value = renameName.value.trim()
  if (!value) {
    ElMessage.warning('版本名称不能为空')
    return
  }
  await renameVersion(value)
  renameDialogVisible.value = false
}

onMounted(ensureLoaded)
</script>

<style scoped>
.panel {
  padding: 28px;
  border-radius: 28px;
  background: var(--panel);
  border: 1px solid var(--line);
  box-shadow: var(--shadow);
}

.pdf-grid {
  display: grid;
  grid-template-columns: minmax(280px, 0.42fr) minmax(0, 0.98fr);
  gap: 24px;
  min-width: 0;
  align-items: start;
}

.left-panel,
.right-panel {
  display: grid;
  gap: 18px;
  min-width: 0;
  align-self: start;
}

.panel-head,
.action-row,
.head-actions {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
}

.action-row,
.head-actions {
  flex-wrap: wrap;
}

.version-list {
  display: grid;
  gap: 12px;
}

.version-item {
  display: flex;
  justify-content: space-between;
  gap: 12px;
  padding: 16px;
  border-radius: 20px;
  border: 1px solid rgba(116, 132, 255, 0.14);
  background: rgba(255, 255, 255, 0.88);
  cursor: pointer;
  text-align: left;
}

.version-item.active {
  border-color: rgba(85, 106, 255, 0.42);
  box-shadow: 0 10px 24px rgba(38, 71, 214, 0.16);
}

.version-item p {
  margin: 8px 0 0;
  color: var(--muted);
}

@media (max-width: 1200px) {
  .pdf-grid {
    grid-template-columns: 1fr;
  }
}
</style>
