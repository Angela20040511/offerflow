export function requireValue(value, label) {
  if (value === null || value === undefined || value === '') {
    throw new Error(`${label}不能为空`)
  }
}
