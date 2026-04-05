export const storage = {
  get(key, fallback = null) {
    const value = localStorage.getItem(key)
    if (value == null) return fallback
    try {
      return JSON.parse(value)
    } catch {
      return value
    }
  },
  set(key, value) {
    const raw = typeof value === 'string' ? value : JSON.stringify(value)
    localStorage.setItem(key, raw)
  },
  remove(key) {
    localStorage.removeItem(key)
  }
}
