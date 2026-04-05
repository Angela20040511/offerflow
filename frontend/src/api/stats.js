import request from './request'

export const getApplicantOverviewApi = () => request.get('/dashboard/applicant/overview')
export const getHrDashboardApi = () => request.get('/hr/dashboard/overview')
export const getHrStatsOverviewApi = () => request.get('/hr/statistics/overview')
export const getHrFunnelApi = () => request.get('/hr/statistics/funnel')
export const getHrDistributionApi = () => request.get('/hr/statistics/job-distribution')
export const getHrStagePieApi = () => request.get('/hr/statistics/stage-pie')
export const getHrSubsidiaryDistributionApi = () => request.get('/hr/statistics/subsidiary-distribution')
