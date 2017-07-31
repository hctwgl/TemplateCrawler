import Vue from 'vue';
import Router from 'vue-router';
const _import = require('./_import_' + process.env.NODE_ENV);
// in development env not use Lazy Loading,because Lazy Loading large page will cause webpack hot update too slow.so only in production use Lazy Loading

/* layout */
import Layout from '../views/layout/Layout';

/* login */
const Login = _import('login/index');

/* dashboard */
const dashboard = _import('dashboard/index');

/* error page */
const Err404 = _import('404');

/* demo page */
const Form = _import('page/form');
const Table = _import('table/index');
const UserList = _import('user/list');
const UserEdit = _import('user/edit');
const WebsitePage = _import('website/page');
const WebsiteEdit = _import('website/edit');

Vue.use(Router);

 /**
  * icon : the icon show in the sidebar
  * hidden : if `hidden:true` will not show in the sidebar
  * redirect : if `redirect:noredirect` will not redirct in the levelbar
  * noDropdown : if `noDropdown:true` will not has submenu in the sidebar
  * meta : `{ role: ['admin'] }`  will control the page role
  **/
export const constantRouterMap = [
  { path: '/login', component: Login, hidden: true },
  { path: '/404', component: Err404, hidden: true },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    name: 'Home',
    hidden: true,
    children: [{ path: 'dashboard', component: dashboard }]
  }
]

export default new Router({
  // mode: 'history', //后端支持可开
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRouterMap
});

export const asyncRouterMap = [
  {
    path: '/example',
    component: Layout,
    redirect: 'noredirect',
    name: 'Example',
    icon: 'zujian',
    children: [
      { path: 'index', component: Form, name: 'Form', icon: 'zonghe' }
    ]
  },
  {
    path: '/table',
    component: Layout,
    redirect: '/table/index',
    name: 'Table',
    icon: 'tubiaoleixingzhengchang',
    noDropdown: true,
    children: [{ path: 'index', component: Table, name: 'TableIndex', meta: { role: ['admin'] } }]
  },
  { path: '*', redirect: '/404', hidden: true },
  {
    path: '/user',
    component: Layout,
    redirect: '/user/list',
    name: 'User',
    icon: 'tubiaoleixingzhengchang',
    // noDropdown: true,
    meta: { role: ['admin'] },
    children: [
      { path: 'list', component: UserList, name: 'userList', meta: { role: ['admin'] } },
      { path: 'edit', component: UserEdit, name: 'userEdit', meta: { role: ['admin'] } }
    ]
  },
  {
    path: '/website',
    component: Layout,
    redirect: '/website/page',
    name: 'Website',
    icon: 'zujian',
    children: [
      { path: 'page', component: WebsitePage, name: 'websitePage', icon: 'zonghe' },
      { path: 'edit', component: WebsiteEdit, name: 'websiteEditNew', icon: 'zonghe', hidden: true },
      { path: 'edit/:id', component: WebsiteEdit, name: 'websiteEdit', icon: 'zonghe', hidden: true }
    ]
  },
];
