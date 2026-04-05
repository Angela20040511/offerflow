export default {
  namespaced: true,
  state: () => ({ currentDoc: null, currentPage: 1, scale: 1.2 }),
  mutations: {
    setCurrentDoc(state, doc) { state.currentDoc = doc },
    setCurrentPage(state, page) { state.currentPage = page },
    setScale(state, scale) { state.scale = scale }
  }
}
