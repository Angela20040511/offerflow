<template>
  <div class="resume-editor">
    <ResumeBasicForm :model="resume.basicInfo" />
    <ResumeEducationForm :model="resume.educationList" />
    <ResumeExperienceForm :model="resume.experienceList" />
    <ResumeProjectForm :model="resume.projectList" />
    <ResumeSkillForm :model="resume.skills" :candidates="skillCandidates" />
  </div>
</template>

<script setup>
import { watchEffect } from 'vue'
import ResumeBasicForm from '@/components/resume/ResumeBasicForm.vue'
import ResumeEducationForm from '@/components/resume/ResumeEducationForm.vue'
import ResumeExperienceForm from '@/components/resume/ResumeExperienceForm.vue'
import ResumeProjectForm from '@/components/resume/ResumeProjectForm.vue'
import ResumeSkillForm from '@/components/resume/ResumeSkillForm.vue'

const props = defineProps({
  resume: { type: Object, required: true },
  skillCandidates: { type: Array, default: () => [] }
})

watchEffect(() => {
  props.resume.basicInfo ||= { name: '', phone: '', email: '', educationLevel: '', gender: '' }
  props.resume.educationList ||= []
  props.resume.experienceList ||= []
  props.resume.projectList ||= []
  props.resume.skills ||= { jobSkills: [], customSkills: [] }
  props.resume.skills.jobSkills ||= []
  props.resume.skills.customSkills ||= []
})
</script>

<style scoped>
.resume-editor {
  display: grid;
  gap: 20px;
}
</style>
