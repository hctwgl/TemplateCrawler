<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script>
import { hasSet } from '@/utils/serverUrl'

export default {
  name: 'app',
  created() {
    if (!hasSet()) {
      console.log('server url not config')
      this.$router.push({ path: '/configUrl' })
    }
  }
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  /* text-align: center; */
  color: #2c3e50;
  width: 400px;
  height: 300px;
  padding: 10px;
  background-color: #f5f5f5;
}
</style>
