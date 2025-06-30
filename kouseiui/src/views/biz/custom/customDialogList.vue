<template>
  <div class="comprehensive-table-container">
    <div ref="titleForm">
      <el-row class="vab-query-form">
        <el-col class="top-panel">
          <el-form
            ref="queryForm"
            :inline="true"
            label-width="62px"
            :model="query"
            @submit.prevent
          >
            <el-form-item label="Code" prop="code$VLK">
              <el-input
                v-model="query.code$VLK"
                placeholder="Please enter the code"
                @keyup.enter.native="search()"
              />
            </el-form-item>
            <el-form-item label="Name" prop="name$VLK">
              <el-input
                v-model="query.name$VLK"
                placeholder="Please enter a name"
                @keyup.enter.native="search()"
              />
            </el-form-item>
            <el-form-item>
              <el-button :icon="Search" type="primary" @click="search()">
                Search
              </el-button>
              <el-button :icon="RefreshRight" @click="reset()">Reset</el-button>
            </el-form-item>
          </el-form>
        </el-col>
        <el-col class="left-panel">
          <router-link to="/biz/custom/customDialogEdit">
            <el-button :icon="Plus" type="primary">Add</el-button>
          </router-link>
        </el-col>
      </el-row>
    </div>
    <ab-table
      ref="abTable"
      v-model="selectedData"
      :checkable="false"
      :default-sort="{ order: 'dx', prop: 'createTime' }"
      :height="tableHeight"
      :query-param="query"
      row-key="id"
      :url="bizApi.customDialog.bizCustDialogUrl"
    >
      <ab-column label="Code" min-width="160" prop="code" />
      <ab-column label="Name" min-width="140" prop="name" />
      <ab-column label="Object Name" min-width="150" prop="objName" />
      <ab-column
        ab-template="dataSource"
        label="DataSource"
        min-width="105"
        prop="dataSource"
      />
      <template #dataSource="{ scope }">
        <span>{{ dataSourceSet(scope.row.dataSource) }}</span>
      </template>
      <ab-column
        ab-text-formatter="tree-Tree-warning-dark|list-List-success-dark"
        align="center"
        label="Format"
        prop="style"
        width="100"
      />
      <ab-column
        ab-text-formatter="view-View-warning-dark|table-Table-success-dark"
        align="center"
        label="Object Type"
        prop="objType"
        width="100"
      />
      <ab-column
        ab-text-formatter="0-No-warning-dark|1-Yes-success-dark"
        align="center"
        label="System-Built"
        prop="system"
        width="120"
      />
      <ab-column ab-template="edit" fixed="right" label="Action" width="290" />
      <template #edit="{ scope }">
        <router-link
          :to="{ name: 'CustomDialogEdit', query: { id: scope.row.id } }"
        >
          <el-button text type="primary">Edit</el-button>
        </router-link>
        <el-button text type="primary" @click="previewDialog(scope.row)">
          Preview Dialog Box
        </el-button>
        <el-button
          v-if="!scope.row.system"
          text
          type="primary"
          @click="
            sendAction(
              bizApi.customDialog.bizCustDialogremoveUrl + scope.row.id,
              `Are you sure you want to delete?`
            )
          "
        >
          Delete
        </el-button>
      </template>
    </ab-table>
    <el-dialog v-model="state.StringdialogVisible" title="Prompt information" width="40%">
      <div
        v-loading="state.loadingBox"
        style="max-height: 400px; overflow-y: auto"
      >
        {{ state.dataString }}
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button type="primary" @click="closeStringdialogVisibleFn">
            Confirm
          </el-button>
        </span>
      </template>
    </el-dialog>
    <ab-cust-dialog
      ref="custDialogref"
      :dialog-key="state.dialogKey"
      :ispre="true"
      @ok="watchFn"
    />
  </div>
</template>
<script>
  import { abTableMix, bizApi, postData, sysApi } from 'agilebpm'
  export default {
    name: 'CustomDialogList',
    mixins: [abTableMix],
  }
</script>
<script setup>
  import { reactive, ref } from 'vue'
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  import { Search, RefreshRight, Plus } from '@element-plus/icons-vue'
  const custDialogref = ref(null)
  // Query condition definition, if ts needs to define all parameters, here override the parent class, if not, you don't need to set it, the parent class has already defined the query object
  const query = reactive({
    code$VLK: '',
    name$VLK: '',
  })
  const state = reactive({
    dialogKey: '',
    dataString: '',
    StringdialogVisible: false,
    loadingBox: false,
    BizCustDialogSourceTypeOption: [],
  })

  // Data source
  sysApi.tools
    .getEnum('com.dstz.biz.api.constant.BizCustDialogSourceType', true)
    .then(({ data }) => {
      state.BizCustDialogSourceTypeOption = data
    })

  const dataSourceSet = (value) => {
    let valueSet = ''
    state.BizCustDialogSourceTypeOption.forEach((item, index) => {
      if (value === item.key) {
        valueSet = item.desc
      }
    })
    return valueSet
  }

  // Preview dialog box
  const previewDialog = (row) => {
    state.dialogKey = row.code
    nextTick(() => {
      custDialogref.value.openDialog()
    })
  }

  // Dialog box OK event
  const watchFn = (data) => {
    state.StringdialogVisible = true
    state.dataString = ''
    state.dataString = JSON.stringify(data)
  }
  // Processing query conditions
  const conditionFieldsdo = (row) => {
    const queryParam = {}
    if (row.length > 0) {
      // Needs modification
      row.forEach((element) => {
        if (element.valueSource === 'fixedValue') {
          queryParam[
            `${getlastStr(element.columnName)}$V${element.condition}`
          ] = element.value.text
        } else {
          queryParam[
            `${getlastStr(element.columnName)}$V${element.condition}`
          ] = ''
        }
      })
    }
    return queryParam
  }

  // Adjustment
  const getlastStr = (str) => {
    if (str[str.length - 1] === '_') {
      // return str.slice(0, str.length - 1)
      return str
    } else {
      return str
    }
  }

  // Preview query event Modify tool class
  const previewSearch = (row) => {
    state.StringdialogVisible = true
    state.loadingBox = true
    postData(bizApi.customDialog.listSimpleData + row.code, {
      pageSize: row.pageSize,
      currentPage: 1,
      queryParam: conditionFieldsdo(row.conditionFields),
    }).then(
      (result) => {
        state.loadingBox = false
        state.dataString = JSON.stringify(result.data)
      },
      () => {}
    )
  }
  // Close event
  const closeStringdialogVisibleFn = () => {
    state.StringdialogVisible = false
    state.dataString = ''
  }
</script>
<style lang="scss">
  .abTable {
    .el-radio {
      .el-radio__label {
        display: none;
      }
    }
  }
  .CustDialogref {
    overflow: auto;
  }
</style>
