import axios from 'axios'
import { getToken } from './token'
import { Message } from 'element-ui'

function create(baseUrl) {
  // 创建axios实例
  const service = axios.create({
    baseURL: baseUrl, // api的base_url
    timeout: 5000                  // 请求超时时间
  })

  // request拦截器
  service.interceptors.request.use(config => {
    config.headers.common['Authorization'] = getToken()
    return config
  }, error => {
    // Do something with request error
    console.log(error) // for debug
    Promise.reject(error)
  })

  // respone拦截器
  service.interceptors.response.use(
    response => {
      if (response.status !== 200) {
        Message({
          message: response,
          type: 'error',
          duration: 5 * 1000
        })
        return Promise.reject(response)
      } else {
        return response
      }
    },
    error => {
      console.log('err' + error)// for debug
      Message({
        message: error.message,
        type: 'error',
        duration: 5 * 1000
      })
      return Promise.reject(error)
    }
  )
  return service
}

export default { create }
