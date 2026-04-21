<template>
  <div class="auth-page">
    <div class="auth-shell">
      <section class="auth-copy">
        <span class="eyebrow">OfferFlow</span>
        <h1>登录你的招聘协同工作台</h1>
        <p>继续访问岗位、简历、投递与候选人评审流程。</p>
      </section>

      <div class="auth-card">
        <div>
          <h2>欢迎回来</h2>
          <p>使用现有账号进入系统</p>
        </div>
        <el-form :model="form" label-position="top" @submit.prevent="handleLogin" @keyup.enter.prevent="handleLogin">
          <el-form-item label="账号">
            <el-input v-model="form.username" placeholder="请输入账号" />
          </el-form-item>
          <el-form-item label="密码">
            <el-input v-model="form.password" type="password" show-password placeholder="请输入密码" />
          </el-form-item>
          <el-form-item label="角色">
            <el-radio-group v-model="form.role">
              <el-radio-button label="APPLICANT">求职者</el-radio-button>
              <el-radio-button label="HR">招聘人员</el-radio-button>
            </el-radio-group>
          </el-form-item>
          <el-button type="primary" class="submit-btn" :loading="submitting" @click="handleLogin">登录</el-button>
          <div class="switch-link" @click="router.push('/register')">没有账号？去注册</div>
        </el-form>
      </div>
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
