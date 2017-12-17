import router from './router'
import fetch from '@/utils/fetch'
import { getUrl } from '@/utils/serverUrl'

const global = {
  token: null,
  fetch: null,
  curWebsite: null,
  setFetch(fetch) {
    this.fetch = fetch
  },
  getFetch() {
    if (this.fetch === null) {
      if (getUrl()) {
        global.fetch = fetch.create(this.baseUrl)
      } else {
        router.push('/')
      }
    }
    return this.fetch
  }
}
export default global
