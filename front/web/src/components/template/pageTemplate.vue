<template>
  <div>
    <div style="margin-bottom: 20px;">
      <el-button
          size="small"
          @click="addTab(editableTabsValue)">
        add tab
      </el-button>
    </div>
    <el-tabs v-model="editableTabsValue" type="card" closable @tab-remove="removeTab">
      <el-tab-pane
          v-for="(page, index) in pages"
          :label="index + '页'"
          :name="index">
        <template-field v-for="(field, index) in page.fields" :field="field"></template-field>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<script>
  import field from './field';

  export default {
    data() {
      return {
        editableTabsValue: '2',
        fields: [],
        pages: [{
          title: 'Tab 1',
          name: '1',
          content: 'Tab 1 content'
        }, {
          title: 'Tab 2',
          name: '2',
          content: 'Tab 2 content'
        }],
        tabIndex: 2
      }
    },
    methods: {
      addTab(targetName) {
        let newTabName = ++this.tabIndex + '';
        this.editableTabs.push({
          title: 'New Tab',
          name: newTabName,
          content: 'New Tab content'
        });
        this.editableTabsValue = newTabName;
      },
      removeTab(targetName) {
        const tabs = this.editableTabs;
        let activeName = this.editableTabsValue;
        if (activeName === targetName) {
          tabs.forEach((tab, index) => {
            if (tab.name === targetName) {
              const nextTab = tabs[index + 1] || tabs[index - 1];
              if (nextTab) {
                activeName = nextTab.name;
              }
            }
          });
        }

        this.editableTabsValue = activeName;
        this.editableTabs = tabs.filter(tab => tab.name !== targetName);
      }
    },
    components: {
      field
    }
  }
</script>