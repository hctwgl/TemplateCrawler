<template>
  <div id="app">
    <div>
    </div>
    <h3>页面名：{{page.name}}</h3>
    <div id="fileds">
      <el-form label-position="right" :inline-message="true" status-icon v-for="(item,index) in page.fields" :key="index" :model="item" :rules="rules" label-width="60px" size="mini">
        <el-form-item label="名称">
          <el-input v-model="item.label"></el-input>
          <el-button @click.prevent="removeField(index)">删除</el-button>
        </el-form-item>
        <el-form-item label="类型" prop="type">
          <el-select v-model="item.type" placeholder="请选择" size='mini'>
            <el-option v-for="item in typeOptions" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="XPATH" prop="xpath">
          <el-input v-model="item.xpath">
            <el-button slot="append" icon="el-icon-location-outline" @click="chooseXpath(item)"></el-button>
            <el-button slot="append" icon="el-icon-search" @mouseout.native="cancelLocation" @mouseover.native="locationByXpath(item.xpath)"></el-button>
          </el-input>
        </el-form-item>
        <!-- <el-form-item label="名称">
                                                                                                        <el-input v-model="item.label"></el-input>
                                                                                                      </el-form-item> -->
        <el-form-item label="默认值">
          <el-input v-model="item.defaultValue"></el-input>
        </el-form-item>
        <el-form-item label="多值" prop="delivery">
          <el-switch v-model="item.multiple"></el-switch>
        </el-form-item>
        <hr>
      </el-form>
      <div>
        <el-button type="primary" @click="addNewFiled">添加新字段</el-button>
        <el-button type="success" @click="submit">完成提交</el-button>
      </div>
    </div>
  </div>
</template>

<script>
/* global chrome:true */
/* eslint no-undef: "error" */
import chromeUtil from '@/utils/chromeUtil'

let messageProcess
function initChromeMessage() {
  // message 处理
  messageProcess = {
    name: 'iframe',
    actions: {
      setXpath(sender, message, sendResponse) {
        console.log('get action choose dom')
        status.startChooseDom()
      }
    },
    accept(sender, message, sendResponse) {
      if (message.target !== this.name) {
        return
      }
      const action = this.actions[message.action]
      if (action) {
        action(sender, message, sendResponse)
      } else {
        console.log('not handle action : ' + message.action)
      }
    },
    sendMessage(tabId, message, options, responseCallback) {
      chrome.tabs.sendMessage(tabId, message, responseCallback)
    }
  }
  chrome.runtime.onMessage.addListener(function (
    requestData,
    sender,
    sendResponse
  ) {
    console.log(sender.tab ? '来自内容脚本：' + sender.tab.url : '来自扩展程序')
    console.log(requestData)
    messageProcess.accept(sender, requestData, sendResponse)
  })
}
function checkXpath(rule, value, callback) {
  console.log(value)
  if (!value) {
    return callback(new Error('年龄不能为空'))
  }
  return callback()
}

export default {
  name: 'app',
  data() {
    return {
      page: {
        fields: []
      },
      curXpathField: null,
      newField: {
        label: '',
        type: '',
        xpath: '',
        defaultValue: '',
        multiple: false
      },
      typeOptions: [
        {
          value: 'HTML_TEXT',
          label: 'HTML文本'
        },
        {
          value: 'PURE_TEXT',
          label: '纯文本'
        },
        {
          value: 'TARGET_LINK',
          label: '目标链接'
        },
        {
          value: 'NEXT_LINK',
          label: '下一页链接'
        },
        {
          value: 'TEXT_LINK',
          label: '文本链接'
        },
        {
          value: 'DOWNLOAD_LINK',
          label: '下载链接'
        }
      ],
      rules: {
        type: [
          { validator: checkXpath, trigger: 'blur' }
        ],
        xpath: [
          { validator: checkXpath, trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    const that = this
    if (process.env.NODE_ENV === 'production') {
      initChromeMessage()
      messageProcess.actions = {
        setXpath(sender, message, sendResponse) {
          console.log('get xpath ' + message.msg)
          that.curXpathField.xpath = message.msg
        }
      }
      chromeUtil.sendMessage({ target: 'background', action: 'getCurPage' }, function (page) {
        console.log(page)
        if (page.fields === null || page.fields === undefined) {
          page.fields = []
        }
        that.page = page
      })
    } else {
      that.page = { fields: [] }
    }
  },
  methods: {
    chooseXpath(filed) {
      console.log('xxxxx')
      this.curXpathField = filed
      chromeUtil.sendMessage({ target: 'router-content', action: 'chooseDom' })
    },
    locationByXpath(xpath) {
      if (xpath.length > 0) {
        chromeUtil.sendMessage({ target: 'router-content', action: 'locationXpath', msg: xpath })
      }
    },
    addNewFiled() {
      var newFiled = JSON.parse(JSON.stringify(this.newField))
      newFiled.label = '字段' + (this.page.fields.length + 1)
      this.page.fields.push(newFiled)
    },
    cancelLocation() {
      chromeUtil.sendMessage({ target: 'router-content', action: 'cancelLocation' })
    },
    submit() {
      if (this.page.fields.length === 0) {
        this.$message.error('没有添加字段')
      } else {
        for (var index = 0; index < this.page.fields.length; index++) {
          var field = this.page.fields[index]
          if (field.xpath === '') {
            this.$message.error('字段：' + field.label + ' 的 xpath 为空')
            break
          } else if (field.type === '') {
            this.$message.error('字段：' + field.label + ' 的 类型 没有选择')
            break
          }
        }
        chromeUtil.sendMessage({ target: 'background', action: 'submitPage', msg: JSON.parse(JSON.stringify(this.page)) })
      }
    },
    removeField(index) {
      this.page.fields.splice(index, 1)
    }
  }
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
  margin-top: 10px;
  /* width: 300px; */
  /* background-color: #c0c0c0; */
  height: 100%;
  border: 1px solid #eee;
  border-radius: 6px;
  margin-bottom: 20px;
  position: relative;
  transition: all 0.2s ease-in-out;
  /* padding: 20px; */
}
#fileds .el-form-item--mini.el-form-item,
.el-form-item--small.el-form-item {
  margin-bottom: 3px;
}
.el-popper[x-placement^='bottom'] {
  margin-top: 4px;
}
.el-select-dropdown__item {
  font-size: 12px;
  height: 20px;
  line-height: 20px;
}
.el-select-dropdown__item span {
  line-height: 20px !important;
}
</style>
