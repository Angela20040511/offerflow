<template>
  <div class="auth-page">
    <div class="auth-card">
      <div>
        <h1>OfferFlow</h1>
        <p>&#30331;&#24405;&#38598;&#22242;&#32479;&#19968;&#25307;&#32856;&#38376;&#25143;</p>
      </div>
      <el-form :model="form" label-position="top" @submit.prevent="handleLogin">
        <el-form-item label="&#36134;&#21495;">
          <el-input v-model="form.username" placeholder="&#35831;&#36755;&#20837;&#36134;&#21495;" />
        </el-form-item>
        <el-form-item label="&#23494;&#30721;">
          <el-input v-model="form.password" type="password" show-password placeholder="&#35831;&#36755;&#20837;&#23494;&#30721;" />
        </el-form-item>
        <el-form-item label="&#35282;&#33394;">
          <el-radio-group v-model="form.role">
            <el-radio-button label="APPLICANT">&#27714;&#32844;&#32773;</el-radio-button>
            <el-radio-button label="HR">&#25307;&#32856;&#20154;&#21592;</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-button type="primary" native-type="submit" class="submit-btn" :loading="submitting">&#30331;&#24405;</el-button>
        <div class="switch-link" @click="router.push('/register')">&#27880;&#20876;&#36134;&#21495;</div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'

const store = useStore()
const router = useRouter()

const form = reactive({ username: 'student01', password: '123456', role: 'APPLICANT' })
const submitting = ref(false)

const handleLogin = async () => {
  if (submitting.value) return
  submitting.value = true
  try {
    await store.dispatch('user/login', { ...form })
    await router.push(form.role === 'HR' ? '/hr/dashboard' : '/applicant/dashboard')
  } catch (error) {
    ElMessage.error(error?.message || '登录失败，请稍后重试')
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.auth-page { min-height: 100vh; display: flex; align-items: center; justify-content: center; padding: 24px; }
.auth-card { width: min(520px, 100%); padding: 36px 28px; border-radius: 32px; background: rgba(255,255,255,.92); box-shadow: 0 24px 50px rgba(153,109,49,.16); }
h1 { margin: 0; font-size: 3rem; color: #2a2d33; }
p { margin: 12px 0 28px; color: #8b6d4a; }
.submit-btn { width: 100%; margin-top: 8px; }
.switch-link { margin-top: 18px; text-align: center; color: #8b6d4a; cursor: pointer; }
</style>
