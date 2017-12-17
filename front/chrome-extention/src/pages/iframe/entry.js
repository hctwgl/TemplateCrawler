/**
 * Created by may on 23/10/2017.
 */
import Vue from 'vue'
// import router from './router'
import App from './App'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'

Vue.config.productionTip = false
Vue.use(ElementUI, { size: 'mini' })

/* eslint-disable no-new */
new Vue({
  el: '#app',
  // router,
  render: c => c(App)
})
