import request from './request'

export const getSubsidiariesApi = () => request.get('/common/subsidiaries')
export const getJobCategoriesApi = () => request.get('/common/job-categories')
export const getProvincesApi = () => request.get('/common/provinces')
export const getCitiesApi = (provinceCode) => request.get('/common/cities', { params: { provinceCode } })
