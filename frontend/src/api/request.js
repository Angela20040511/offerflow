import axios from 'axios'
import { ElMessage } from 'element-plus'
import { clearToken, clearUserCache, getToken } from '@/utils/auth'

const service = axios.create({
  baseURL: '/api',
  timeout: 15000
})

service.interceptors.request.use((config) => {
  const token = getToken()
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

service.interceptors.response.use(
  (response) => {
    const payload = response.data
    if (payload && typeof payload.code !== 'undefined') {
      if (payload.code === 0 || payload.code === 200) {
        return payload.data
      }
      ElMessage.error(payload.msg || '\u8bf7\u6c42\u5931\u8d25')
      return Promise.reject(new Error(payload.msg || '\u8bf7\u6c42\u5931\u8d25'))
    }
    return payload
  },
  (error) => {
    const status = error?.response?.status
    if (status === 401 && getToken()) {
      clearToken()
      clearUserCache()
      if (!window.location.pathname.startsWith('/login')) {
        window.location.replace('/login')
      }
    }
    const message = error?.response?.data?.msg || error.message || '\u7f51\u7edc\u5f02\u5e38\uff0c\u8bf7\u7a0d\u540e\u91cd\u8bd5'
    ElMessage.error(message)
    return Promise.reject(error)
  }
)

export default service
