import fetch from '@/utils/fetch'

export default function BaseApi (baseUri) {
  return {
    get: id => fetch({
      url: baseUri + '/' + id,
      method: 'GET'
    }),
    page: (page = 0, size = 10) => fetch({
      url: baseUri + '/page',
      method: 'GET',
      params: {
        page,
        size
      }
    }),
    query: (q, field = 'name', page = 0, size = 10) => fetch({
      url: baseUri + '/query',
      method: 'GET',
      params: {
        q,
        field,
        page,
        size
      }
    }),
    add: data => fetch({
      url: baseUri + '/add',
      method: 'POST',
      data
    }),
    edit: data => fetch({
      url: baseUri + '/edit',
      method: 'PUT',
      data
    }),
    delete: id => fetch({
      url: baseUri + '/delete/' + id,
      method: 'DELETE'
    })
  }
}
