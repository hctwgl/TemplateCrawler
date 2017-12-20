<template>
  <div class="user-edit-container">
    <el-form autoComplete="on" :model="formData" :rules="loginRules" ref="formData" :label-position="'top'" class="edit-form">
      <h3 class="title">编辑</h3>
      <el-form-item label="任务名">
        <el-input name="name" type="text" v-model="formData.name" autoComplete="on" placeholder="任务名"></el-input>
      </el-form-item>
      <el-form-item label="搜索">
        <!-- <query-select :api="templateApi" v-on:change="handleTemplateChange" ref="templateSelect"></query-select> -->
        <el-select v-model="formData.templateId" filterable remote reserve-keyword placeholder="请输入模板名称" :remote-method="searchTemplate" :loading="loading">
          <el-option v-for="item in searchTemplates" :key="item.id" :label="item.name" :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="周期">
        <el-input name="domain" type="number" v-model="formData.cycle" placeholder="周期"></el-input>
      </el-form-item>
      <el-form-item label="线程数">
        <el-input name="domain" type="number" v-model="formData.threadNum" placeholder="线程数"></el-input>
      </el-form-item>
      <el-form-item label="起始 Urls">
        <el-input name="startUrls" type="textarea" v-model="formData.startUrls" placeholder="起始Urls, 用,分隔"></el-input>
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
import api from '@/api/task'
import templateApi from '@/api/template'
// import querySelect from '@/components/querySelect'

export default {
  name: 'login',
  data () {
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
      id: null,
      templateApi,
      searchTemplates: []
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
          if (this.formData.templateId) {
            templateApi.get(this.formData.templateId).then(response => {
              this.searchTemplates.push(response.data)
            })
          }
        })
      }
      console.log('edit id : ' + id)
    },
    handleCommit () {
      this.$refs.formData.validate(valid => {
        if (valid) {
          this.loading = true
          this.formData.startUrlList = this.formData.startUrls.split('\n')
          if (this.id) {
            api.edit(this.formData).then(response => {
              //            this.authorities = response.data.content
              this.loading = false
              this.$router.push({ path: '/task' })
            }).catch(() => {
              this.loading = false
            })
          } else {
            api.add(this.formData).then(response => {
              //            this.authorities = response.data.content
              this.loading = false
              this.$router.push({ path: '/task' })
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
    },
    handleTemplateChange (data) {
      console.log(data)
      this.formData.templateId = data.id
    },
    searchTemplate (query) {
      if (query !== '') {
        // this.loading = true;
        this.templateApi.query(query, 'name').then(response => {
          this.searchTemplates = response.data.content
        })
        // setTimeout(() => {
        //   this.loading = false;
        //   this.options4 = this.list.filter(item => {
        //     return item.label.toLowerCase()
        //       .indexOf(query.toLowerCase()) > -1;
        //   });
        // }, 200);
      } else {
        this.searchTemplates = []
      }
    }
  },
  components: {
    // querySelect
  }
}
</script>
