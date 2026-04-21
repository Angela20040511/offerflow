import { createRouter, createWebHistory } from 'vue-router'
import { ElMessage } from 'element-plus'
import HomeFrame from '@/components/layout/HomeFrame.vue'
import store from '@/store'
import { ROLE } from '@/constants/role'
import { getToken } from '@/utils/auth'

const routes = [
  { path: '/', name: 'LandingView', component: () => import('@/views/public/LandingView.vue'), meta: { title: 'OfferFlow', hiddenTab: true } },
  { path: '/login', name: 'LoginView', component: () => import('@/views/login/LoginView.vue'), meta: { title: '登录', hiddenTab: true } },
  { path: '/register', name: 'RegisterView', component: () => import('@/views/login/RegisterView.vue'), meta: { title: '注册', hiddenTab: true } },
  {
    path: '/',
    component: HomeFrame,
    meta: { requiresAuth: true, title: '首页', hiddenTab: true },
    children: [
      { path: 'applicant/dashboard', name: 'ApplicantDashboard', component: () => import('@/views/dashboard/ApplicantDashboard.vue'), meta: { requiresAuth: true, role: ROLE.APPLICANT, title: '首页', keepAlive: true } },
      { path: 'applicant/jobs', name: 'JobCenterView', component: () => import('@/views/job/JobCenterView.vue'), meta: { requiresAuth: true, role: ROLE.APPLICANT, title: '岗位中心', keepAlive: true } },
      { path: 'applicant/jobs/:id', name: 'JobDetailView', component: () => import('@/views/job/JobDetailView.vue'), meta: { requiresAuth: true, role: ROLE.APPLICANT, title: '岗位详情', keepAlive: true } },
      {
        path: 'applicant/resume',
        name: 'ResumeCenterView',
        component: () => import('@/views/resume/ResumeCenterView.vue'),
        meta: { requiresAuth: true, role: ROLE.APPLICANT, title: '简历中心', keepAlive: true },
        children: [
          { path: '', redirect: '/applicant/resume/editor' },
          { path: 'editor', name: 'ResumeEditorView', component: () => import('@/views/resume/ResumeEditorView.vue'), meta: { requiresAuth: true, role: ROLE.APPLICANT, title: '简历中心-编辑', keepAlive: true } },
          { path: 'pdf', name: 'ResumePdfView', component: () => import('@/views/resume/ResumePdfView.vue'), meta: { requiresAuth: true, role: ROLE.APPLICANT, title: '简历中心-PDF', keepAlive: true } }
        ]
      },
      { path: 'applicant/applications', name: 'MyApplicationsView', component: () => import('@/views/application/MyApplicationsView.vue'), meta: { requiresAuth: true, role: ROLE.APPLICANT, title: '我的投递', keepAlive: true } },
      { path: 'applicant/favorites', name: 'MyFavoritesView', component: () => import('@/views/application/MyFavoritesView.vue'), meta: { requiresAuth: true, role: ROLE.APPLICANT, title: '我的收藏', keepAlive: true } },
      { path: 'applicant/guide', name: 'GuideCenterView', component: () => import('@/views/guide/GuideCenterView.vue'), meta: { requiresAuth: true, role: ROLE.APPLICANT, title: '应聘指南', keepAlive: true } },
      { path: 'hr/dashboard', name: 'HrDashboard', component: () => import('@/views/dashboard/HrDashboard.vue'), meta: { requiresAuth: true, role: ROLE.HR, title: '招聘概览', keepAlive: true } },
      { path: 'hr/jobs', name: 'HrJobManageView', component: () => import('@/views/hr/HrJobManageView.vue'), meta: { requiresAuth: true, role: ROLE.HR, title: '岗位管理', keepAlive: true } },
      { path: 'hr/candidates', name: 'HrCandidateManageView', component: () => import('@/views/hr/HrCandidateManageView.vue'), meta: { requiresAuth: true, role: ROLE.HR, title: '候选人管理', keepAlive: true } },
      { path: 'hr/applications', name: 'HrApplicationManageView', component: () => import('@/views/hr/HrApplicationManageView.vue'), meta: { requiresAuth: true, role: ROLE.HR, title: '投递管理', keepAlive: true } },
      { path: 'hr/review/:id', name: 'HrResumeReviewView', component: () => import('@/views/hr/HrResumeReviewView.vue'), meta: { requiresAuth: true, role: ROLE.HR, title: '简历评审', keepAlive: true } },
      { path: 'hr/statistics', name: 'HrStatisticsView', component: () => import('@/views/hr/HrStatisticsView.vue'), meta: { requiresAuth: true, role: ROLE.HR, title: '统计分析', keepAlive: true } }
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
    } catch {
      store.commit('user/clearUser')
      store.commit('tabs/resetTabs')
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
  const tabRoute = [...to.matched].reverse().find((item) => item.meta?.title && !item.meta?.hiddenTab)
  const target = tabRoute
    ? {
        fullPath: to.fullPath,
        name: to.name,
        meta: {
          ...tabRoute.meta,
          title: to.meta?.title || tabRoute.meta.title,
          keepAlive: Boolean(to.meta?.keepAlive || tabRoute.meta.keepAlive)
        }
      }
    : to
  store.commit('tabs/addTab', target)
  store.commit('tabs/setActive', to.fullPath)
})

export default router
