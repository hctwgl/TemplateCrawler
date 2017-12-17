<template>
  <div id="app">
    <h1>我是多页面---OPTION</h1>
    <img src="../../assets/logo.png">
    <router-view/>
    <router-link to="/">
      <span>首页</span>
    </router-link>
    <router-link to="/child">
      <span>子页面</span>
    </router-link>
    <el-form :model="ruleForm2" status-icon :rules="rules2" ref="ruleForm2" label-width="100px" class="demo-ruleForm">
      <el-form-item label="年龄" prop="age">
        <el-input v-model.number="ruleForm2.age"></el-input>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  name: 'app',
  data() {
    var checkAge = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('年龄不能为空'))
      }
      callback()
    }
    return {
      ruleForm2: {
        age: ''
      },
      rules2: {
        age: [
          { validator: checkAge, trigger: 'blur' }
        ]
      }
    }
  }
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
