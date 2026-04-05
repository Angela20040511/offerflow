export default {
  namespaced: true,
  state: () => ({
    activePath: '',
    openedTabs: [],
    keepAliveNames: []
  }),
  mutations: {
    addTab(state, route) {
      if (!route.meta?.title || route.meta?.hiddenTab) return
      const exists = state.openedTabs.find((item) => item.path === route.fullPath)
      if (!exists) {
        state.openedTabs.push({ title: route.meta.title, path: route.fullPath, name: route.name })
      }
      state.activePath = route.fullPath
      if (route.meta?.keepAlive && route.name && !state.keepAliveNames.includes(route.name)) {
        state.keepAliveNames.push(route.name)
      }
    },
    setActive(state, path) {
      state.activePath = path
    },
    removeTab(state, path) {
      const removed = state.openedTabs.find((item) => item.path === path)
      state.openedTabs = state.openedTabs.filter((item) => item.path !== path)
      if (removed?.name) {
        const stillOpened = state.openedTabs.some((item) => item.name === removed.name)
        if (!stillOpened) {
          state.keepAliveNames = state.keepAliveNames.filter((name) => name !== removed.name)
        }
      }
      if (state.activePath === path) {
        state.activePath = state.openedTabs[state.openedTabs.length - 1]?.path || ''
      }
    },
    resetTabs(state) {
      state.activePath = ''
      state.openedTabs = []
      state.keepAliveNames = []
    }
  }
}
