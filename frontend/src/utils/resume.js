const asArray = (value) => (Array.isArray(value) ? value : [])

const text = (value) => (value == null ? '' : String(value).trim())

const splitPeriod = (period) => {
  const raw = text(period)
  if (!raw) {
    return { startDate: '', endDate: '' }
  }
  const parts = raw
    .split(/\s*(?:-|~|至|到)\s*/)
    .map((item) => item.trim())
    .filter(Boolean)
  return {
    startDate: parts[0] || raw,
    endDate: parts[1] || ''
  }
}

export const educationLevelOptions = ['博士后', '博士', '硕士/研究生', '本科', '大专', '高中', '中专', '其他']
export const genderOptions = ['男', '女', '不便透露', '其他']
export const employmentTypeOptions = ['正式工作', '实习', '兼职', '见习/培训生', '创业']

export const formatRange = (startDate, endDate) => [text(startDate), text(endDate)].filter(Boolean).join(' - ')

export const normalizeBasicInfo = (basicInfo = {}) => ({
  name: text(basicInfo.name),
  phone: text(basicInfo.phone),
  email: text(basicInfo.email),
  educationLevel: text(basicInfo.educationLevel),
  gender: text(basicInfo.gender)
})

export const normalizeEducationItem = (item = {}) => {
  const periodRange = splitPeriod(item.period)
  return {
    school: text(item.school),
    educationStage: text(item.educationStage || item.educationLevel || item.degree),
    major: text(item.major),
    startDate: text(item.startDate || periodRange.startDate),
    endDate: text(item.endDate || periodRange.endDate),
    courses: text(item.courses || item.highlight)
  }
}

export const normalizeExperienceItem = (item = {}) => {
  const periodRange = splitPeriod(item.period)
  return {
    company: text(item.company),
    position: text(item.position || item.title),
    employmentType: text(item.employmentType || item.workType),
    startDate: text(item.startDate || periodRange.startDate),
    endDate: text(item.endDate || periodRange.endDate),
    content: text(item.content || item.description)
  }
}

export const normalizeProjectItem = (item = {}) => {
  const periodRange = splitPeriod(item.period)
  return {
    name: text(item.name),
    role: text(item.role),
    startDate: text(item.startDate || periodRange.startDate),
    endDate: text(item.endDate || periodRange.endDate),
    link: text(item.link),
    content: text(item.content || item.description)
  }
}

export const normalizeSkills = (resume = {}) => {
  if (resume.skills && typeof resume.skills === 'object') {
    return {
      jobSkills: asArray(resume.skills.jobSkills).map(text).filter(Boolean),
      customSkills: asArray(resume.skills.customSkills).map(text).filter(Boolean)
    }
  }

  const skillList = asArray(resume.skillList).map(text).filter(Boolean)
  return {
    jobSkills: skillList,
    customSkills: []
  }
}

export const normalizeResume = (resume = {}) => {
  const basicInfo = normalizeBasicInfo(resume.basicInfo || {})
  return {
    id: resume.id,
    userId: resume.userId,
    title: text(resume.title || resume.resumeName),
    resumeName: text(resume.resumeName || resume.title || '通用简历'),
    resumeType: text(resume.resumeType || 'GENERAL'),
    targetSubsidiaryId: resume.targetSubsidiaryId ?? null,
    targetCategoryId: resume.targetCategoryId ?? null,
    templateCode: text(resume.templateCode || 'classic'),
    pdfUrl: text(resume.pdfUrl),
    completeScore: Number(resume.completeScore || 0),
    isDefault: Boolean(Number(resume.isDefault || 0)),
    updateTime: resume.updateTime || '',
    basicInfo,
    educationList: asArray(resume.educationList).map(normalizeEducationItem),
    experienceList: asArray(resume.experienceList).map(normalizeExperienceItem),
    projectList: asArray(resume.projectList).map(normalizeProjectItem),
    skills: normalizeSkills(resume)
  }
}

export const ensureResumeShape = (resume = {}) => normalizeResume(resume)

export const toResumeSavePayload = (resume = {}) => {
  const normalized = normalizeResume(resume)
  return {
    id: normalized.id,
    title: normalized.title || normalized.resumeName,
    resumeName: normalized.resumeName,
    resumeType: normalized.resumeType,
    targetSubsidiaryId: normalized.targetSubsidiaryId,
    targetCategoryId: normalized.targetCategoryId,
    templateCode: normalized.templateCode,
    basicInfo: {
      name: normalized.basicInfo.name,
      phone: normalized.basicInfo.phone,
      email: normalized.basicInfo.email,
      educationLevel: normalized.basicInfo.educationLevel,
      gender: normalized.basicInfo.gender
    },
    educationList: normalized.educationList.map((item) => ({
      school: item.school,
      educationStage: item.educationStage,
      major: item.major,
      startDate: item.startDate,
      endDate: item.endDate,
      courses: item.courses
    })),
    experienceList: normalized.experienceList.map((item) => ({
      company: item.company,
      position: item.position,
      employmentType: item.employmentType,
      startDate: item.startDate,
      endDate: item.endDate,
      content: item.content
    })),
    projectList: normalized.projectList.map((item) => ({
      name: item.name,
      role: item.role,
      startDate: item.startDate,
      endDate: item.endDate,
      link: item.link,
      content: item.content
    })),
    skillList: [...normalized.skills.jobSkills, ...normalized.skills.customSkills].filter(Boolean)
  }
}

export const getPdfFileName = (resume = {}) => {
  if (text(resume.pdfUrl)) {
    const parts = resume.pdfUrl.split('?')[0].split('/')
    return parts[parts.length - 1]
  }
  return `${text(resume.resumeName || '通用简历')}.pdf`
}
