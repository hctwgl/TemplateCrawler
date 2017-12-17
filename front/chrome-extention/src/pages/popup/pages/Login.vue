<template>
  <div id="loginForm">
    <h3>登录模板服务器</h3>
    <el-form ref="form" :model="loginForm" label-position="top" label-width="80px">
      <el-form-item>
        <el-input v-model="loginForm.username" size="small" placeholder="用户名"></el-input>
      </el-form-item>
      <el-form-item>
        <el-input type="password" v-model="loginForm.password" size="small" placeholder="密码"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button id="loginBtn" type="primary" size="small" @click="login">登录</el-button>
        <!-- <el-button @click="resetForm">重置</el-button> -->
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import global from '../global.js'
import { setToken } from '@/utils/token.js'

export default {
  name: 'Login',
  created() {
    this.refreshToken()
  },
  data() {
    return {
      loginForm: {
        username: 'admin',
        password: '123'
      }
    }
  },
  methods: {
    refreshToken() {
      global.getFetch()
        .get('/auth/refresh')
        .then(Response => {
          console.log(Response)
          if (Response.status === 401) {
          }
          const data = Response.data
          if (data) {
            setToken(data.token)
            this.$router.push('/website')
          }
        })
        .catch(function (error) {
          console.log(error)
        })
    },
    login() {
      global.getFetch()
        .post(
        '/auth/auth?username=' +
        this.loginForm.username +
        '&password=' +
        this.loginForm.password
        )
        .then(Response => {
          console.log(Response)
          if (Response.status === 401) {
          }
          const data = Response.data
          if (data) {
            setToken(data.token)
            this.$router.push('/website')
          }
        })
        .catch(function (error) {
          console.log(error)
        })
    },
    resetForm() { }
  }
}
</script>

<style lang="scss">
h3 {
  text-align: center;
}
#loginForm {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
  width: 250px;
  margin: 30px auto 0px auto;
}
#loginBtn {
  width: 100%;
}
</style>
