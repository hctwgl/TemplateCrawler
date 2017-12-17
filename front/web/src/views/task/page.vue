<template>
    <div class="app-container">
        <div class="filter-container">
            <!--<el-input @keyup.enter.native="handleFilter" style="width: 200px;" class="filter-item" placeholder="标题" v-model="listQuery.title">-->
            <!--</el-input>-->

            <!--<el-select clearable style="width: 90px" class="filter-item" v-model="listQuery.importance" placeholder="重要性">-->
                <!--<el-option v-for="item in importanceOptions" :key="item" :label="item" :value="item">-->
                <!--</el-option>-->
            <!--</el-select>-->

            <!--<el-select clearable class="filter-item" style="width: 130px" v-model="listQuery.type" placeholder="类型">-->
                <!--<el-option v-for="item in  calendarTypeOptions" :key="item.key" :label="item.display_name+'('+item.key+')'" :value="item.key">-->
                <!--</el-option>-->
            <!--</el-select>-->

            <!--<el-select @change='handleFilter' style="width: 120px" class="filter-item" v-model="listQuery.sort" placeholder="排序">-->
                <!--<el-option v-for="item in sortOptions" :key="item.key" :label="item.label" :value="item.key">-->
                <!--</el-option>-->
            <!--</el-select>-->

            <!--<el-button class="filter-item" type="primary" v-waves icon="search" @click="handleFilter">搜索</el-button>-->
            <el-button class="filter-item" style="margin-left: 10px;" @click="handleCreate" type="primary" icon="edit">添加</el-button>
            <!--<el-button class="filter-item" type="primary" icon="document" @click="handleDownload">导出</el-button>-->
            <!--<el-checkbox class="filter-item" @change='tableKey=tableKey+1' v-model="showAuditor">显示审核人</el-checkbox>-->
        </div>
        <el-table :data="list" v-loading.body="listLoading" element-loading-text="拼命加载中" border fit highlight-current-row>
            <el-table-column align="center" label='ID' width="50">
                <template scope="scope">
                    {{scope.row.id}}
                </template>
            </el-table-column>
            <el-table-column label="name" width="110">
                <template scope="scope">
                    {{scope.row.name}}
                </template>
            </el-table-column>
            <!--<el-table-column label="模板名" align="center">-->
                <!--<template scope="scope">-->
                    <!--<span>{{scope.row.template.name}}</span>-->
                <!--</template>-->
            <!--</el-table-column>-->
            <el-table-column label="周期" align="center">
                <template scope="scope">
                    <span>{{scope.row.cycle}}</span>
                </template>
            </el-table-column>
            <el-table-column label="线程数" align="center">
                <template scope="scope">
                    <span>{{scope.row.threadNum}}</span>
                </template>
            </el-table-column>
            <el-table-column label="上次运行时间" align="center">
                <template scope="scope">
                    <span>{{scope.row.lastStartTime}}</span>
                </template>
            </el-table-column>
            <el-table-column label="email" align="center">
                <template scope="scope">
                    <el-button v-if="scope.row.status!='published'" size="small" type="success" @click="handleEdit(scope.row)">编辑
                    </el-button>
                </template>
            </el-table-column>
        </el-table>
        <!--<div v-show="!listLoading" class="pagination-container">-->
            <!--<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page.sync="listQuery.page"-->
                           <!--:page-sizes="[10,20,30, 50]" :page-size="listQuery.limit" layout="total, sizes, prev, pager, next, jumper" :total="total">-->
            <!--</el-pagination>-->
        <!--</div>-->
    </div>
</template>

<script>
  import api from '@/api/task';

  export default {
    data() {
      return {
        list: null,
        listLoading: true,
      }
    },
    created() {
      this.fetchData();
    },
    methods: {
      fetchData() {
        this.listLoading = true;
        api.page().then(response => {
          this.list = response.data.content;
          this.listLoading = false;
        });
      },
      handleCreate() {
        this.$router.push({ path: '/task/edit' });
      },
      handleEdit(row) {
        console.log(row);
        this.$router.push({ path: '/task/edit/' + row.id });
      }
    }
  };
</script>
