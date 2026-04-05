import {
  createResumeVersionApi,
  deleteResumeApi,
  getMyResumeListApi,
  getResumePdfApi,
  saveResumeApi,
  setDefaultResumeApi,
  uploadResumeApi
} from '@/api/resume'
import { normalizeResume, toResumeSavePayload } from '@/utils/resume'

const pickCurrentResume = (list, preferredId) => {
  if (!list.length) {
    return null
  }
  return list.find((item) => Number(item.id) === Number(preferredId)) || list.find((item) => item.isDefault) || list[0]
}

export default {
  namespaced: true,
  state: () => ({
    list: [],
    currentResume: null,
    currentTemplate: 'classic',
    pdfUrl: '',
    lastSavedAt: '',
    skillCandidates: ['Vue 3', 'JavaScript', 'TypeScript', 'Spring Boot', 'MySQL', 'SQL', '数据分析', '产品运营']
  }),
  mutations: {
    setList(state, payload) {
      state.list = payload || []
      const current = pickCurrentResume(state.list, state.currentResume?.id)
      state.currentResume = current
      state.currentTemplate = current?.templateCode || 'classic'
      state.pdfUrl = current?.pdfUrl || ''
    },
    setCurrentResume(state, payload) {
      state.currentResume = payload ? normalizeResume(payload) : null
      state.currentTemplate = state.currentResume?.templateCode || 'classic'
      state.pdfUrl = state.currentResume?.pdfUrl || ''
    },
    updateCurrentResume(state, payload = {}) {
      if (!state.currentResume) {
        state.currentResume = normalizeResume(payload)
      } else {
        state.currentResume = normalizeResume({
          ...state.currentResume,
          ...payload
        })
      }
      state.currentTemplate = state.currentResume?.templateCode || 'classic'
      state.pdfUrl = state.currentResume?.pdfUrl || ''
      state.list = state.list.map((item) => (item.id === state.currentResume.id ? state.currentResume : item))
    },
    setPdfUrl(state, url) {
      state.pdfUrl = url || ''
      if (state.currentResume) {
        state.currentResume = normalizeResume({
          ...state.currentResume,
          pdfUrl: url || ''
        })
        state.list = state.list.map((item) => (item.id === state.currentResume.id ? state.currentResume : item))
      }
    },
    setLastSaved(state, time) {
      state.lastSavedAt = time || ''
    },
    setSkillCandidates(state, list) {
      state.skillCandidates = list?.length ? list : state.skillCandidates
    }
  },
  actions: {
    async fetchMyList({ commit, state }, preferredId) {
      const data = await getMyResumeListApi()
      const list = (data || []).map(normalizeResume)
      commit('setList', list)
      const current = pickCurrentResume(list, preferredId || state.currentResume?.id)
      commit('setCurrentResume', current)
      return list
    },
    async createVersion({ dispatch }, payload) {
      const created = normalizeResume(await createResumeVersionApi(payload))
      await dispatch('fetchMyList', created.id)
      return created
    },
    async setDefault({ dispatch }, id) {
      const result = await setDefaultResumeApi(id)
      await dispatch('fetchMyList', id)
      return result
    },
    async deleteVersion({ dispatch, state }, id) {
      const result = await deleteResumeApi(id)
      const nextResumeId = state.list.find((item) => item.id !== id)?.id
      await dispatch('fetchMyList', nextResumeId)
      return result
    },
    async save({ commit, dispatch, state }, resume) {
      const saved = normalizeResume(await saveResumeApi(toResumeSavePayload(resume)))
      await dispatch('fetchMyList', saved.id)
      commit('setCurrentResume', saved)
      commit('setLastSaved', saved.updateTime || new Date().toISOString())
      return saved
    },
    async uploadPdf({ commit, dispatch }, { resumeId, file }) {
      const formData = new FormData()
      formData.append('resumeId', resumeId)
      formData.append('file', file)
      const result = await uploadResumeApi(formData)
      commit('setPdfUrl', result?.pdfUrl || '')
      await dispatch('fetchMyList', resumeId)
      return result
    },
    async fetchPdf({ commit }, resumeId) {
      const result = await getResumePdfApi(resumeId)
      commit('setPdfUrl', result?.pdfUrl || '')
      return result
    }
  }
}
