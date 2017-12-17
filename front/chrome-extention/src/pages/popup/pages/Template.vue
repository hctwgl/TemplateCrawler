<template>
  <div id="template" v-loading="loading">
    <div>当前网站：{{website.name}}</div>
    <div>
      <ul>
        <li v-for="(template,index) in templateList" :key="index">
          <span>{{template.name}}</span>
          <el-button type="primary" @click="editTemplate(template)" size="mini" class="injectBtn">编辑</el-button>
          </el-form>
        </li>
      </ul>
    </div>
    <hr>
    <el-button type="primary" @click="createNew" v-show="!showEditTemplate">新建模板</el-button>
    <div id="curEditTemplate" v-show="showEditTemplate">
      <div v-show="!editTemplateBaseInfo">
        <div>
          <span>名称:{{curEditTemplate.name}}</span>
        </div>
        <div>
          <span>内容起始页:{{curEditTemplate.contentStartLayer}}</span>
        </div>
        <el-button type="primary" @click="editTemplateBaseInfo=true" size="mini">编辑</el-button>
        <el-button type="danger" @click="deleteEditTemplate" size="mini">删除</el-button>
      </div>
      <el-form label-position="right" label-width="60px" size="mini" v-show="editTemplateBaseInfo">
        <el-form-item label="名称">
          <el-input v-model="curEditTemplate.name"></el-input>
        </el-form-item>
        <el-form-item label="内容起始页">
          <el-input-number v-model="curEditTemplate.contentStartLayer"></el-input-number>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitEditTemplateBaseInfo" size="mini">确定</el-button>
        </el-form-item>
      </el-form>
      <div class="pages">
        <ul>
          <li v-for="(page,index) in curEditTemplate.pageStructure" :key="index">
            <span @click="editPageIndex = index" v-show="editPageIndex !== index">{{page.name}}</span>
            <span v-show="editPageIndex === index">
              <el-input class="pageNameInput" v-model="page.name"></el-input>
              <el-button type="success" @click="submitPageName" size="mini">确定</el-button>
            </span>
            <el-button type="primary" @click="inject(index)" size="mini" class="injectBtn">注入</el-button>
            </el-form>
          </li>
        </ul>
        <el-button type="primary" @click="addPage" size="mini">添加页面</el-button>
      </div>
      <!-- <el-button type="primary" @click="saveTemplate" size="mini">保存模板</el-button> -->
    </div>
  </div>
</template>

<script>
import global from '../global.js'
import chrome from '@/utils/chromeUtil'

const newTemplateStruct = {
  name: '',
  pageStructure: [
    {
      name: '第1页'
    }
  ],
  contentStartLayer: 0
}
export default {
  name: 'ConfigWeb',
  data() {
    return {
      website: {},
      exist: false,
      loading: false,
      templateList: [],
      curEditTemplate: {
        name: '',
        pageStructure: [
          {
            name: '第1页'
          }
        ],
        contentStartLayer: 0
      },
      showEditTemplate: false,
      editTemplateBaseInfo: false,
      editPageIndex: -1
    }
  },
  created() {
    if (global.curWebsite) {
      this.website = global.curWebsite
      this.getTemp()
      this.getTemplateList()
    } else {
      this.$router.push('/website')
    }
  },
  methods: {
    getTemplateList() {
      global.getFetch().get('/api/template/website/' + this.website.id + '/page', { params: { page: 0, size: 1000 } }).then(response => {
        console.log(response)
        this.templateList = response.data.content
      })
    },
    editTemplate(template) {
      this.curEditTemplate = template
      this.saveTemp(false)
      this.showEditTemplate = true
      this.editTemplateBaseInfo = true
    },
    createNew() {
      this.showEditTemplate = true
      this.editTemplateBaseInfo = true
      this.curEditTemplate = JSON.parse(JSON.stringify(newTemplateStruct))
      this.curEditTemplate.websiteId = this.website.id
      this.saveTemp()
    },
    submitEditTemplateBaseInfo() {
      this.editTemplateBaseInfo = false
      this.saveTemp()
    },
    submitPageName() {
      this.editPageIndex = -1
      this.saveTemp()
    },
    addPage() {
      this.curEditTemplate.pageStructure.push({ name: '第' + (this.curEditTemplate.pageStructure.length + 1) + '页' })
      this.saveTemp()
    },
    inject(index) {
      // chrome.getCurTabId().then(id => {
      //   chrome.sendMessage(id, {action: 'inject',})
      // })
      chrome.$bg.inject(index, JSON.parse(JSON.stringify(this.curEditTemplate)))
      window.close()
      // chrome.sendMessage('iframe', { target: 'router-content', action: 'chooseDom' })
    },
    getTemp() {
      if (process.env.NODE_ENV === 'production') {
        console.log(chrome.$bg)
        var temp = chrome.$bg.getCurEditTemplate()
        if (temp) {
          this.curEditTemplate = temp
          this.showEditTemplate = true
        }
      } else {
        var temp2 = localStorage.getItem('curEditTemplate')
        if (temp2) {
          this.curEditTemplate = JSON.parse(temp2)
          this.showEditTemplate = true
        }
      }
      console.log(this.curEditTemplate)
    },
    deleteEditTemplate() {
      this.curEditTemplate = {}
      this.showEditTemplate = false
      if (process.env.NODE_ENV === 'production') {
        chrome.$bg.updateCurEditTemplate(null)
      } else {
        localStorage.removeItem('curEditTemplate')
      }
    },
    saveTemp(updateServer) {
      if (process.env.NODE_ENV === 'production') {
        chrome.$bg.updateCurEditTemplate(JSON.parse(JSON.stringify(this.curEditTemplate)), updateServer)
      } else {
        localStorage.setItem('curEditTemplate', JSON.stringify(this.curEditTemplate))
      }
    }
    // saveTemplate() {
    //   if (this.curEditTemplate.id) {
    //     global.getFetch().put('/api/template/edit', JSON.parse(JSON.stringify(this.curEditTemplate))).then(response => {
    //       console.log(response)
    //     })
    //   } else {
    //     global.getFetch().post('/api/template/add', JSON.parse(JSON.stringify(this.curEditTemplate))).then(response => {
    //       console.log(response)
    //     })
    //   }
    // }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h3 {
  text-align: center;
}
#template {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
  width: 100%;
  height: 100%;
  /* width: 250px; */
  /* margin: 30px auto 0px auto; */
}
#setBtn {
  width: 100%;
}
.pageNameInput {
  width: 200px;
}
.injectBtn {
  float: right;
}
</style>
