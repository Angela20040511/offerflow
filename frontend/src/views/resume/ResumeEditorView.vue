<template>
  <div class="resume-editor-page">
    <section class="panel top-panel">
      <div class="panel-head">
        <div>
          <h3>简历版本与模板</h3>
          <p>先选择简历版本，再维护结构化内容并实时预览。</p>
        </div>
        <div class="head-actions">
          <el-button type="primary" plain @click="openCreateDialog">新增版本</el-button>
          <el-button :disabled="!activeResume" @click="openRenameDialog">重命名版本</el-button>
          <el-button :disabled="!activeResume" @click="setDefault">设为默认</el-button>
          <el-button type="danger" plain :disabled="!activeResume || activeResume.isDefault" @click="removeVersion">删除版本</el-button>
        </div>
      </div>

      <div class="top-grid">
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

        <ResumeTemplateSelector v-if="activeResume" v-model="activeResume.templateCode" :templates="resumeTemplates" />
        <el-empty v-else description="请选择简历版本" :image-size="80" />
      </div>
    </section>

    <section class="bottom-grid">
      <section class="panel">
        <div class="panel-head">
          <div>
            <h3>{{ activeResume?.resumeName || '结构化编辑' }}</h3>
            <p>在这里维护基本信息、教育经历、工作经历、项目经历和技能。</p>
          </div>
          <el-button type="primary" :disabled="!activeResume" @click="saveResume">保存简历</el-button>
        </div>
        <ResumeEditor v-if="activeResume" :resume="activeResume" :skill-candidates="skillCandidates" />
        <el-empty v-else description="请先创建或选择简历版本" :image-size="88" />
      </section>

      <aside class="panel preview-panel">
        <div class="panel-head">
          <div>
            <h3>{{ activeResume?.resumeName || '结构化预览' }}</h3>
            <p>右侧预览会实时反映当前版本内容。</p>
          </div>
        </div>
        <ResumePreview v-if="activeResume" :resume="activeResume" />
        <el-empty v-else description="暂无可预览内容" :image-size="88" />
      </aside>
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
import ResumeEditor from '@/components/resume/ResumeEditor.vue'
import ResumePreview from '@/components/resume/ResumePreview.vue'
import ResumeTemplateSelector from '@/components/resume/ResumeTemplateSelector.vue'
import { useResumeWorkspace } from '@/composables/useResumeWorkspace'
import { resumeTemplates } from '@/constants/resume-template'

const {
  resumes,
  activeResume,
  skillCandidates,
  ensureLoaded,
  selectResume,
  createVersion,
  renameVersion,
  setDefault,
  removeVersion,
  saveResume
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
.resume-editor-page,
.bottom-grid {
  display: grid;
  gap: 24px;
}

.panel {
  padding: 28px;
  border-radius: 28px;
  background: var(--panel);
  border: 1px solid var(--line);
  box-shadow: var(--shadow);
}

.panel-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
}

.head-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

.top-grid {
  display: grid;
  grid-template-columns: minmax(280px, 0.9fr) minmax(0, 1fr);
  gap: 20px;
  margin-top: 20px;
}

.bottom-grid {
  grid-template-columns: minmax(0, 1.15fr) minmax(340px, 0.85fr);
  align-items: start;
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

.preview-panel {
  min-width: 0;
}

@media (max-width: 1280px) {
  .top-grid,
  .bottom-grid {
    grid-template-columns: 1fr;
  }
}
</style>
