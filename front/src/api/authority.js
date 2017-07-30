import fetch from '@/utils/fetch';
const apiBaseUri = '/authority';
export function list() {
  return fetch({
    url: apiBaseUri + '/list',
    method: 'get'
  });
}


