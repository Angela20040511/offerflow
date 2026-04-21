import { computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useStore } from 'vuex'
import { getPdfFileName } from '@/utils/resume'

const withCacheBust = (url) => (url ? `${url}${url.includes('?') ? '&' : '?'}t=${Date.now()}` : '')

export function useResumeWorkspace() {
  const store = useStore()

  const resumes = computed(() => store.state.resume.list || [])
  const activeResume = computed(() => store.state.resume.currentResume)
  const currentPdfUrl = computed(() => withCacheBust(store.state.resume.pdfUrl || activeResume.value?.pdfUrl || ''))
  const skillCandidates = computed(() => store.state.resume.skillCandidates || [])
  const pdfFileName = computed(() => (activeResume.value ? getPdfFileName(activeResume.value) : ''))

  const refreshList = async (preferredId) => {
    await store.dispatch('resume/fetchMyList', preferredId)
    if (store.state.resume.currentResume?.id && store.state.resume.currentResume?.pdfUrl) {
      await store.dispatch('resume/fetchPdf', store.state.resume.currentResume.id)
    }
  }

  const ensureLoaded = async () => {
    await refreshList(activeResume.value?.id)
  }

  const selectResume = async (resumeId) => {
    const target = resumes.value.find((item) => Number(item.id) === Number(resumeId))
    store.commit('resume/setCurrentResume', target)
    if (target?.pdfUrl) {
      await store.dispatch('resume/fetchPdf', resumeId)
    }
  }

  const createVersion = async (resumeName) => {
    const created = await store.dispatch('resume/createVersion', { resumeName })
    await selectResume(created.id)
    ElMessage.success('已创建新的简历版本')
    return created
  }

  const renameVersion = async (resumeName) => {
    if (!activeResume.value) return null
    const saved = await store.dispatch('resume/save', {
      ...activeResume.value,
      title: resumeName,
      resumeName
    })
    await selectResume(saved.id)
    ElMessage.success('简历版本名称已更新')
    return saved
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
    await selectResume(saved.id)
    ElMessage.success('简历已保存')
  }

  const uploadPdf = async (file) => {
    if (!activeResume.value || !file) return
    const result = await store.dispatch('resume/uploadPdf', {
      resumeId: activeResume.value.id,
      file
    })
    await selectResume(activeResume.value.id)
    ElMessage.success('PDF 简历已上传')
    return result
  }

  return {
    resumes,
    activeResume,
    currentPdfUrl,
    skillCandidates,
    pdfFileName,
    ensureLoaded,
    refreshList,
    selectResume,
    createVersion,
    renameVersion,
    setDefault,
    removeVersion,
    saveResume,
    uploadPdf
  }
}
