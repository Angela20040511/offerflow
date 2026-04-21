<template>
  <PageFrame title="岗位管理" desc="统一维护集团各子公司的岗位信息、业务线和招聘要求。">
    <HrJobToolbar :model="filters" :subsidiaries="subsidiaries" @search="load" @reset="reset" @create="openDialog" />
    <div class="table-wrap">
      <HrJobTable :data="page.list || []" @edit="edit" @remove="remove" />
    </div>
    <PaginationBar :total="page.total || 0" :page-num="filters.pageNum" :page-size="filters.pageSize" @change="changePage" />

    <el-dialog v-model="visible" title="岗位信息" width="760px">
      <el-form :model="form" label-position="top" class="job-form-grid">
        <el-form-item label="岗位名称"><el-input v-model="form.title" /></el-form-item>
        <el-form-item label="子公司">
          <el-select v-model="form.subsidiaryId" filterable @change="handleSubsidiaryChange">
            <el-option v-for="item in subsidiaries" :key="item.id" :label="item.subsidiaryName" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="业务线"><el-input v-model="form.businessLine" /></el-form-item>
        <el-form-item label="岗位类别">
          <el-select v-model="form.jobCategoryId" filterable>
            <el-option v-for="item in categories" :key="item.id" :label="item.categoryName" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="省份">
          <el-select v-model="form.provinceCode" filterable @change="handleProvinceChange">
            <el-option v-for="item in provinces" :key="item.code" :label="item.name" :value="item.code" />
          </el-select>
        </el-form-item>
        <el-form-item label="城市">
          <el-select v-model="form.cityCode" filterable>
            <el-option v-for="item in cities" :key="item.code" :label="item.name" :value="item.code" />
          </el-select>
        </el-form-item>
        <el-form-item label="工作方式">
          <el-select v-model="form.workMode">
            <el-option label="现场办公" value="ONSITE" />
            <el-option label="远程协同" value="REMOTE" />
            <el-option label="混合办公" value="HYBRID" />
          </el-select>
        </el-form-item>
        <el-form-item label="工作时长">
          <el-select v-model="form.durationType">
            <el-option label="短期实习" value="SHORT_TERM" />
            <el-option label="长期实习" value="LONG_TERM" />
            <el-option label="校招全职" value="FULL_TIME" />
          </el-select>
        </el-form-item>
        <el-form-item label="岗位状态">
          <el-select v-model="form.status">
            <el-option v-for="item in jobStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="日薪下限"><el-input-number v-model="form.salaryMin" /></el-form-item>
        <el-form-item label="日薪上限"><el-input-number v-model="form.salaryMax" /></el-form-item>
        <el-form-item label="推荐技能" class="span-2">
          <el-select v-model="form.requiredSkills" multiple filterable allow-create default-first-option>
            <el-option v-for="item in skillCandidates" :key="item" :label="item" :value="item" />
          </el-select>
        </el-form-item>
        <el-form-item label="岗位说明" class="span-2"><el-input v-model="form.description" type="textarea" :rows="4" /></el-form-item>
        <el-form-item label="任职要求" class="span-2"><el-input v-model="form.requirement" type="textarea" :rows="4" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="visible = false">取消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
      </template>
    </el-dialog>
  </PageFrame>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { useStore } from 'vuex'
import { createJobApi, deleteJobApi, getHrJobsApi, getJobDetailApi, updateJobApi } from '@/api/job'
import PaginationBar from '@/components/common/PaginationBar.vue'
import HrJobTable from '@/components/hr/HrJobTable.vue'
import HrJobToolbar from '@/components/hr/HrJobToolbar.vue'
import PageFrame from '@/components/layout/PageFrame.vue'
import { jobStatusOptions } from '@/constants/status'

const store = useStore()
const page = reactive({ list: [], total: 0 })
const filters = reactive({ keyword: '', subsidiaryId: '', pageNum: 1, pageSize: 10 })
const visible = ref(false)
const skillCandidates = ['Vue 3', 'JavaScript', 'TypeScript', 'Spring Boot', 'MySQL', 'Redis', 'SQL', 'Python', 'Pandas', 'Excel', 'Power BI', 'Tableau', '流程管理', 'Figma']

const subsidiaries = computed(() => store.state.common.subsidiaries || [])
const categories = computed(() => store.state.common.categories || [])
const provinces = computed(() => store.state.common.provinces || [])
const cities = computed(() => store.getters['common/cityOptions'](form.provinceCode))

const baseForm = () => ({
  id: null,
  title: '',
  subsidiaryId: '',
  jobCategoryId: '',
  businessLine: '',
  provinceCode: '',
  cityCode: '',
  workMode: 'ONSITE',
  durationType: 'LONG_TERM',
  salaryMin: 150,
  salaryMax: 250,
  requiredSkills: [],
  description: '',
  requirement: '',
  status: 'OPEN'
})

const form = reactive(baseForm())

const parseSkills = (value) => {
  if (Array.isArray(value)) return value
  if (typeof value === 'string' && value.trim()) {
    try {
      const parsed = JSON.parse(value)
      return Array.isArray(parsed) ? parsed : []
    } catch {
      return []
    }
  }
  return []
}

const load = async () => {
  await store.dispatch('common/ensureBaseData')
  Object.assign(page, await getHrJobsApi(filters))
}

const handleSubsidiaryChange = (subsidiaryId) => {
  const selected = subsidiaries.value.find((item) => Number(item.id) === Number(subsidiaryId))
  form.businessLine = selected?.businessLine || ''
}

const openDialog = async () => {
  Object.assign(form, baseForm())
  if (provinces.value[0]) {
    form.provinceCode = provinces.value[0].code
    await store.dispatch('common/fetchCities', form.provinceCode)
    form.cityCode = cities.value[0]?.code || ''
  }
  if (subsidiaries.value[0]) {
    form.subsidiaryId = subsidiaries.value[0].id
    form.businessLine = subsidiaries.value[0].businessLine
  }
  if (categories.value[0]) {
    form.jobCategoryId = categories.value[0].id
  }
  visible.value = true
}

const edit = async (row) => {
  const detail = await getJobDetailApi(row.id)
  Object.assign(form, baseForm(), detail, {
    requiredSkills: parseSkills(detail.requiredSkills?.length ? detail.requiredSkills : detail.requiredSkillsJson)
  })
  if (form.provinceCode) {
    await store.dispatch('common/fetchCities', form.provinceCode)
  }
  visible.value = true
}

const save = async () => {
  const selectedSubsidiary = subsidiaries.value.find((item) => Number(item.id) === Number(form.subsidiaryId))
  const selectedCategory = categories.value.find((item) => Number(item.id) === Number(form.jobCategoryId))
  const selectedCity = cities.value.find((item) => item.code === form.cityCode)
  const payload = {
    id: form.id,
    title: form.title,
    subsidiaryId: form.subsidiaryId,
    jobCategoryId: form.jobCategoryId,
    businessLine: form.businessLine || selectedSubsidiary?.businessLine || '',
    provinceCode: form.provinceCode,
    cityCode: form.cityCode,
    workMode: form.workMode,
    durationType: form.durationType,
    requiredSkills: form.requiredSkills,
    tags: form.requiredSkills,
    salaryMin: form.salaryMin,
    salaryMax: form.salaryMax,
    description: form.description,
    requirement: form.requirement,
    status: form.status,
    company: selectedSubsidiary?.subsidiaryName || '',
    city: selectedCity?.name || '',
    category: selectedCategory?.categoryName || ''
  }
  if (form.id) {
    await updateJobApi(form.id, payload)
  } else {
    await createJobApi(payload)
  }
  ElMessage.success('岗位信息已保存')
  visible.value = false
  await load()
}

const remove = async (row) => {
  await deleteJobApi(row.id)
  ElMessage.success('岗位已删除')
  await load()
}

const changePage = (pageNum) => {
  filters.pageNum = pageNum
  load()
}

const reset = () => {
  Object.assign(filters, { keyword: '', subsidiaryId: '', pageNum: 1, pageSize: 10 })
  load()
}

const handleProvinceChange = async (provinceCode) => {
  form.cityCode = ''
  await store.dispatch('common/fetchCities', provinceCode)
  form.cityCode = cities.value[0]?.code || ''
}

onMounted(load)
</script>

<style scoped>
.table-wrap {
  width: 100%;
  min-width: 0;
  overflow-x: auto;
}

.job-form-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 0 14px;
}

.span-2 {
  grid-column: span 2;
}

@media (max-width: 760px) {
  .job-form-grid {
    grid-template-columns: 1fr;
  }

  .span-2 {
    grid-column: span 1;
  }
}
</style>
