import { createStore } from 'vuex'
import user from './modules/user'
import tabs from './modules/tabs'
import resume from './modules/resume'
import job from './modules/job'
import guide from './modules/guide'
import common from './modules/common'

export default createStore({
  modules: { user, tabs, resume, job, guide, common }
})
