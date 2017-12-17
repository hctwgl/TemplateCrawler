import chromeUtile from './chromeUtil'
let curToken = null
let $bg = null
function init() {
  if (process.env.NODE_ENV === 'production') {
    chromeUtile.getBackgroundPage().then(bg => {
      $bg = bg
    })
  }
}
init()
export const setToken = function(token) {
  if (process.env.NODE_ENV === 'production') {
    $bg.token = token
  } else {
    localStorage.setItem('token', token)
  }
  curToken = token
  console.log(curToken)
}

export const getToken = function() {
  console.log(curToken)
  if (curToken === null) {
    if (process.env.NODE_ENV === 'production') {
      curToken = $bg.token
    } else {
      curToken = localStorage.getItem('token')
    }
  }
  console.log(curToken)
  return curToken
}

export const clearToken = function() {
  curToken = null
  if (process.env.NODE_ENV === 'production') {
    $bg.token = null
  } else {
    localStorage.removeItem('token')
  }
}
