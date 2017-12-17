import utils from './utils'

/* global chrome:true */
/* eslint no-undef: "error" */
const chromeUtil = {
  $bg: null,
  getCurTabId() {
    return new Promise(function(resolve, reject) {
      if (process.env.NODE_ENV === 'production') {
        chrome.tabs.query({ active: true }, function(tabs) {
          const tab = tabs[0]
          resolve(tab.id)
        })
      } else {
        resolve('http://www.test.com')
      }
    })
  },
  getCurTabUrl() {
    return new Promise(function(resolve, reject) {
      if (process.env.NODE_ENV === 'production') {
        chrome.tabs.query({ active: true }, function(tabs) {
          const tab = tabs[0]
          resolve(tab.url)
        })
      } else {
        resolve('http://www.test.com')
      }
    })
  },
  getCurTabDomain() {
    return new Promise((resolve, reject) => {
      this.getCurTabUrl().then(function(url) {
        resolve(utils.getHost(url))
      })
    })
  },
  injectContent(page) {
    // chrome.tabs.executeScript({
    //   code: 'document.body.style.backgroundColor="red"'
    // })
    chrome.runtime.sendMessage({
      target: 'background',
      action: 'inject',
      msg: page
    })
  },
  sendMessage(message, responseCallback) {
    console.log('send msg:' + message.action)
    chrome.runtime.sendMessage(message, responseCallback)
  },
  getBackgroundPage() {
    return new Promise((resolve, reject) => {
      chrome.runtime.getBackgroundPage(function(bg) {
        resolve(bg)
      })
    })
  },
  init() {
    return new Promise((resolve, reject) => {
      this.getBackgroundPage().then(bg => {
        this.$bg = bg
        resolve()
      })
    })
  }
}

export default chromeUtil
