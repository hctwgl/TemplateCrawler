<template>
  <div class="login-container">
    <el-form autoComplete="on" :model="loginForm" ref="loginForm" label-position="left" label-width="0px" class="card-box login-form">
      <!--<el-form autoComplete="on" :model="loginForm" :rules="loginRules" ref="loginForm" label-position="left" label-width="0px"-->
      <!--class="card-box login-form">-->
      <h3 class="title">系统登录</h3>
      <el-form-item prop="email">
        <el-input name="email" type="text" v-model="loginForm.email" autoComplete="on" placeholder="邮箱"></el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input name="password" type="password" @keyup.enter.native="handleLogin" v-model="loginForm.password" autoComplete="on" placeholder="密码"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" style="width:100%" :loading="loading" @click.native.prevent="handleLogin">
          登录
        </el-button>
      </el-form-item>
      <!--<div class='tips'>admin账号为:admin@wallstreetcn.com 密码随便填</div>-->
      <!--<div class='tips'>editor账号:editor@wallstreetcn.com 密码随便填</div>-->
    </el-form>
  </div>
</template>
<script>
export default {
  name: 'login',
  data () {
    const validateEmail = (rule, value, callback) => {
      //   if (!isWscnEmail(value)) {
      //     callback(new Error('请输入正确的合法邮箱'))
      //   } else {
      //     callback()
      //   }
      callback()
    }
    const validatePass = (rule, value, callback) => {
      if (value.length < 6) {
        callback(new Error('密码不能小于6位'))
      } else {
        callback()
      }
    }
    return {
      loginForm: {
        email: 'admin',
        password: '123'
      },
      loginRules: {
        email: [
          { required: true, trigger: 'blur', validator: validateEmail }
        ],
        password: [
          { required: true, trigger: 'blur', validator: validatePass }
        ]
      },
      loading: false
    }
  },
  methods: {
    handleLogin () {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true
          this.$store.dispatch('Login', this.loginForm).then(() => {
            this.loading = false
            this.$router.push({ path: '/' })
          }).catch(() => {
            this.loading = false
          })
        } else {
          console.log('error submit!!')
          return false
        }
      })
    }
  }
}
</script>

<style>

</style>


