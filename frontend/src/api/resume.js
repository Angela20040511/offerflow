import request from './request'

export const getMyResumeApi = () => request.get('/resumes/me')
export const getMyResumeListApi = () => request.get('/resumes/my-list')
export const saveResumeApi = (data) => request.post('/resumes/save', data)
export const createResumeVersionApi = (data) => request.post('/resumes/create-version', data)
export const setDefaultResumeApi = (id) => request.put(`/resumes/${id}/default`)
export const deleteResumeApi = (id) => request.delete(`/resumes/${id}`)
export const uploadResumeApi = (formData) => request.post('/resumes/upload', formData)
export const getResumePdfApi = (id) => request.get(`/resumes/${id}/pdf`)
export const getResumeScoreApi = (id) => request.get(`/resumes/${id}/complete-score`)
