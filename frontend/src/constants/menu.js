import { ROLE } from '@/constants/role'

export const applicantMenu = [
  { key: '/applicant/dashboard', label: '\u9996\u9875' },
  { key: '/applicant/jobs', label: '\u5c97\u4f4d\u4e2d\u5fc3' },
  { key: '/applicant/resume', label: '\u7b80\u5386\u4e2d\u5fc3' },
  { key: '/applicant/applications', label: '\u6211\u7684\u6295\u9012' },
  { key: '/applicant/favorites', label: '\u6211\u7684\u6536\u85cf' },
  { key: '/applicant/guide', label: '\u5e94\u8058\u6307\u5357' }
]

export const hrMenu = [
  { key: '/hr/dashboard', label: '\u62db\u8058\u6982\u89c8' },
  { key: '/hr/jobs', label: '\u5c97\u4f4d\u7ba1\u7406' },
  { key: '/hr/candidates', label: '\u5019\u9009\u4eba\u7ba1\u7406' },
  { key: '/hr/applications', label: '\u6295\u9012\u7ba1\u7406' },
  { key: '/hr/statistics', label: '\u7edf\u8ba1\u5206\u6790' }
]

export const menuByRole = {
  [ROLE.APPLICANT]: applicantMenu,
  [ROLE.HR]: hrMenu
}

export const routeTitleMap = {
  '/login': '\u767b\u5f55',
  '/register': '\u6ce8\u518c',
  '/applicant/dashboard': '\u9996\u9875',
  '/applicant/jobs': '\u5c97\u4f4d\u4e2d\u5fc3',
  '/applicant/job': '\u5c97\u4f4d\u8be6\u60c5',
  '/applicant/resume': '\u7b80\u5386\u4e2d\u5fc3',
  '/applicant/applications': '\u6211\u7684\u6295\u9012',
  '/applicant/favorites': '\u6211\u7684\u6536\u85cf',
  '/applicant/guide': '\u5e94\u8058\u6307\u5357',
  '/hr/dashboard': '\u62db\u8058\u6982\u89c8',
  '/hr/jobs': '\u5c97\u4f4d\u7ba1\u7406',
  '/hr/candidates': '\u5019\u9009\u4eba\u7ba1\u7406',
  '/hr/applications': '\u6295\u9012\u7ba1\u7406',
  '/hr/statistics': '\u7edf\u8ba1\u5206\u6790',
  '/hr/review': '\u7b80\u5386\u8bc4\u5ba1'
}
