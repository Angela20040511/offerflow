import { getCitiesApi, getJobCategoriesApi, getProvincesApi, getSubsidiariesApi } from '@/api/common'

export default {
  namespaced: true,
  state: () => ({
    subsidiaries: [],
    categories: [],
    provinces: [],
    citiesMap: {}
  }),
  getters: {
    subsidiaries: (state) => state.subsidiaries,
    categories: (state) => state.categories,
    provinces: (state) => state.provinces,
    citiesMap: (state) => state.citiesMap,
    cityOptions: (state) => (provinceCode) => state.citiesMap[provinceCode] || []
  },
  mutations: {
    setSubsidiaries(state, list) {
      state.subsidiaries = list || []
    },
    setCategories(state, list) {
      state.categories = list || []
    },
    setProvinces(state, list) {
      state.provinces = list || []
    },
    setCities(state, { provinceCode, list }) {
      state.citiesMap = {
        ...state.citiesMap,
        [provinceCode]: list || []
      }
    },
    clearCities(state, provinceCode) {
      if (!provinceCode) {
        state.citiesMap = {}
        return
      }
      const nextMap = { ...state.citiesMap }
      delete nextMap[provinceCode]
      state.citiesMap = nextMap
    }
  },
  actions: {
    async ensureBaseData({ state, dispatch }) {
      const tasks = []
      if (!state.subsidiaries.length) tasks.push(dispatch('fetchSubsidiaries'))
      if (!state.categories.length) tasks.push(dispatch('fetchCategories'))
      if (!state.provinces.length) tasks.push(dispatch('fetchProvinces'))
      await Promise.all(tasks)
      return {
        subsidiaries: state.subsidiaries,
        categories: state.categories,
        provinces: state.provinces,
        citiesMap: state.citiesMap
      }
    },
    async fetchBaseOptions({ dispatch }) {
      return dispatch('ensureBaseData')
    },
    async fetchSubsidiaries({ commit }) {
      const data = await getSubsidiariesApi()
      commit('setSubsidiaries', data)
      return data
    },
    async fetchCategories({ commit }) {
      const data = await getJobCategoriesApi()
      commit('setCategories', data)
      return data
    },
    async fetchProvinces({ commit }) {
      const data = await getProvincesApi()
      commit('setProvinces', data)
      return data
    },
    async fetchCities({ commit, state }, provinceCode) {
      if (!provinceCode) {
        return []
      }
      if (state.citiesMap[provinceCode]?.length) {
        return state.citiesMap[provinceCode]
      }
      const data = await getCitiesApi(provinceCode)
      commit('setCities', { provinceCode, list: data })
      return data
    }
  }
}
