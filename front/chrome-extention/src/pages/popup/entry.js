/**
 * Created by may on 23/10/2017.
 */
import Vue from 'vue'
import router from './router'
import App from './App'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import chromeUtil from '@/utils/chromeUtil'

Vue.config.productionTip = false
Vue.use(ElementUI, { size: 'small' })

/* eslint-disable no-new */
function createVue() {
  new Vue({
    el: '#app',
    router,
    render: c => c(App),
    created() {}
  })
}
if (process.env.NODE_ENV === 'production') {
  chromeUtil.init().then(() => {
    createVue()
  })
} else {
  createVue()
}
