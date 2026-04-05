export function toAcceptPdf(file) {
  if (!file) return false
  return file.type === 'application/pdf' || file.name.endsWith('.pdf')
}
