export const JOB_STATUS = {
  OPEN: 'OPEN',
  CLOSED: 'CLOSED',
  DRAFT: 'DRAFT'
}

export const APPLICATION_STAGE = {
  APPLIED: 'APPLIED',
  SCREENING: 'SCREENING',
  INTERVIEW: 'INTERVIEW',
  OFFER: 'OFFER',
  REJECTED: 'REJECTED',
  WITHDRAWN: 'WITHDRAWN'
}

export const jobStatusLabelMap = {
  [JOB_STATUS.OPEN]: '\u62db\u8058\u4e2d',
  [JOB_STATUS.CLOSED]: '\u5df2\u5173\u95ed',
  [JOB_STATUS.DRAFT]: '\u8349\u7a3f'
}

export const applicationStageLabelMap = {
  [APPLICATION_STAGE.APPLIED]: '\u5df2\u6295\u9012',
  [APPLICATION_STAGE.SCREENING]: '\u7b5b\u9009\u4e2d',
  [APPLICATION_STAGE.INTERVIEW]: '\u9762\u8bd5\u4e2d',
  [APPLICATION_STAGE.OFFER]: '\u5df2\u53d1\u5f55\u7528',
  [APPLICATION_STAGE.REJECTED]: '\u5df2\u6dd8\u6c70',
  [APPLICATION_STAGE.WITHDRAWN]: '\u5df2\u64a4\u56de'
}

export const jobStatusOptions = [
  { label: '\u62db\u8058\u4e2d', value: JOB_STATUS.OPEN },
  { label: '\u5df2\u5173\u95ed', value: JOB_STATUS.CLOSED },
  { label: '\u8349\u7a3f', value: JOB_STATUS.DRAFT }
]

export const applicationStageOptions = [
  { label: '\u5df2\u6295\u9012', value: APPLICATION_STAGE.APPLIED },
  { label: '\u7b5b\u9009\u4e2d', value: APPLICATION_STAGE.SCREENING },
  { label: '\u9762\u8bd5\u4e2d', value: APPLICATION_STAGE.INTERVIEW },
  { label: '\u5df2\u53d1\u5f55\u7528', value: APPLICATION_STAGE.OFFER },
  { label: '\u5df2\u6dd8\u6c70', value: APPLICATION_STAGE.REJECTED },
  { label: '\u5df2\u64a4\u56de', value: APPLICATION_STAGE.WITHDRAWN }
]

export const hrApplicationStageOptions = applicationStageOptions.filter(
  (item) => ![APPLICATION_STAGE.APPLIED, APPLICATION_STAGE.WITHDRAWN].includes(item.value)
)

export const salaryRangeOptions = [
  { label: '150 \u5143/\u5929\u4ee5\u4e0b', value: '0-150', salaryMin: 0, salaryMax: 150 },
  { label: '150-180 \u5143/\u5929', value: '150-180', salaryMin: 150, salaryMax: 180 },
  { label: '180-220 \u5143/\u5929', value: '180-220', salaryMin: 180, salaryMax: 220 },
  { label: '220-250 \u5143/\u5929', value: '220-250', salaryMin: 220, salaryMax: 250 },
  { label: '250 \u5143/\u5929\u4ee5\u4e0a', value: '250-999', salaryMin: 250, salaryMax: 999 }
]

export const JOB_STATUS_OPTIONS = jobStatusOptions
export const APPLICATION_STAGE_OPTIONS = applicationStageOptions
export const HR_APPLICATION_STAGE_OPTIONS = hrApplicationStageOptions
export const JOB_STATUS_LABEL_MAP = jobStatusLabelMap
export const APPLICATION_STAGE_LABEL_MAP = applicationStageLabelMap
export const JOB_STATUS_LABELS = jobStatusLabelMap
export const APPLICATION_STAGE_LABELS = applicationStageLabelMap
