import { createRouter, createWebHistory } from 'vue-router'
import { ElMessage } from 'element-plus'
import HomeFrame from '@/components/layout/HomeFrame.vue'
import store from '@/store'
import { ROLE } from '@/constants/role'
import { getToken, clearToken } from '@/utils/auth'

const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', name: 'LoginView', component: () => import('@/views/login/LoginView.vue'), meta: { title: '\u767b\u5f55', hiddenTab: true } },
  { path: '/register', name: 'RegisterView', component: () => import('@/views/login/RegisterView.vue'), meta: { title: '\u6ce8\u518c', hiddenTab: true } },
  {
    path: '/',
    component: HomeFrame,
    meta: { requiresAuth: true, title: '\u9996\u9875', hiddenTab: true },
    children: [
      { path: 'applicant/dashboard', name: 'ApplicantDashboard', component: () => import('@/views/dashboard/ApplicantDashboard.vue'), meta: { requiresAuth: true, role: ROLE.APPLICANT, title: '\u9996\u9875', keepAlive: true } },
      { path: 'applicant/jobs', name: 'JobCenterView', component: () => import('@/views/job/JobCenterView.vue'), meta: { requiresAuth: true, role: ROLE.APPLICANT, title: '\u5c97\u4f4d\u4e2d\u5fc3', keepAlive: true } },
      { path: 'applicant/jobs/:id', name: 'JobDetailView', component: () => import('@/views/job/JobDetailView.vue'), meta: { requiresAuth: true, role: ROLE.APPLICANT, title: '\u5c97\u4f4d\u8be6\u60c5', keepAlive: true } },
      { path: 'applicant/resume', name: 'ResumeCenterView', component: () => import('@/views/resume/ResumeCenterView.vue'), meta: { requiresAuth: true, role: ROLE.APPLICANT, title: '\u7b80\u5386\u4e2d\u5fc3', keepAlive: true } },
      { path: 'applicant/applications', name: 'MyApplicationsView', component: () => import('@/views/application/MyApplicationsView.vue'), meta: { requiresAuth: true, role: ROLE.APPLICANT, title: '\u6211\u7684\u6295\u9012', keepAlive: true } },
      { path: 'applicant/favorites', name: 'MyFavoritesView', component: () => import('@/views/application/MyFavoritesView.vue'), meta: { requiresAuth: true, role: ROLE.APPLICANT, title: '\u6211\u7684\u6536\u85cf', keepAlive: true } },
      { path: 'applicant/guide', name: 'GuideCenterView', component: () => import('@/views/guide/GuideCenterView.vue'), meta: { requiresAuth: true, role: ROLE.APPLICANT, title: '\u5e94\u8058\u6307\u5357', keepAlive: true } },
      { path: 'hr/dashboard', name: 'HrDashboard', component: () => import('@/views/dashboard/HrDashboard.vue'), meta: { requiresAuth: true, role: ROLE.HR, title: '\u62db\u8058\u6982\u89c8', keepAlive: true } },
      { path: 'hr/jobs', name: 'HrJobManageView', component: () => import('@/views/hr/HrJobManageView.vue'), meta: { requiresAuth: true, role: ROLE.HR, title: '\u5c97\u4f4d\u7ba1\u7406', keepAlive: true } },
      { path: 'hr/candidates', name: 'HrCandidateManageView', component: () => import('@/views/hr/HrCandidateManageView.vue'), meta: { requiresAuth: true, role: ROLE.HR, title: '\u5019\u9009\u4eba\u7ba1\u7406', keepAlive: true } },
      { path: 'hr/applications', name: 'HrApplicationManageView', component: () => import('@/views/hr/HrApplicationManageView.vue'), meta: { requiresAuth: true, role: ROLE.HR, title: '\u6295\u9012\u7ba1\u7406', keepAlive: true } },
      { path: 'hr/review/:id', name: 'HrResumeReviewView', component: () => import('@/views/hr/HrResumeReviewView.vue'), meta: { requiresAuth: true, role: ROLE.HR, title: '\u7b80\u5386\u8bc4\u5ba1', keepAlive: true } },
      { path: 'hr/statistics', name: 'HrStatisticsView', component: () => import('@/views/hr/HrStatisticsView.vue'), meta: { requiresAuth: true, role: ROLE.HR, title: '\u7edf\u8ba1\u5206\u6790', keepAlive: true } }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach(async (to, from, next) => {
  const token = getToken()
  const profile = store.state.user.profile

  if (to.meta.requiresAuth && !token) {
    next('/login')
    return
  }

  if (token && !profile) {
    try {
      await store.dispatch('user/fetchProfile')
    } catch (error) {
      clearToken()
      next('/login')
      return
    }
  }

  const currentRole = store.state.user.profile?.role
  if (to.meta.role && currentRole && to.meta.role !== currentRole) {
    ElMessage.warning('当前账号无权访问该页面')
    next(currentRole === ROLE.HR ? '/hr/dashboard' : '/applicant/dashboard')
    return
  }

  document.title = `OfferFlow - ${to.meta.title || ''}`
  next()
})

router.afterEach((to) => {
  store.commit('tabs/addTab', to)
  store.commit('tabs/setActive', to.fullPath)
})

export default router
