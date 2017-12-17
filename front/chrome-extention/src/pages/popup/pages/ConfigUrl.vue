<template>
  <div id="form">
    <h3>设置模板服务器地址</h3>
    <el-form ref="form" label-position="top" label-width="80px">
      <el-form-item>
        <el-input v-model="baseUrl" size="small" placeholder="模板服务器地址"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button id="setBtn" type="primary" size="small" @click="set">设置</el-button>
        <!-- <el-button @click="resetForm">重置</el-button> -->
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import global from '../global'
import fetch from '@/utils/fetch'
import { setUrl, getUrl, hasSet } from '@/utils/serverUrl'

export default {
  name: 'ConfigUrl',
  data() {
    return {
      // baseUrl: null
      baseUrl: 'http://localhost:10000'
    }
  },
  created() {
    if (hasSet()) {
      this.baseUrl = getUrl()
      this.set()
      console.log('url is ' + this.baseUrl)
    } else {
      console.log('no old url')
    }
  },
  methods: {
    set() {
      console.log('init fetch....')
      global.fetch = fetch.create(this.baseUrl)
      setUrl(this.baseUrl)
      this.$router.push({ path: '/login' })
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
  width: 250px;
  margin: 30px auto 0px auto;
}
#setBtn {
  width: 100%;
}
</style>
