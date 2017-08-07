<template>
  <el-select
      v-model="value"
      filterable
      remote
      placeholder="搜索"
      v-on:change="onChange"
      :remote-method="remoteMethod"
      :loading="loading">
    <el-option
        v-for="item in options"
        :key="item.id"
        :label="item.name"
        :value="item.id">
    </el-option>
  </el-select>
</template>

<script>
  export default {
    name: 'query-select',
    props: {
      api: {
        required: true
      },
      field: {
        type: String,
        default: 'name'
      },
      page: {
        type: Number,
        default: 0
      },
      size: {
        type: Number,
        default: 10
      }
    },
    data() {
      return {
        options: [],
        value: [],
        loading: false,
        curData: null
      }
    },
    mounted() {
    },
    methods: {
      setData(data) {
        if (data) {
          this.curData = data;
          this.options = [data];
          this.value = data.id;
        }
      },
      remoteMethod(query) {
        if (query === null || query === undefined || typeof (query) !== 'string' || query.length === 0) {
          return
        }
        if (this.curData && this.curData.name === query) {
          console.log('same data');
          return
        }
        console.log(query);
        if (query && query.trim().length > 0) {
          this.loading = true;
          this.api.query(query).then(response => {
            this.options = response.data.content;
            this.loading = false;
          });
        } else {
          this.options = [];
        }
      },
      onChange(value) {
        if (value) {
          if (this.curData && this.curData.id === value) {
            console.log('same data');
            return
          }
          console.log('change ' + value);
          const data = this.options.find(v => v.id = value);
          this.curData = data;
          this.$emit('change', data)
        }
      }
    }
  };
</script>
