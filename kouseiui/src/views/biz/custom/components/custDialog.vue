<template>
  <div>
    <el-form
      v-if="state.conditionFields && state.conditionFields.length > 0"
      ref="prequeryForm"
      :inline="true"
      label-width="50px"
      :model="state.querydia"
      @submit.prevent
    >
      <template v-for="item in state.conditionFields" :key="item.condition">
        <el-form-item
          v-if="item.valueSource === 'param'"
          :label="item.showName"
          label-width="90"
          :prop="item.columnName"
        >
          <el-input
            v-if="item.value.ctrlType === 'inputText'"
            v-model="
              state.querydia.queryParam[
                getlastStr(item.columnName) + '$V' + item.condition
              ]
            "
          />
        </el-form-item>
      </template>
      <el-form-item>
        <el-button :icon="Search" type="primary" @click="searchprequery()">
          Search
        </el-button>
        <el-button :icon="RefreshRight" @click="resetprequery(prequeryForm)">
          Reset
        </el-button>
      </el-form-item>
    </el-form>

    <el-table
      ref="multipleTableRef"
      v-loading="state.loading"
      class="abTable"
      :data="state.previewtableData"
      style="width: 100%; margin-top: 20px"
      @cell-click="rowClickFn"
    >
      <template v-if="state.displayFields && state.displayFields.length > 0">
        <el-table-column width="60">
          <template #default="{ row }">
            <div>
              <el-radio v-model="row.radio" :label="row.account_" />
            </div>
          </template>
        </el-table-column>
        <el-table-column
          v-for="item in state.displayFields"
          :key="item.columnName"
          :label="item.showName"
          :prop="item.columnName"
          show-overflow-tooltip
        />
      </template>
    </el-table>
    <!--Pagination-->
    <el-pagination
      v-if="props.data.page === 1 && state.previewtableData.length > 0"
      v-model:currentPage="state.querydia.currentPage"
      v-model:page-size="state.querydia.pageSize"
      :background="true"
      :disabled="false"
      layout="prev, pager, next, jumper,total, sizes"
      :page-sizes="[5, 10, 20, 30, 40, 50]"
      :total="state.total"
      @current-change="listDataByCodeUrllFn"
      @size-change="listDataByCodeUrllFn"
    />
  </div>
</template>
<script setup lang="ts">
  import { reactive, ref, watch } from 'vue'
  import { Search, RefreshRight } from '@element-plus/icons-vue'
  import { sysApi, bizApi, postData } from 'agilebpm'
  const emit = defineEmits(['updateformKeyName', 'updateRow'])
  const props = defineProps({
    visible: {
      type: Boolean,
      default: false,
    },
    data: {
      type: Object,
      required: true,
    },
  })
  const state: any = reactive({
    conditionFields: [],
    displayFields: [],
    tableData: [],
    previewtableData: [],
    loading: false,
    total: 0,
    querydia: {
      pageSize: 10,
      currentPage: 1,
      queryParam: {
        style$VEQ: 'tree',
      },
    },
    previewDialogRow: {
      radio: null,
    },
  })
  const prequeryForm = ref()
  // Listening pop-up box open close
  watch(
    () => props.visible,
    (newValue, oldValue) => {
      if (!newValue) {
        state.conditionFields = []
        state.displayFields = []
        state.previewtableData = []
        state.querydia.currentPage = 1
        state.total = 0
        prequeryForm.value.resetFields()
      }
      if (newValue) {
        init()
      }
    }
  )

  const conditionFieldsdo = (row: any) => {
    const queryParam = {}
    if (row.length > 0) {
      row.forEach(
        (element: {
          valueSource: string
          columnName: string | string[]
          condition: string
          value: { text: any }
        }) => {
          if (element.valueSource === 'fixedValue') {
            queryParam[
              `${getlastStr(element.columnName)}$V${element.condition}`
            ] = element.value.text
          }
        }
      )
    }
    return queryParam
  }

  const searchprequery = () => {
    state.querydia.currentPage = 1
    listDataByCodeUrllFn()
  }

  const resetprequery = (formEl: any) => {
    prequeryForm.value.resetFields()
    state.querydia.queryParam = conditionFieldsdo(state.conditionFields)
    state.querydia.queryParam['style_$VEQ'] = 'tree'
    state.querydia.currentPage = 1
    listDataByCodeUrllFn()
  }

  // Request dialog data interface
  const listDataByCodeUrllFn = () => {
    state.loading = true
    //postData(`${bizApi.customDialog.listDataByCodeUrl}custDialog`, state.querydia)
    bizApi.customDialog.listDataByCode('custDialog', state.querydia).then(
      (result) => {
        state.loading = false
        state.previewtableData = result.data.rows
        state.previewtableData.forEach((element: any) => {
          element.radio = false
        })
        state.total = result.data.total
      },
      () => {}
    )
  }
  // Click on an item in the table list
  const rowClickFn = (row: any) => {
    state.previewDialogRow = row
    state.previewtableData.forEach((element: any) => {
      element.radio = false
    })
    row.radio = row.account_
    emit('updateRow', row)
  }

  const getlastStr = (str: string | string[]) => {
    if (str[str.length - 1] === '_') {
      return str.slice(0, str.length - 1)
    } else {
      return str
    }
  }
  const init = () => {
    state.conditionFields = props.data.conditionFields
    state.displayFields = props.data.displayFields
    state.querydia.pageSize = props.data.pageSize
    state.querydia.queryParam = conditionFieldsdo(state.conditionFields)
    state.querydia.queryParam['style_$VEQ'] = 'tree'
    listDataByCodeUrllFn()
  }
  const nodeClickFn = (row: any) => {
    const obj = {
      id: row.id,
      parentId: row.id,
      name: row.name,
    }
    emit('updateRow', obj)
  }

  onMounted(() => {
    init()
  })
</script>
<style lang="scss">
  .abTable {
    .el-radio {
      .el-radio__label {
        display: none;
      }
    }
  }
</style>
