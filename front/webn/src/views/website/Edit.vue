<template>
  <div class="user-edit-container">
    <el-form autoComplete="on" :model="formData" :rules="loginRules" ref="formData" label-position="left" label-width="0px" class="card-box login-form">
      <h3 class="title">编辑</h3>
      <el-form-item prop="username">
        <el-input name="name" type="text" v-model="formData.name" autoComplete="on" placeholder="网站名"></el-input>
      </el-form-item>
      <el-form-item prop="username">
        <el-input name="domain" type="text" v-model="formData.domain" autoComplete="on" placeholder="域名"></el-input>
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
import api from '@/api/website'

export default {
  name: 'login',
  data () {
    // const validateUsername = (rule, value, callback) => {
    //   if (!validatAlphabetsAndNum(value)) {
    //     callback(new Error('请输入正确的合法邮箱'))
    //   } else if (value.length < 4) {
    //     callback(new Error('用户名长度不能小于4'))
    //   } else {
    //     callback()
    //   }
    // }
    const validatePass = (rule, value, callback) => {
      if (value.length < 6) {
        callback(new Error('密码不能小于6位'))
      } else {
        callback()
      }
    }
    return {
      formData: {
        username: '',
        password: '',
        authorities: null
      },
      loginRules: {
        domain: [
          { required: true, trigger: 'blur', validator: validatePass }
        ]
      },
      loading: false,
      authorities: [],
      id: null
    }
  },
  created () {
    this.fetchData()
  },
  methods: {
    fetchData () {
      const id = this.$route.params.id
      this.id = id
      if (id) {
        api.get(id).then(response => {
          this.formData = response.data
        })
      }
      console.log('edit id : ' + id)
    },
    handleCommit () {
      this.$refs.formData.validate(valid => {
        if (valid) {
          this.loading = true
          if (this.id) {
            api.edit(this.formData).then(response => {
              //            this.authorities = response.data.content
              this.loading = false
              this.$router.push({ path: '/website' })
            }).catch(() => {
              this.loading = false
            })
          } else {
            api.add(this.formData).then(response => {
              //            this.authorities = response.data.content
              this.loading = false
              this.$router.push({ path: '/website' })
            }).catch(() => {
              this.loading = false
            })
          }
          //            this.$store.dispatch('Login', this.formData).then(() => {
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
.tips {
  font-size: 14px;
  color: #fff;
  margin-bottom: 5px；
}

</style>
