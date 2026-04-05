import request from './request'
export const applyJobApi = (data) => request.post('/applications', data)
export const getMyApplicationsApi = (params) => request.get('/applications/me', { params })
export const withdrawApplicationApi = (id) => request.put(`/applications/${id}/withdraw`)
