/* global chrome:true axios:true */
/* eslint no-undef: "error" */
console.log('hello crawler template')
var serverUrl
var token
let curEditTemplate
let curInjectPageIndex
/// ///////////////////////////   事件监听   //////////////////////////////
/*
message : {target: 'abc', action: 'click', msg: ''}
target 可以加 router 前缀 用于 background 转发
messageProcess: {name: 'abc', actions: {
    'click'(sender, message, sendResponse) {},
    'xxx'(message) {}
}}
*/
/* eslint no-unused-vars: "off" */
function getCurEditTemplate() {
  if (curEditTemplate) {
    return JSON.parse(JSON.stringify(curEditTemplate))
  } else {
    return null
  }
}
function updateCurEditTemplate(template, updateServer = true, callbackF) {
  if (template === null) {
    curEditTemplate = null
    if (callbackF) {
      callbackF(false)
    }
    return
  }
  curEditTemplate = JSON.parse(JSON.stringify(template))
  if (updateServer) {
    axios.put('/api/template/edit', curEditTemplate, {
      baseURL: serverUrl,
      headers: {'Authorization': token}
    }).then(response => {
      if (response.status === 200) {
        curEditTemplate = response.data
        callbackF(true)
      } else {
        callbackF(false)
      }
    }).catch((error) => {
      callbackF(false)
      console.log(error)
    })
  }
}
function sendMessage(tabId, message, options, responseCallback) {
  chrome.tabs.sendMessage(tabId, message, responseCallback)
}
function injectTemplate(tabId) {
  chrome.tabs.insertCSS(tabId, { file: '/inject.css' })
  chrome.tabs.executeScript(tabId, { file: '/jquery.js' })
  chrome.tabs.executeScript(tabId, { file: '/xpath.js' })
  chrome.tabs.executeScript(tabId, { file: '/content.js' })
}
function inject(pageIndex, template) {
  curEditTemplate = template
  curInjectPageIndex = pageIndex
  console.log('cur inject page index : ' + curInjectPageIndex)
  console.log(curEditTemplate)
  const pageUrl = curEditTemplate.pageStructure[pageIndex].url
  if (pageUrl) {
    chrome.tabs.create({url: pageUrl}, (tab) => {
      injectTemplate(tab.id)
    })
  } else {
    injectTemplate(null)
  }
}
const messageProcess = {
  name: 'background',
  actions: {
    click(sender, message, sendResponse) {},
    inject(sender, message, sendResponse) {
      curEditTemplate = message.msg.curEditTemplate
      curInjectPageIndex = message.msg.pageIndex
      console.log('cur inject page index : ' + curInjectPageIndex)
      console.log(curEditTemplate)
      if (curEditTemplate.url) {
        chrome.tabs.create({url: curEditTemplate.url}, (tab) => {
          injectTemplate(tab.id)
        })
      } else {
        injectTemplate(null)
      }
    },
    getCurPage(sender, message, sendResponse) {
      console.log(curEditTemplate.pageStructure[curInjectPageIndex])
      sendResponse(curEditTemplate.pageStructure[curInjectPageIndex])
    },
    submitPage(sender, message, sendResponse) {
      message.msg.url = sender.tab.url
      curEditTemplate.pageStructure[curInjectPageIndex] = JSON.parse(JSON.stringify(message.msg))
      updateCurEditTemplate(curEditTemplate, (result) => {
        sendResponse(result)
      })
      console.log('保存成功')
      console.log(message.msg)
      console.log(curEditTemplate)
      sendResponse()
    }
  },
  accept(sender, message, sendResponse) {
    if (message.target !== this.name) {
      return
    }
    const action = this.actions[message.action]
    if (action) {
      console.log('invoke action ' + action)
      action(sender, message, sendResponse)
    }
  }
}
chrome.runtime.onMessage.addListener(function(
  requestData,
  sender,
  sendResponse
) {
  console.log(sender.tab ? '来自内容脚本：' + sender.tab.url : '来自扩展程序')
  // console.log(requestData)
  if (requestData.target.startsWith('router')) {
    requestData.target = requestData.target.substring(7)
    sendMessage(sender.tab.id, requestData, null, sendResponse)
  }
  messageProcess.accept(sender, requestData, sendResponse)
})
