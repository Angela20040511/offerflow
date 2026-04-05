import request from './request'

export const getCandidatesApi = (params) => request.get('/hr/candidates', { params })
export const getCandidateDetailApi = (id) => request.get(`/hr/candidates/${id}`)
export const updateCandidateStageApi = (data) => request.put('/hr/candidates/stage', data)
export const updateCandidateNoteApi = (data) => request.put('/hr/candidates/note', data)
export const updateCandidateReviewScoreApi = (data) => request.put('/hr/candidates/review-score', data)
export const getMatchScoreApi = (id) => request.get(`/hr/candidates/${id}/match-score`)
export const getHrApplicationsApi = (params) => request.get('/hr/applications', { params })
export const createInterviewApi = (data) => request.post('/hr/interviews', data)
export const getInterviewsApi = (applicationId) => request.get('/hr/interviews', { params: { applicationId } })
