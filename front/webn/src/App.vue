<template>
  <div id="appp">
    <router-view/>
  </div>
</template>

<script>
export default {
  name: 'app'
}
</script>

<style>
html {
  height: 100%;
}
body {
  margin: 0;
  padding: 0;
  height: 100%;
}

#appp {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  height: 100%;
}
.main-container {
  height: 100%;
  border: 1px solid #eee;
}
</style>
