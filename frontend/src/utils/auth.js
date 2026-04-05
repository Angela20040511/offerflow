import { storage } from './storage'

const TOKEN_KEY = 'offerflow_token'
const USER_KEY = 'offerflow_user'

export function getToken() {
  return storage.get(TOKEN_KEY, '')
}

export function setToken(token) {
  storage.set(TOKEN_KEY, token || '')
}

export function clearToken() {
  storage.remove(TOKEN_KEY)
}

export function getUserCache() {
  return storage.get(USER_KEY, null)
}

export function setUserCache(user) {
  storage.set(USER_KEY, user)
}

export function clearUserCache() {
  storage.remove(USER_KEY)
}
