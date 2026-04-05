export function formatMoney(min, max) {
  if (min == null || max == null) return '--'
  return `${min}-${max} ?/?`
}

export function formatDateTime(value) {
  if (!value) return '--'
  return String(value).replace('T', ' ')
}

export function joinLocation(provinceName, cityName) {
  return [provinceName, cityName].filter(Boolean).join(' / ') || '--'
}
