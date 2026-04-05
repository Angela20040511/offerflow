<template>
  <div class="resume-preview" :class="templateClass">
    <header class="preview-head">
      <div>
        <h2>{{ displayName }}</h2>
        <p>{{ careerDirection }}</p>
      </div>
      <div class="contact">
        <span>{{ phone }}</span>
        <span>{{ email }}</span>
      </div>
    </header>

    <section>
      <h3>教育经历</h3>
      <article v-for="(item, index) in normalizedResume.educationList" :key="index">
        <strong>{{ [item.school, item.major].filter(Boolean).join(' / ') || '教育信息待补充' }}</strong>
        <p>{{ [item.degree, formatRange(item.startDate, item.endDate)].filter(Boolean).join(' / ') || '时间待补充' }}</p>
        <p v-if="item.highlight">{{ item.highlight }}</p>
      </article>
      <p v-if="!normalizedResume.educationList.length" class="empty-text">暂无教育经历</p>
    </section>

    <section>
      <h3>实习经历</h3>
      <article v-for="(item, index) in normalizedResume.experienceList" :key="index">
        <strong>{{ [item.company, item.position].filter(Boolean).join(' / ') || '实习信息待补充' }}</strong>
        <p>{{ [formatRange(item.startDate, item.endDate), item.location].filter(Boolean).join(' / ') || '时间待补充' }}</p>
        <p v-if="item.content">{{ item.content }}</p>
      </article>
      <p v-if="!normalizedResume.experienceList.length" class="empty-text">暂无实习经历</p>
    </section>

    <section>
      <h3>项目经历</h3>
      <article v-for="(item, index) in normalizedResume.projectList" :key="index">
        <strong>{{ [item.name, item.role].filter(Boolean).join(' / ') || '项目信息待补充' }}</strong>
        <p>{{ formatRange(item.startDate, item.endDate) || '时间待补充' }}</p>
        <p v-if="item.content">{{ item.content }}</p>
        <p v-if="item.link">{{ item.link }}</p>
      </article>
      <p v-if="!normalizedResume.projectList.length" class="empty-text">暂无项目经历</p>
    </section>

    <section>
      <h3>技能标签</h3>
      <div v-if="skillList.length" class="tag-list">
        <el-tag v-for="item in skillList" :key="item" effect="plain">{{ item }}</el-tag>
      </div>
      <p v-else class="empty-text">暂无技能标签</p>
    </section>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { ensureResumeShape, formatRange } from '@/utils/resume'

const props = defineProps({
  resume: { type: Object, default: () => ({}) }
})

const normalizedResume = computed(() => ensureResumeShape(props.resume))
const profile = computed(() => normalizedResume.value.basicInfo || {})
const skillList = computed(() => [...normalizedResume.value.skills.jobSkills, ...normalizedResume.value.skills.customSkills])
const templateClass = computed(() => `template-${normalizedResume.value.templateCode || 'classic'}`)
const displayName = computed(() => profile.value.name || '未填写姓名')
const careerDirection = computed(() => profile.value.careerDirection || '求职方向待补充')
const phone = computed(() => profile.value.phone || '未填写手机')
const email = computed(() => profile.value.email || '未填写邮箱')
</script>

<style scoped>
.resume-preview {
  min-height: 100%;
  padding: 24px;
  border-radius: 24px;
  background: #fff;
  display: grid;
  gap: 20px;
}

.template-classic {
  border-top: 8px solid #2958d9;
}

.template-fresh {
  background: linear-gradient(180deg, #f8fffb 0%, #ffffff 100%);
  border-left: 8px solid #34a37a;
}

.template-sharp {
  background: linear-gradient(135deg, #20242b 0%, #2f3640 100%);
  color: #fff;
}

.preview-head {
  display: flex;
  justify-content: space-between;
  gap: 16px;
}

.preview-head h2,
section h3 {
  margin: 0;
}

.contact,
.tag-list {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
}

.empty-text {
  color: #866846;
}
</style>
