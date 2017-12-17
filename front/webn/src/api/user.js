import fetch from '@/utils/fetch'

export function list () {
  return fetch({
    url: '/user/list',
    method: 'get'
  })
}

export function create (userData) {
  return fetch({
    url: '/user/create',
    method: 'post',
    params: userData
  })
}
