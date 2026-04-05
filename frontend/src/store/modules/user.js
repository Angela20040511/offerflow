import { profileApi } from '@/api/auth'
import { getApplicantOverviewApi, getHrDashboardApi } from '@/api/stats'
import { applyJobApi, getMyApplicationsApi, withdrawApplicationApi } from '@/api/application'
import { addFavoriteApi, getMyFavoritesApi, removeFavoriteApi } from '@/api/favorite'
import { getCandidatesApi, getHrApplicationsApi } from '@/api/candidate'
import { loginApi } from '@/api/auth'
import { clearToken, clearUserCache, getToken, getUserCache, setToken, setUserCache } from '@/utils/auth'

const emptyUserState = () => ({
  token: getToken(),
  profile: getUserCache() || null,
  userId: getUserCache()?.userId || null,
  username: getUserCache()?.username || '',
  displayName: getUserCache()?.displayName || '',
  role: getUserCache()?.role || '',
  avatar: getUserCache()?.avatar || '',
  applicantOverview: {},
  hrOverview: {},
  myApplications: [],
  favorites: [],
  hrApplications: [],
  hrCandidates: []
})

const unwrapPageList = (payload) => payload?.list || []

export default {
  namespaced: true,
  state: emptyUserState,
  getters: {
    isLogin: (state) => Boolean(state.token),
    role: (state) => state.role,
    token: (state) => state.token,
    username: (state) => state.username,
    displayName: (state) => state.displayName || state.username
  },
  mutations: {
    setToken(state, token) {
      state.token = token
      setToken(token)
    },
    setUserInfo(state, payload = {}) {
      state.userId = payload.userId || payload.id || null
      state.username = payload.username || ''
      state.displayName = payload.displayName || payload.username || ''
      state.role = payload.role || ''
      state.avatar = payload.avatar || ''
      state.profile = {
        userId: state.userId,
        id: state.userId,
        username: state.username,
        displayName: state.displayName,
        role: state.role,
        avatar: state.avatar,
        email: payload.email || '',
        phone: payload.phone || ''
      }
      setUserCache(state.profile)
    },
    clearUser(state) {
      Object.assign(state, emptyUserState(), {
        token: '',
        profile: null,
        userId: null,
        username: '',
        displayName: '',
        role: '',
        avatar: ''
      })
      clearToken()
      clearUserCache()
    },
    setApplicantOverview(state, payload) {
      state.applicantOverview = payload || {}
    },
    setHrOverview(state, payload) {
      state.hrOverview = payload || {}
    },
    setMyApplications(state, payload) {
      state.myApplications = payload || []
    },
    setFavorites(state, payload) {
      state.favorites = payload || []
    },
    setHrApplications(state, payload) {
      state.hrApplications = payload || []
    },
    setHrCandidates(state, payload) {
      state.hrCandidates = payload || []
    }
  },
  actions: {
    async login({ commit }, payload) {
      const data = await loginApi(payload)
      commit('setToken', data.token)
      commit('setUserInfo', data)
      return data
    },
    async getProfile({ commit }) {
      const data = await profileApi()
      commit('setUserInfo', data)
      return data
    },
    async fetchProfile({ dispatch }) {
      return dispatch('getProfile')
    },
    logout({ commit }) {
      commit('clearUser')
      commit('tabs/resetTabs', null, { root: true })
    },
    async fetchApplicantOverview({ commit }) {
      const overview = await getApplicantOverviewApi()
      commit('setApplicantOverview', {
        ...overview,
        resumeCompleteScore: overview?.resumeCompleteScore ?? overview?.resumeCompleteness ?? 0,
        recentApplications: overview?.recentApplications || overview?.latestApplications || [],
        recommendJobs: overview?.recommendJobs || overview?.recommendedJobs || []
      })
    },
    async fetchHrOverview({ commit }) {
      const overview = await getHrDashboardApi()
      commit('setHrOverview', {
        ...overview,
        latestApplications: overview?.latestApplications || overview?.recentApplications || [],
        subsidiaryJobDistribution: overview?.subsidiaryJobDistribution || overview?.jobDistribution || [],
        subsidiaryApplicationDistribution: overview?.subsidiaryApplicationDistribution || overview?.subsidiaryDistribution || []
      })
    },
    async fetchMyApplications({ commit }, params = {}) {
      const page = await getMyApplicationsApi(params)
      commit('setMyApplications', unwrapPageList(page))
      return page
    },
    async withdrawApplication({ dispatch }, id) {
      const result = await withdrawApplicationApi(id)
      await dispatch('fetchMyApplications')
      return result
    },
    async fetchFavorites({ commit }, params = {}) {
      const page = await getMyFavoritesApi(params)
      commit('setFavorites', unwrapPageList(page).map((item) => ({
        ...item,
        id: item.jobId || item.id,
        isFavorite: true
      })))
      return page
    },
    async toggleFavorite({ dispatch, state }, payload) {
      const jobId = typeof payload === 'object' ? payload?.id ?? payload?.jobId : payload
      const knownFavoriteState = typeof payload === 'object' && typeof payload?.isFavorite === 'boolean' ? payload.isFavorite : null
      const exists =
        knownFavoriteState ??
        state.favorites.some((item) => Number(item.id) === Number(jobId) || Number(item.jobId) === Number(jobId))
      if (exists) {
        await removeFavoriteApi(jobId)
      } else {
        await addFavoriteApi({ jobId })
      }
      await dispatch('fetchFavorites')
      return !exists
    },
    async applyJob({ dispatch }, payload) {
      const result = await applyJobApi(payload)
      return result
    },
    async fetchHrApplications({ commit }, params = {}) {
      const page = await getHrApplicationsApi(params)
      commit('setHrApplications', unwrapPageList(page))
      return page
    },
    async fetchHrCandidates({ commit }, params = {}) {
      const page = await getCandidatesApi(params)
      commit('setHrCandidates', unwrapPageList(page))
      return page
    }
  }
}
