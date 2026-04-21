<template>
  <div class="landing-page">
    <header class="topbar">
      <div class="brand">
        <span class="brand-mark">OF</span>
        <div>
          <strong>OfferFlow</strong>
          <p>科技招聘协同平台</p>
        </div>
      </div>
      <div class="topbar-actions">
        <el-button plain @click="router.push('/login')">登录</el-button>
        <el-button type="primary" @click="router.push('/register')">注册</el-button>
      </div>
    </header>

    <main class="hero-section">
      <section class="hero-copy">
        <span class="eyebrow">Smart Hiring Workspace</span>
        <h1>让招聘协同、岗位投递与简历管理回到一个流畅的工作台里</h1>
        <p>
          OfferFlow 为招聘团队和求职者提供统一的岗位、投递、收藏、评审与简历协同能力，
          把原本分散的流程整合成更清晰、更现代的招聘体验。
        </p>
        <div class="hero-actions">
          <el-button type="primary" size="large" @click="router.push('/register')">立即开始</el-button>
          <el-button size="large" plain @click="router.push('/login')">已有账号，去登录</el-button>
        </div>
      </section>

      <section class="hero-panel card-panel">
        <div class="hero-panel-head">
          <span>平台能力</span>
          <strong>招聘与投递双端协同</strong>
        </div>
        <div class="metric-grid">
          <article>
            <span>岗位管理</span>
            <strong>多状态发布</strong>
          </article>
          <article>
            <span>简历中心</span>
            <strong>结构化 + PDF</strong>
          </article>
          <article>
            <span>候选人流转</span>
            <strong>筛选到录用</strong>
          </article>
          <article>
            <span>推荐体验</span>
            <strong>收藏与再投递</strong>
          </article>
        </div>
      </section>
    </main>

    <section class="feature-grid">
      <article class="feature-card card-panel">
        <h3>招聘管理能力</h3>
        <p>统一查看岗位、投递、候选人状态与 PDF 简历，帮助 HR 更快完成筛选与跟进。</p>
      </article>
      <article class="feature-card card-panel">
        <h3>求职者投递能力</h3>
        <p>支持岗位收藏、撤回后重新投递、简历多版本管理和系统匹配分说明。</p>
      </article>
      <article class="feature-card card-panel">
        <h3>产品化体验</h3>
        <p>从公开介绍页到登录、注册、岗位中心与评审页，统一科技感视觉语言。</p>
      </article>
    </section>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()
</script>

<style scoped>
.landing-page {
  min-height: 100vh;
  padding: 28px;
  background:
    radial-gradient(circle at 10% 20%, rgba(94, 106, 255, 0.22), transparent 26%),
    radial-gradient(circle at 90% 10%, rgba(35, 180, 255, 0.16), transparent 24%),
    linear-gradient(145deg, #0d1530 0%, #152751 42%, #1a3366 100%);
  color: #f5f8ff;
}

.topbar,
.hero-section,
.feature-grid {
  width: min(1180px, 100%);
  margin: 0 auto;
}

.topbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
}

.brand {
  display: flex;
  align-items: center;
  gap: 14px;
}

.brand-mark {
  width: 48px;
  height: 48px;
  border-radius: 16px;
  display: grid;
  place-items: center;
  font-weight: 800;
  background: linear-gradient(135deg, #5d76ff, #2dd4ff);
  color: #fff;
}

.brand p {
  margin: 4px 0 0;
  color: rgba(235, 241, 255, 0.72);
}

.topbar-actions {
  display: flex;
  gap: 12px;
}

.hero-section {
  display: grid;
  grid-template-columns: minmax(0, 1.15fr) minmax(320px, 0.85fr);
  gap: 28px;
  padding: 72px 0 48px;
  align-items: center;
}

.eyebrow {
  display: inline-flex;
  padding: 8px 14px;
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.08);
  color: #a9d7ff;
  font-size: 13px;
  letter-spacing: 0.08em;
  text-transform: uppercase;
}

.hero-copy h1 {
  font-size: clamp(40px, 6vw, 64px);
  line-height: 1.05;
  margin: 18px 0;
}

.hero-copy p {
  max-width: 640px;
  font-size: 18px;
  line-height: 1.8;
  color: rgba(235, 241, 255, 0.78);
}

.hero-actions {
  display: flex;
  gap: 14px;
  flex-wrap: wrap;
  margin-top: 28px;
}

.hero-panel {
  padding: 28px;
  background: rgba(8, 16, 40, 0.48);
  border-color: rgba(133, 158, 255, 0.18);
  backdrop-filter: blur(12px);
}

.hero-panel-head span {
  color: #8ec8ff;
  font-size: 13px;
  text-transform: uppercase;
  letter-spacing: 0.08em;
}

.hero-panel-head strong {
  display: block;
  margin-top: 8px;
  font-size: 22px;
}

.metric-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 14px;
  margin-top: 20px;
}

.metric-grid article,
.feature-card {
  padding: 18px;
  border-radius: 20px;
  background: rgba(255, 255, 255, 0.06);
}

.metric-grid span,
.feature-card p {
  color: rgba(235, 241, 255, 0.72);
}

.metric-grid strong {
  display: block;
  margin-top: 8px;
  font-size: 18px;
}

.feature-grid {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 20px;
}

.feature-card {
  border: 1px solid rgba(133, 158, 255, 0.14);
}

.feature-card h3 {
  margin: 0 0 10px;
  color: #fff;
}

@media (max-width: 960px) {
  .hero-section,
  .feature-grid {
    grid-template-columns: 1fr;
  }

  .topbar {
    flex-direction: column;
    align-items: flex-start;
  }
}
</style>
