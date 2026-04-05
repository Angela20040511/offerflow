import request from './request'
export const getGuidesApi = () => request.get('/guides')
export const getGuideDetailApi = (code) => request.get(`/guides/${code}`)
