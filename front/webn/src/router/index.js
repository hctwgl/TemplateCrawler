import Vue from 'vue'
import Router from 'vue-router'
// import HelloWorld from '@/components/HelloWorld'
import Login from '@/views/login/Login'
import DashBoard from '@/views/dashboard/DashBoard'
import WebsiteEdit from '@/views/website/Edit'
import WebsiteList from '@/views/website/List'
import UserEdit from '@/views/user/Edit'
import UserList from '@/views/user/List'
import TaskEdit from '@/views/task/Edit'
import TaskList from '@/views/task/List'
import TemplateEdit from '@/views/template/Edit'
import TemplateList from '@/views/template/List'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: Login
    },
    {
      path: '/',
      name: 'dashBoard',
      component: DashBoard,
      children: [
        {
          path: 'website',
          name: 'websiteList',
          component: WebsiteList
        },
        {
          path: 'website/edit',
          name: 'websiteAdd',
          component: WebsiteEdit
        },
        {
          path: 'website/edit/:id',
          name: 'websiteEdit',
          component: WebsiteEdit
        },
        {
          path: 'user',
          name: 'userList',
          component: UserList
        },
        {
          path: 'user/edit',
          name: 'userAdd',
          component: UserEdit
        },
        {
          path: 'user/edit/:id',
          name: 'userEdit',
          component: UserEdit
        },
        {
          path: 'template',
          name: 'templateList',
          component: TemplateList
        },
        {
          path: 'template/edit',
          name: 'templateAdd',
          component: TemplateEdit
        },
        {
          path: 'template/edit/:id',
          name: 'templateEdit',
          component: TemplateEdit
        },
        {
          path: 'task',
          name: 'taskList',
          component: TaskList
        },
        {
          path: 'task/edit',
          name: 'taskAdd',
          component: TaskEdit
        },
        {
          path: 'task/edit/:id',
          name: 'taskEdit',
          component: TaskEdit
        }
      ]
    }
  ]
})
