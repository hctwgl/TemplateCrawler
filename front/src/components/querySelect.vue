<template>
    <el-select
            v-model="value"
            filterable
            remote
            placeholder="搜索"
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
        loading: false
      }
    },
    mounted() {
    },
    methods: {
      remoteMethod(query) {
        if (query && query.trim().length > 0) {
          this.loading = true;
          this.api.query(query).then(response => {
            this.options = response.data.content;
            this.loading = false;
          });
        } else {
          this.options = [];
        }
      }
    }
  };
</script>
