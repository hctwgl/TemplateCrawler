import chromeUtile from './chromeUtil'
let $bg
function init() {
  if (process.env.NODE_ENV === 'production') {
    console.log('init')
    chromeUtile.getBackgroundPage().then(bg => {
      $bg = bg
      console.log($bg)
    })
  }
}
init()
export const setUrl = function(url) {
  if (process.env.NODE_ENV === 'production') {
    $bg.serverUrl = url
  } else {
    localStorage.setItem('baseUrl', url)
  }
  console.log(url)
}

export const getUrl = function() {
  if (process.env.NODE_ENV === 'production') {
    console.log($bg)
    return $bg.serverUrl
  } else {
    return localStorage.getItem('baseUrl')
  }
}

export const hasSet = function() {
  const url = getUrl()
  if (url === null || url === undefined || url.length === 0) {
    return false
  }
  return true
}
