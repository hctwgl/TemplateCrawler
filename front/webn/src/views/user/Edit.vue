<template>
  <div class="user-edit-container">
    <el-form autoComplete="on" :model="userForm" :rules="loginRules" ref="userForm" label-position="left" label-width="0px" class="card-box login-form">
      <h3 class="title">编辑</h3>
      <el-form-item prop="username">
        <el-input name="username" type="text" v-model="userForm.username" autoComplete="on" placeholder="用户名"></el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input name="password" type="password" @keyup.enter.native="handleCommit" v-model="userForm.password" autoComplete="on" placeholder="密码"></el-input>
      </el-form-item>
      <el-form-item>
        <el-select v-model="userForm.authorities" multiple placeholder="请选择">
          <el-option v-for="item in authorities" :key="item.id" :label="item.name" :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" style="width:100%" :loading="loading" @click.native.prevent="handleCommit">
          提交
        </el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { validatAlphabetsAndNum } from '@/utils/validate'
import { list as authorityList } from '@/api/authority'
import { create } from '@/api/user'

export default {
  name: 'login',
  data () {
    const validateUsername = (rule, value, callback) => {
      if (!validatAlphabetsAndNum(value)) {
        callback(new Error('请输入正确的合法邮箱'))
      } else if (value.length < 4) {
        callback(new Error('用户名长度不能小于4'))
      } else {
        callback()
      }
    }
    const validatePass = (rule, value, callback) => {
      if (value.length < 6) {
        callback(new Error('密码不能小于6位'))
      } else {
        callback()
      }
    }
    return {
      userForm: {
        username: '',
        password: '',
        authorities: null
      },
      loginRules: {
        username: [
          { required: true, trigger: 'blur', validator: validateUsername }
        ],
        password: [
          { required: true, trigger: 'blur', validator: validatePass }
        ]
      },
      loading: false,
      authorities: []
    }
  },
  created () {
    this.fetchData()
  },
  methods: {
    fetchData () {
      authorityList().then(response => {
        this.authorities = response.data.content
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    handleCommit () {
      this.$refs.userForm.validate(valid => {
        if (valid) {
          this.loading = true
          create(this.userForm).then(response => {
            //              this.authorities = response.data.content
            this.loading = false
            this.$router.push({ path: '/user' })
          }).catch(() => {
            this.loading = false
          })
          //            this.$store.dispatch('Login', this.userForm).then(() => {
          //              this.loading = false
          //              this.$router.push({ path: '/' })
          //            }).catch(() => {
          //            })
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
/*@import "src/styles/mixin.scss"*/
.tips {
  font-size: 14px;
  color: #fff;
  margin-bottom: 5px;
}
</style>
