<template>
  <div>
    <el-form :label-position="right" label-width="80px" :model="field">
      <el-form-item label="名称">
        <el-input v-model="field.name"></el-input>
      </el-form-item>
      <el-form-item label="标签">
        <el-input v-model="field.label"></el-input>
      </el-form-item>
      <el-form-item label="类型">
        <el-input v-model="field.type"></el-input>
      </el-form-item>
      <el-form-item label="默认值">
        <el-input v-model="field.defaultValue"></el-input>
      </el-form-item>
      <el-form-item label="Xpath">
        <el-input v-model="field.xpath"></el-input>
      </el-form-item>
      <el-form-item label="多选">
        <el-input v-model="field.multiple"></el-input>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
  export default {
    name: 'template-field',
    data() {
      return {
        labelPosition: 'right',
        field: {
          name: '',
          region: '',
          type: ''
        }
      };
    }
  }
</script>