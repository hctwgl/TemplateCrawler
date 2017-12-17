<template>
  <div id="form" v-loading="loading">
    <div v-if="!loading">
      <div v-if="exist">
        当前网站：
        <span>{{website.name}}</span>
        <div>
          <el-button type="primary" size="small" @click="confirm">确认</el-button>
        </div>
      </div>
      <div v-if="!exist">
        <h3>设置网站</h3>
        <div>{{website.domain}}</div>
        <el-form ref="form" label-position="top" label-width="80px">
          <el-form-item>
            <el-input v-model="website.name" size="small" placeholder="网站名"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button id="setBtn" type="primary" size="small" @click="submit">设置</el-button>
            <!-- <el-button @click="resetForm">重置</el-button> -->
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
import global from '../global.js'
import chrome from '@/utils/chromeUtil'

export default {
  name: 'ConfigWeb',
  data() {
    return {
      website: {},
      exist: false,
      loading: true
    }
  },
  created() {
    this.getCurWebsite()
  },
  methods: {
    getCurWebsite() {
      chrome.getCurTabDomain().then(domain => {
        console.log(global)
        this.website.domain = domain
        const that = this
        global.getFetch().get('/api/website/domain', {
          'params': {
            value: domain
          }
        }).then(response => {
          this.loading = false
          console.log(response)
          // that.setState({ fetchData: true })
          // console.log(response)
          if (response.data) {
            that.website = response.data
            that.exist = true
          } else {
            this.exist = false
          }
        })
      }
      )
    },
    submit() {
      global.getFetch().post('/api/website/add', this.website).then(response => {
        this.website = response.data
        this.exist = true
        // this.updateWebsite(response.data)
      })
    },
    confirm() {
      global.curWebsite = this.website
      this.$router.push('/template')
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h3 {
  text-align: center;
}
#form {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
  width: 100%;
  height: 100%;
  /* width: 250px; */
  /* margin: 30px auto 0px auto; */
}
#setBtn {
  width: 100%;
}
</style>
