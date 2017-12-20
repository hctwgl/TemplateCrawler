<template>
  <div class="user-edit-container">
    <el-form autoComplete="on" :model="formData" :rules="loginRules" ref="formData" label-position="right" label-width="80px" class="card-box login-form">
      <h3 class="title">编辑</h3>
      <el-form-item prop="username" label="模板名">
        <el-input name="name" type="text" v-model="formData.name" autoComplete="on" placeholder="模板名"></el-input>
      </el-form-item>
      <el-form-item prop="username" label="网站">
        <!-- <query-select :api="websiteApi" v-on:change="handleWebsiteChange" ref="websiteSelect"></query-select> -->
      </el-form-item>
      <el-form-item>
        <el-button type="primary" style="width:100%" :loading="loading" @click.native.prevent="handleCommit">
          提交
        </el-button>
      </el-form-item>
    </el-form>
    <el-tabs class="page-template-edit" v-model="editPageName" type="card" editable @edit="handleTabsEdit">
      <el-tab-pane v-for="(item, index) in formData.pageStructure" :key="index" :label="item.name" :name="item.index +''">
        <el-form :label-position="'right'" label-width="80px">
          <el-form-item label="网页名称">
            <el-input name="name" type="text" v-model="item.name" autoComplete="on" placeholder="当前页名称"></el-input>
          </el-form-item>
          <el-form-item label="网页链接">
            <el-input name="name" type="text" v-model="item.url" autoComplete="on" placeholder="网页链接"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" style="width:100%" :loading="loading" @click.native.prevent="handleAddField(item)">
              添加字段
            </el-button>
          </el-form-item>
          <div v-for="(field) in item.fields" :key="field.name">
            <el-form :label-position="'right'" label-width="80px" :model="field">
              <el-form-item label="属性名">
                <el-input v-model="field.name"></el-input>
              </el-form-item>
              <el-form-item label="显示名">
                <el-input v-model="field.label"></el-input>
              </el-form-item>
              <el-form-item label="类型">
                <el-select v-model="field.type" placeholder="请选择">
                  <el-option v-for="type in fieldType" :key="type.value" :label="type.label" :value="type.value">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="默认值">
                <el-input v-model="field.defaultValue"></el-input>
              </el-form-item>
              <el-form-item label="Xpath">
                <el-input v-model="field.xpath"></el-input>
              </el-form-item>
              <el-form-item label="多选">
                <el-checkbox v-model="field.multiple"></el-checkbox>
              </el-form-item>
            </el-form>
          </div>
        </el-form>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import Vue from 'vue'
import api from '@/api/template'
import websiteApi from '@/api/website'

export default {
  name: 'template-edit',
  computed: {
  },
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
      websiteApi,
      editPageName: '0',
      tabIndex: 1,
      fieldType: [
        {
          value: 'HTML_TEXT',
          label: '文本'
        },
        {
          value: 'NEXT_LEVEL_LINK',
          label: '跳转链接'
        },
        {
          value: 'NEXT_PAGE_LINK',
          label: '下一页链接'
        },
        {
          value: 'TEXT_LINK',
          label: '抓取的链接文本'
        },
        {
          value: 'PURE_TEXT',
          label: '抓取的链接文本'
        },
        {
          value: 'DOWNLOAD_LINK',
          label: '下载链接'
        }
      ]
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
          if (this.formData.pageStructure) {
            this.formData.pageStructure.forEach((page, index) => {
              page.index = index
            })
          }
          if (this.formData.websiteId) {
            websiteApi.get(this.formData.websiteId).then(response => {
              this.$refs.websiteSelect.setData(response.data)
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
          if (this.id) {
            api.edit(this.formData).then(response => {
              //            this.authorities = response.data.content
              this.loading = false
              this.$router.push({ path: '/template/page' })
            }).catch(() => {
              this.loading = false
            })
          } else {
            api.add(this.formData).then(response => {
              //            this.authorities = response.data.content
              this.loading = false
              this.$router.push({ path: '/template/page' })
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
    handleWebsiteChange (data) {
      this.formData.websiteId = data.id
    },
    handleTabsEdit (targetName, action) {
      if (action === 'add') {
        if (this.formData.pageStructure === null) {
          this.formData.pageStructure = []
        }
        const newIndex = ++this.tabIndex
        const newTabName = newIndex + ''
        this.formData.pageStructure.push({
          title: '未命名',
          name: '未命名',
          url: 'xxx',
          index: newTabName,
          content: 'New Tab content'
        })
        this.editPageName = newTabName
      }
      if (action === 'remove') {
        const tabs = this.formData.pageStructure
        let activeName = this.editPageName
        if (activeName === targetName) {
          tabs.forEach((tab, index) => {
            if (tab.index === targetName) {
              const nextTab = tabs[index + 1] || tabs[index - 1]
              if (nextTab) {
                activeName = nextTab.index
              }
            }
          })
        }

        this.editPageName = activeName
        this.formData.pageStructure = tabs.filter(tab => tab.index !== targetName)
      }
    },
    handleAddField: pageTemplate => {
      if (pageTemplate.fields === undefined || pageTemplate.fields === null) {
        Vue.set(pageTemplate, 'fields', [])
      }
      pageTemplate.fields.push({ type: '' })
    }
  },
  components: {
  }
}
</script>
