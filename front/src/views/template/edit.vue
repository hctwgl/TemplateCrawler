<template>
  <div class="user-edit-container">
    <el-form autoComplete="on" :model="formData" :rules="loginRules" ref="formData" label-position="left"
             label-width="0px"
             class="card-box login-form">
      <h3 class="title">编辑</h3>
      <el-form-item prop="username">
                <span class="svg-container">
                  <icon-svg icon-class="jiedianyoujian"></icon-svg>
                </span>
        <el-input name="name" type="text" v-model="formData.name" autoComplete="on" placeholder="模板名"></el-input>
      </el-form-item>
      <query-select :api="queryApi"></query-select>
      <el-form-item>
        <el-button type="primary" style="width:100%;" :loading="loading" @click.native.prevent="handleCommit">
          提交
        </el-button>
      </el-form-item>
    </el-form>
    <el-tabs v-model="editPageName" type="card" editable @edit="handleTabsEdit">
      <el-tab-pane v-for="(item, index) in formData.pageStructure" :key="index" :label="item.name" :name="item.index +''">
        <el-form autoComplete="on" :model="formData" :rules="loginRules" ref="formData" label-position="left"
                 label-width="0px" class="card-box login-form">
          <el-form-item prop="username">
                <span class="svg-container">
                  <icon-svg icon-class="jiedianyoujian"></icon-svg>
                </span>
            <el-input name="name" type="text" v-model="item.name" autoComplete="on" placeholder="当前页名称"></el-input>
          </el-form-item>
          <el-form-item prop="username">
                <span class="svg-container">
                  <icon-svg icon-class="jiedianyoujian"></icon-svg>
                </span>
            <el-input name="name" type="text" v-model="item.url" autoComplete="on" placeholder="网页链接"></el-input>
          </el-form-item>
          <div v-for="(field, index) in item.fields">
            <el-form :label-position="right" label-width="80px" :model="field">
              <el-form-item label="名称">
                <el-input v-model="field.name"></el-input>
              </el-form-item>
              <el-form-item label="标签">
                <el-input v-model="field.label"></el-input>
              </el-form-item>
              <el-form-item label="类型">
                <el-input v-model="field.type"></el-input>
              </el-form-item>
              <el-form-item label="默认值">
                <el-input v-model="field.defaultValue"></el-input>
              </el-form-item>
              <el-form-item label="Xpath">
                <el-input v-model="field.xpath"></el-input>
              </el-form-item>
              <el-form-item label="多选">
                <el-input v-model="field.multiple"></el-input>
              </el-form-item>
            </el-form>
          </div>
        </el-form>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
  import {validatAlphabetsAndNum} from '@/utils/validate';
  import api from '@/api/template';
  import queryApi from '@/api/website';
  import querySelect from '@/components/querySelect';

  export default {
    name: 'login',
    computed: {
    },
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
        formData: {
          username: '',
          password: '',
          authorities: null
        },
        loginRules: {
          domain: [
            {required: true, trigger: 'blur', validator: validatePass}
          ]
        },
        loading: false,
        authorities: [],
        id: null,
        queryApi,
        editPageName: '2',
//        editPage: [{
//          title: 'Tab 1',
//          name: '1',
//          content: 'Tab 1 content'
//        }, {
//          title: 'Tab 2',
//          name: '2',
//          content: 'Tab 2 content'
//        }],
        tabIndex: 0
      }
    },
    created() {
      console.log(querySelect);
      this.fetchData();
    },
    methods: {
      fetchData() {
        const id = this.$route.params.id;
        this.id = id;
        if (id) {
          api.get(id).then(response => {
            this.formData = response.data
          });
        }
        console.log('edit id : ' + id);
      },
      handleCommit() {
        this.$refs.formData.validate(valid => {
          if (valid) {
            this.loading = true;
            if (this.id) {
              api.edit(this.formData).then(response => {
                //            this.authorities = response.data.content;
                this.loading = false;
                this.$router.push({path: '/template/page'});
              }).catch(() => {
                this.loading = false;
              });
            } else {
              api.add(this.formData).then(response => {
                //            this.authorities = response.data.content;
                this.loading = false;
                this.$router.push({path: '/template/page'});
              }).catch(() => {
                this.loading = false;
              });
            }
//            this.$store.dispatch('Login', this.formData).then(() => {
//              this.loading = false;
//              this.$router.push({ path: '/' });
//            }).catch(() => {
//            });
          } else {
            console.log('error submit!!');
            return false;
          }
        });
      },
      handleTabsEdit(targetName, action) {
        if (action === 'add') {
          if (this.formData.pageStructure === null) {
            this.formData.pageStructure = []
          }
          const newIndex = ++this.tabIndex;
          const newTabName = newIndex + '';
          this.formData.pageStructure.push({
            title: '未命名',
            name: '未命名',
            url: 'xxx',
            index: newTabName,
            content: 'New Tab content'
          });
          this.editPageName = newTabName;
        }
        if (action === 'remove') {
          const tabs = this.formData.pageStructure;
          let activeName = this.editPageName;
          if (activeName === targetName) {
            tabs.forEach((tab, index) => {
              if (tab.index === targetName) {
                const nextTab = tabs[index + 1] || tabs[index - 1];
                if (nextTab) {
                  activeName = nextTab.index;
                }
              }
            });
          }

          this.editPageName = activeName;
          this.formData.pageStructure = tabs.filter(tab => tab.index !== targetName);
        }
      }
    },
    components: {
      querySelect
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
