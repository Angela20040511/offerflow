import request from './request'
export const getJobsApi = (params) => request.get('/jobs', { params })
export const getJobDetailApi = (id) => request.get(`/jobs/${id}`)
export const getHrJobsApi = (params) => request.get('/hr/jobs', { params })
export const createJobApi = (data) => request.post('/hr/jobs', data)
export const updateJobApi = (id, data) => request.put(`/hr/jobs/${id}`, data)
export const deleteJobApi = (id) => request.delete(`/hr/jobs/${id}`)
export const updateJobStatusApi = (id, data) => request.put(`/hr/jobs/${id}/status`, data)
