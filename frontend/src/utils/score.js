export function scoreTone(score = 0) {
  if (score >= 85) return 'success'
  if (score >= 60) return 'warning'
  return 'danger'
}
