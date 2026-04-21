const CITY_SUFFIXES = ['省', '市', '自治区', '特别行政区', '壮族自治区', '回族自治区', '维吾尔自治区']

const stripSuffix = (value) => {
  const text = value == null ? '' : String(value).trim()
  if (!text) return ''
  return CITY_SUFFIXES.reduce((current, suffix) => (current.endsWith(suffix) ? current.slice(0, -suffix.length) : current), text)
}

export const formatShortLocation = (provinceName, cityName) => {
  const province = stripSuffix(provinceName)
  const city = stripSuffix(cityName)
  return [province, city].filter(Boolean).join('/') || '地点待定'
}

export const parseTagList = (value) => {
  if (Array.isArray(value)) return value.filter(Boolean)
  if (typeof value === 'string' && value.trim()) {
    try {
      const parsed = JSON.parse(value)
      return Array.isArray(parsed) ? parsed.filter(Boolean) : []
    } catch {
      return []
    }
  }
  return []
}

export const getTagSummary = (value, limit = 3) => {
  const list = parseTagList(value)
  return {
    visible: list.slice(0, limit),
    extra: Math.max(0, list.length - limit)
  }
}
