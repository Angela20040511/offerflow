<template>
  <div class="auth-page">
    <div class="auth-shell">
      <section class="auth-copy">
        <span class="eyebrow">Applicant Only</span>
        <h1>创建你的 OfferFlow 求职者账号</h1>
        <p>注册后可以管理简历、收藏岗位并完成在线投递。</p>
      </section>

      <div class="auth-card">
        <div>
          <h2>求职者注册</h2>
          <p>当前阶段仅开放求职者注册</p>
        </div>
        <el-form ref="formRef" :model="form" :rules="rules" label-position="top" @submit.prevent="handleRegister" @keyup.enter.prevent="handleRegister">
          <el-form-item label="账号" prop="username">
            <el-input v-model="form.username" placeholder="请输入账号" />
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input v-model="form.password" type="password" show-password placeholder="请设置密码" />
          </el-form-item>
          <el-form-item label="确认密码" prop="confirmPassword">
            <el-input v-model="form.confirmPassword" type="password" show-password placeholder="请再次输入密码" />
          </el-form-item>
          <el-form-item label="邮箱" prop="email">
            <el-input v-model="form.email" placeholder="请输入邮箱" />
          </el-form-item>
          <el-form-item label="手机号" prop="phone">
            <el-input v-model="form.phone" placeholder="请输入手机号" />
          </el-form-item>
          <el-alert v-if="submitError" :title="submitError" type="error" show-icon :closable="false" />
          <el-button type="primary" class="submit-btn" :loading="submitting" @click="handleRegister">注册</el-button>
          <div class="switch-link" @click="router.push('/login')">已有账号？去登录</div>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { registerApi } from '@/api/auth'

const router = useRouter()
const formRef = ref(null)
const submitting = ref(false)
const submitError = ref('')

const form = reactive({
  username: '',
  password: '',
  confirmPassword: '',
  email: '',
  phone: '',
  role: 'APPLICANT'
})

const validateConfirmPassword = (_, value, callback) => {
  if (!value) {
    callback(new Error('请再次输入密码'))
    return
  }
  if (value !== form.password) {
    callback(new Error('两次输入的密码不一致'))
    return
  }
  callback()
}

const rules = {
  username: [{ required: true, message: '请输入账号', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  confirmPassword: [{ validator: validateConfirmPassword, trigger: ['blur', 'change'] }],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入合法的邮箱地址', trigger: ['blur', 'change'] }
  ],
  phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }]
}

const handleRegister = async () => {
  if (submitting.value) return
  submitError.value = ''
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return

  submitting.value = true
  try {
    await registerApi({ ...form, role: 'APPLICANT' })
    ElMessage.success('注册成功，请登录')
    router.push('/login')
  } catch (error) {
    submitError.value = error?.message || '注册失败，请稍后重试'
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.auth-page {
  min-height: 100vh;
  padding: 24px;
  background:
    radial-gradient(circle at top left, rgba(93, 118, 255, 0.26), transparent 24%),
    radial-gradient(circle at bottom right, rgba(35, 180, 255, 0.18), transparent 22%),
    linear-gradient(140deg, #0c1430 0%, #162954 44%, #1f4178 100%);
  display: grid;
  place-items: center;
}

.auth-shell {
  width: min(1100px, 100%);
  display: grid;
  grid-template-columns: minmax(0, 1fr) minmax(360px, 420px);
  gap: 28px;
  align-items: center;
}

.auth-copy {
  color: #f4f8ff;
}

.eyebrow {
  display: inline-flex;
  padding: 8px 14px;
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.08);
  color: #8ed2ff;
  font-size: 13px;
  letter-spacing: 0.08em;
}

.auth-copy h1 {
  font-size: clamp(36px, 5vw, 58px);
  line-height: 1.08;
  margin: 18px 0;
}

.auth-copy p {
  color: rgba(235, 241, 255, 0.76);
  font-size: 18px;
}

.auth-card {
  padding: 32px 28px;
  border-radius: 32px;
  background: rgba(255, 255, 255, 0.9);
  border: 1px solid rgba(130, 157, 255, 0.18);
  box-shadow: 0 30px 60px rgba(8, 18, 50, 0.24);
  backdrop-filter: blur(14px);
}

.auth-card h2 {
  margin: 0;
  color: #162246;
}

.auth-card p {
  margin: 10px 0 24px;
  color: #61729b;
}

.submit-btn {
  width: 100%;
  margin-top: 8px;
}

.switch-link {
  margin-top: 18px;
  text-align: center;
  color: #3c57da;
  cursor: pointer;
}

@media (max-width: 920px) {
  .auth-shell {
    grid-template-columns: 1fr;
  }
}
</style>
