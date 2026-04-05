<template>
  <div class="auth-page">
    <div class="auth-card">
      <div>
        <h1>OfferFlow</h1>
        <p>&#21019;&#24314;&#38598;&#22242;&#25307;&#32856;&#38376;&#25143;&#36134;&#21495;</p>
      </div>
      <el-form :model="form" label-position="top" @submit.prevent>
        <el-form-item label="&#36134;&#21495;"><el-input v-model="form.username" placeholder="&#35831;&#36755;&#20837;&#36134;&#21495;" /></el-form-item>
        <el-form-item label="&#23494;&#30721;"><el-input v-model="form.password" type="password" show-password placeholder="&#35831;&#35774;&#32622;&#23494;&#30721;" /></el-form-item>
        <el-form-item label="&#37038;&#31665;"><el-input v-model="form.email" placeholder="&#35831;&#36755;&#20837;&#37038;&#31665;" /></el-form-item>
        <el-form-item label="&#25163;&#26426;&#21495;"><el-input v-model="form.phone" placeholder="&#35831;&#36755;&#20837;&#25163;&#26426;&#21495;" /></el-form-item>
        <el-form-item label="&#35282;&#33394;">
          <el-radio-group v-model="form.role">
            <el-radio-button label="APPLICANT">&#27714;&#32844;&#32773;</el-radio-button>
            <el-radio-button label="HR">&#25307;&#32856;&#20154;&#21592;</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-button type="primary" class="submit-btn" @click="handleRegister">&#27880;&#20876;</el-button>
        <div class="switch-link" @click="router.push('/login')">&#36820;&#22238;&#30331;&#24405;</div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import request from '@/api/request'

const router = useRouter()
const form = reactive({ username: '', password: '', email: '', phone: '', role: 'APPLICANT' })

const handleRegister = async () => {
  await request.post('/auth/register', form)
  ElMessage.success('\u6ce8\u518c\u6210\u529f\uff0c\u8bf7\u767b\u5f55')
  router.push('/login')
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
