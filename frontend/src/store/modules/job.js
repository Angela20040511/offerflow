import { getJobDetailApi, getJobsApi } from '@/api/job'

const defaultFilters = () => ({
  subsidiaryId: '',
  keyword: '',
  categoryId: '',
  provinceCode: '',
  cityCode: '',
  salaryRange: '',
  salaryMin: null,
  salaryMax: null,
  durationType: '',
  workMode: '',
  pageNum: 1,
  pageSize: 6
})

const parseSalaryRange = (salaryRange) => {
  if (!salaryRange) {
    return { salaryMin: null, salaryMax: null }
  }
  if (salaryRange.endsWith('+')) {
    return {
      salaryMin: Number(salaryRange.replace('+', '')) || null,
      salaryMax: null
    }
  }
  const [min, max] = salaryRange.split('-').map((item) => Number(item))
  return {
    salaryMin: Number.isFinite(min) ? min : null,
    salaryMax: Number.isFinite(max) ? max : null
  }
}

export default {
  namespaced: true,
  state: () => ({
    filters: defaultFilters(),
    list: [],
    detail: null,
    total: 0,
    loading: false
  }),
  mutations: {
    setFilters(state, payload) {
      state.filters = {
        ...state.filters,
        ...payload
      }
    },
    resetFilters(state) {
      state.filters = defaultFilters()
    },
    setList(state, list) {
      state.list = list || []
    },
    setDetail(state, detail) {
      state.detail = detail || null
    },
    setLoading(state, loading) {
      state.loading = Boolean(loading)
    },
    setPage(state, payload = {}) {
      state.total = Number(payload.total || 0)
      state.filters = {
        ...state.filters,
        pageNum: payload.pageNum || state.filters.pageNum,
        pageSize: payload.pageSize || state.filters.pageSize
      }
    }
  },
  actions: {
    async fetchList({ commit, state }, payload = {}) {
      const nextFilters = {
        ...state.filters,
        ...payload
      }
      const salaryParams = parseSalaryRange(nextFilters.salaryRange)
      commit('setFilters', {
        ...nextFilters,
        ...salaryParams
      })
      commit('setLoading', true)
      try {
        const page = await getJobsApi({
          ...nextFilters,
          ...salaryParams
        })
        commit('setList', page?.list || [])
        commit('setPage', page)
        return page
      } finally {
        commit('setLoading', false)
      }
    },
    async fetchDetail({ commit }, id) {
      commit('setLoading', true)
      try {
        const detail = await getJobDetailApi(id)
        commit('setDetail', detail)
        return detail
      } finally {
        commit('setLoading', false)
      }
    }
  }
}
