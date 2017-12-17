/* global chrome:true $:true xpathUtil:true */
/* eslint no-undef: "error" */
let iframe = null

function injectIframe() {
  iframe = document.createElement('iframe')
  //   iframe.style.display = 'none'
  iframe.id = 'create-template-inject-iframe'
  iframe.src = chrome.extension.getURL('/iframe.html')
  document.body.appendChild(iframe)
}

function init() {
  console.log('content init')
  injectIframe()
}
init()
const status = {
  chooseDom: false,
  cancelChooseDom() {
    this.chooseDom = false
    $('.' + chooseDomClass).removeClass(chooseDomClass)
    iframe.style.display = 'block'
  },
  startChooseDom() {
    this.chooseDom = true
    iframe.style.display = 'none'
    iframe.blur()
    document.body.focus()
  },
  locateByXpath(xpath) {
    var nodes = xpathUtil.getElementsByXpath(xpath, document)
    for (var i = 0; i < nodes.length; i++) {
      var n = nodes[i]
      addLocationClass(n)
    }
  }
}

// 元素选择
const chooseDomClass = 'create-template-choose-dom'
// $('body')
//   .find('*')
//   .each(function() {
//     $(this).off('click')
//   })
$(document).on('click', 'body', function(e) {
  if (status.chooseDom) {
    status.cancelChooseDom()
    const xpath = xpathUtil.getXpath(event.target)
    console.log(xpath)
    console.log('click')
    messageProcess.sendMessage({
      target: 'router-iframe',
      action: 'setXpath',
      msg: xpath
    })
    e.preventDefault()
    e.stopPropagation()
  }
})
// document.addEventListener(
//   'click',
//   function(event) {
//     if (status.chooseDom) {
//       const xpath = xpathUtil.getXpath(event.target)
//       console.log(xpath)
//       console.log('click')
//       messageProcess.sendMessage({
//         target: 'router-iframe',
//         action: 'setXpath',
//         msg: xpath
//       })
//       event.preventDefault()
//       event.stopPropagation()
//     }
//   },
//   false
// )
function addLocationClass(node) {
  node.className = (node.className + ' ' + chooseDomClass).trim()
}
function removeClass(node) {
  node.className = node.className
    .replace(new RegExp(chooseDomClass, 'g'), '')
    .trim()
}
document.addEventListener(
  'mouseover',
  function(event) {
    if (status.chooseDom) {
      // highlight the mouseover target
      addLocationClass(event.target)
    }
  },
  false
)

document.addEventListener(
  'mouseout',
  function(event) {
    if (status.chooseDom) {
      // highlight the mouseover target
      if (event.target.className) {
        removeClass(event.target)
      }
    }
  },
  false
)
document.onkeydown = function(e) {
  // alert(String.fromCharCode(e.keyCode)+" --> "+e.keyCode);
  if (e.which === 27) {
    console.log('press esc!')
    status.cancelChooseDom()
  }
}
// message 处理
const messageProcess = {
  name: 'content',
  actions: {
    chooseDom(sender, message, sendResponse) {
      console.log('get action choose dom')
      status.startChooseDom()
    },
    locationXpath(sender, message, sendResponse) {
      console.log('get action choose dom')
      status.locateByXpath(message.msg)
    },
    cancelLocation() {
      status.cancelChooseDom()
    }
  },
  accept(sender, message, sendResponse) {
    if (message.target !== this.name) {
      return
    }
    const action = this.actions[message.action]
    if (action) {
      action(sender, message, sendResponse)
    }
  },
  sendMessage(message, responseCallback) {
    chrome.runtime.sendMessage(message, responseCallback)
  }
}
chrome.runtime.onMessage.addListener(function(
  requestData,
  sender,
  sendResponse
) {
  console.log(sender.tab ? '来自内容脚本：' + sender.tab.url : '来自扩展程序')
  console.log(requestData)
  messageProcess.accept(sender, requestData, sendResponse)
})
