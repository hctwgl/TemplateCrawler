<template>
    <div class="user-edit-container">
        <el-form autoComplete="on" :model="userForm" :rules="loginRules" ref="userForm" label-position="left" label-width="0px"
        class="card-box login-form">
            <h3 class="title">编辑</h3>
            <el-form-item prop="username">
        <span class="svg-container">
                  <icon-svg icon-class="jiedianyoujian"></icon-svg>
                </span>
                <el-input name="username" type="text" v-model="userForm.username" autoComplete="on" placeholder="用户名"></el-input>
            </el-form-item>
            <el-form-item prop="password">
        <span class="svg-container">
                  <icon-svg icon-class="mima" ></icon-svg>
                </span>
                <el-input name="password" type="password" @keyup.enter.native="handleCommit" v-model="userForm.password" autoComplete="on"
                          placeholder="密码"></el-input>
            </el-form-item>
            <el-form-item>
        <span class="svg-container">
                  <icon-svg icon-class="jiedianyoujian" ></icon-svg>
                </span>
                <el-select v-model="userForm.authorities" multiple placeholder="请选择">
                    <el-option
                            v-for="item in authorities"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" style="width:100%;" :loading="loading" @click.native.prevent="handleCommit">
                    提交
                </el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<script>
  import { validatAlphabetsAndNum } from '@/utils/validate';
  import { list as authorityList } from '@/api/authority';
  import { create } from '@/api/user';

  export default {
    name: 'login',
    data() {
      const validateUsername = (rule, value, callback) => {
        if (!validatAlphabetsAndNum(value)) {
          callback(new Error('请输入正确的合法邮箱'));
        } else if (value.length < 4) {
          callback(new Error('用户名长度不能小于4'));
        } else {
          callback();
        }
      };
      const validatePass = (rule, value, callback) => {
        if (value.length < 6) {
          callback(new Error('密码不能小于6位'));
        } else {
          callback();
        }
      };
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
    created() {
      this.fetchData();
    },
    methods: {
      fetchData() {
        authorityList().then(response => {
          this.authorities = response.data.content;
          this.loading = false;
        }).catch(() => {
          this.loading = false;
        });
      },
      handleCommit() {
        this.$refs.userForm.validate(valid => {
          if (valid) {
            this.loading = true;
            create(this.userForm).then(response => {
//              this.authorities = response.data.content;
              this.loading = false;
              this.$router.push({ path: '/user/list' });
            }).catch(() => {
              this.loading = false;
            });
//            this.$store.dispatch('Login', this.userForm).then(() => {
//              this.loading = false;
//              this.$router.push({ path: '/' });
//            }).catch(() => {
//            });
          } else {
            console.log('error submit!!');
            return false;
          }
        });
      }
    }
  }
</script>

<style rel="stylesheet/scss" lang="scss">
    /*@import "src/styles/mixin.scss";*/
    .tips {
        font-size: 14px;
        color: #fff;
        margin-bottom: 5px;
    }

    .user-edit-container {
    /*@include relative;*/
        /*height: 100vh;*/
        /*background-color: #2d3a4b;*/
    /*input:-webkit-autofill {*/
        /*-webkit-box-shadow: 0 0 0px 1000px #293444 inset !important;*/
        /*-webkit-text-fill-color: #fff !important;*/
    /*}*/
    input {
        /*background: transparent;*/
        /*border: 0px;*/
        /*-webkit-appearance: none;*/
        /*border-radius: 0px;*/
        padding: 12px 5px 12px 15px;
        /*color: #eeeeee;*/
        /*height: 47px;*/
    }
    .el-input {
        display: inline-block;
        height: 47px;
        width: 85%;
    }
    .el-select {
        display: inline-block;
        height: 47px;
        width: 85%;
    }
    .el-select .el-input {
        width: 100%;
    }
    .svg-container {
        padding: 6px 5px 6px 15px;
        color: #889aa4;
    }
    /*.title {*/
        /*font-size: 26px;*/
        /*font-weight: 400;*/
        /*color: #eeeeee;*/
        /*margin: 0px auto 40px auto;*/
        /*text-align: center;*/
        /*font-weight: bold;*/
    /*}*/
    .login-form {
        /*position: absolute;*/
        left: 0;
        right: 0;
        width: 400px;
        padding: 35px 35px 15px 35px;
        margin: 120px auto;
    }
    .el-form-item {
        /*border: 1px solid rgba(255, 255, 255, 0.1);*/
        /*background: rgba(0, 0, 0, 0.1);*/
        /*border-radius: 5px;*/
        /*color: #454545;*/
    }
    .forget-pwd {
        color: #fff;
    }
    }
</style>
