<template>
  <div id="dashboard">
    <el-container class="main-container">
      <el-aside width="200px" style="background-color: rgb(238, 241, 246)">
        <el-menu :default-openeds="['2', '3']">
          <el-submenu index="1">
            <template slot="title">
              <i class="el-icon-menu"></i>网站管理</template>
            <router-link :to="'/website'">
              <el-menu-item index="1-1">网站列表</el-menu-item>
            </router-link>
            <router-link :to="'/website/edit'">
              <el-menu-item index="1-2">新增网站</el-menu-item>
            </router-link>
          </el-submenu>
          <el-submenu index="3">
            <template slot="title">
              <i class="el-icon-menu"></i>模板管理</template>
            <router-link :to="'/template'">
              <el-menu-item index="3-1">模板列表</el-menu-item>
            </router-link>
            <router-link :to="'/template/edit'">
              <el-menu-item index="3-2">新增模板</el-menu-item>
            </router-link>
          </el-submenu>
          <el-submenu index="2">
            <template slot="title">
              <i class="el-icon-menu"></i>任务管理</template>
            <router-link :to="'/task'">
              <el-menu-item index="2-1">任务列表</el-menu-item>
            </router-link>
            <router-link :to="'/task/edit'">
              <el-menu-item index="2-2">新增任务</el-menu-item>
            </router-link>
          </el-submenu>
          <el-submenu index="3">
            <template slot="title">
              <i class="el-icon-setting"></i>任务管理</template>
            <el-menu-item index="3-1">任务列表</el-menu-item>
            <!-- <el-menu-item-group>
              <template slot="title">分组一</template>
              <el-menu-item index="3-3">选项3</el-menu-item>
            </el-menu-item-group> -->
            <!-- <el-menu-item-group title="分组2">
              <el-menu-item index="3-4">选项4</el-menu-item>
            </el-menu-item-group> -->
          </el-submenu>
          <el-submenu index="4">
            <template slot="title">
              <i class="el-icon-setting"></i>管理</template>
            <el-menu-item-group>
              <template slot="title">用户管理</template>
              <router-link :to="'/user'">
                <el-menu-item index="4-1">用户列表</el-menu-item>
              </router-link>
              <router-link :to="'/user/edit'">
                <el-menu-item index="4-2">添加用户</el-menu-item>
              </router-link>
            </el-menu-item-group>
          </el-submenu>
        </el-menu>
      </el-aside>

      <el-container>
        <el-header style="text-align: right; font-size: 12px">
          <el-dropdown>
            <i class="el-icon-setting" style="margin-right: 15px"></i>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>查看</el-dropdown-item>
              <el-dropdown-item>新增</el-dropdown-item>
              <el-dropdown-item>删除</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
          <span>王小虎</span>
        </el-header>

        <el-main>
          <!-- <img src="../../assets/logo.png"> -->
          <router-view/>
        </el-main>
      </el-container>
    </el-container>

  </div>
</template>

<script>
export default {
  name: 'dashboard'
}
</script>

<style>
html {
  height: 100%;
}
body {
  margin: 0;
  padding: 0;
  height: 100%;
}

#dashboard {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  height: 100%;
}
.main-container {
  height: 100%;
  border: 1px solid #eee;
}
</style>
