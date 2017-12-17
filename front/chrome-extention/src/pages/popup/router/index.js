import Vue from 'vue'
import Router from 'vue-router'
import ConfigUrl from '../pages/ConfigUrl.vue'
import Login from '../pages/Login.vue'
import Website from '../pages/Website.vue'
import Template from '../pages/Template.vue'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      redirect: 'ConfigUrl'
    },
    {
      path: '/login',
      name: 'login',
      component: Login
    },
    {
      path: '/configUrl',
      name: 'configUrl',
      component: ConfigUrl
    },
    {
      path: '/website',
      name: 'website',
      component: Website
    },
    {
      path: '/template',
      name: 'template',
      component: Template
    }
  ]
})
