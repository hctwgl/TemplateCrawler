const util = {}
util.getHost = function(url) {
  const a = document.createElement('a')
  a.href = url
  console.log(a.hostname)
  return a.hostname
}

export default util
