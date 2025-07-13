<template>
  <el-container class="layout-container-demo">
    <el-header>
      <ab-save
        back-name="CustomDialogList"
        :back-other-fn="backOtherFn"
        :custom="
          props.dialogSetting && props.dialogSetting.custom
            ? props.dialogSetting.custom
            : false
        "
        :form-ref="formRef"
        :save-data="info.data"
        :url="bizApi.customDialog.bizCustDialogSave"
        :valid-after-fn="validaftersave"
      />
      <ab-load
        v-model="info.data"
        :url="bizApi.customDialog.bizCustDialogGet"
        @after-fn="getDataFn"
      />
      <el-button
        v-if="info.data.objName.length > 0 && info.data.style.length > 0"
        style="margin-left: 12px"
        type="primary"
        @click="openpar"
      >
        Setting field parameters
      </el-button>
    </el-header>
    <el-divider class="dividermar" />
    <el-main>
      <el-scrollbar>
        <el-form
          ref="formRef"
          label-suffix="："
          label-width="200px"
          :model="info.data"
        >
          <el-row>
            <el-col :span="9" style="margin-right: 35px">
              <el-form-item
                label="Name"
                m-item
                prop="name"
                :rules="[{ required: true, message: 'Required' }]"
              >
                <ab-pinyin
                  v-model="info.data.name"
                  v-model:to="info.data.code"
                />
              </el-form-item>
              <el-form-item label="Description" prop="desc">
                <el-input v-model="info.data.desc" :rows="4" type="textarea" />
              </el-form-item>
              <el-form-item
                label="Format"
                prop="style"
                :rules="[{ required: true, message: 'Required' }]"
              >
                <el-select
                  v-model="info.data.style"
                  placeholder="Please select"
                  style="width: 100%"
                  @change="changeStyle"
                >
                  <el-option
                    v-for="item in info.BizCustDialogStyleOption"
                    :key="item.key"
                    :label="item.desc"
                    :value="item.key"
                  />
                </el-select>
              </el-form-item>
              <el-form-item label="Single choice/Multi choice" prop="multiple">
                <el-switch
                  v-model="info.data.multiple"
                  active-text="Multi Choice"
                  :active-value="1"
                  inactive-text="Single Choice"
                  :inactive-value="0"
                />
              </el-form-item>
              <el-form-item label="Left Tree" prop="leftTree">
                <el-switch
                  v-model="info.data.leftTree"
                  active-text="Open"
                  :active-value="1"
                  inactive-text="Close"
                  :inactive-value="0"
                  @change="changeLeftTree"
                />
              </el-form-item>

              <el-form-item
                v-if="info.data.leftTree"
                label="Left tree configuration"
                prop="leftTreeConfigJson"
              >
                <!-- Data dictionary -->
                <ab-select-tree
                  v-if="
                    info.leftTreeData &&
                    info.leftTreeData.length > 0 &&
                    info.data.leftTreeConfig.type === 'dataDict'
                  "
                  v-model="info.data.leftTreeConfig.key"
                  :is-cust="true"
                  style="width: 100%"
                  :tree-data="info.leftTreeData"
                />
                <!-- Tree dialog box -->
                <el-tag
                  v-if="
                    info.data.leftTreeConfig.type === 'bizCustDialog' &&
                    info.data.leftTreeConfig.key.length > 0
                  "
                >
                  {{ info.data.leftTreeConfig.key }} -
                  {{ info.data.leftTreeConfig.name }}
                </el-tag>
                <ab-cust-dialog
                  v-if="info.data.leftTreeConfig.type === 'bizCustDialog'"
                  dialog-key="custDialog"
                  :dialog-setting="{ multiple: false }"
                  :param="{ style_$VEQ: 'tree' }"
                  style="display: inline-block"
                  @ok="choosezdydhklb"
                >
                  Select
                </ab-cust-dialog>
              </el-form-item>

              <el-form-item v-if="info.data.leftTree" label="Left tree root node">
                <el-input
                  v-model="info.data.leftTreeConfig.rootName"
                  placeholder="Please enter the left root node"
                />
              </el-form-item>
              <el-form-item
                label="Data Source"
                prop="dataSource"
                :rules="[{ required: true, message: 'Required' }]"
              >
                <el-select
                  v-model="info.data.dataSource"
                  :disabled="info.data.id ? true : false"
                  placeholder="Please select"
                  style="width: 100%"
                >
                  <el-option
                    v-for="item in info.BizCustDialogSourceTypeOption"
                    :key="item.key"
                    :label="item.desc"
                    :value="item.key"
                  />
                </el-select>
              </el-form-item>
              <el-form-item
                v-if="info.data.dataSource === 'database'"
                label="Data Source"
                prop="source"
              >
                <el-select
                  v-model="info.data.dsKey"
                  :disabled="info.data.id ? true : false"
                  placeholder="Please select"
                  style="width: 100%"
                  @change="changesource"
                >
                  <el-option
                    v-for="item in info.dataSourceOptions"
                    :key="item.alias"
                    :label="item.name"
                    :value="item.alias"
                  />
                </el-select>
              </el-form-item>
              <el-form-item
                v-if="info.data.dataSource === 'database'"
                label="Query Type"
                prop="objType"
                :rules="[{ required: true, message: 'Required' }]"
              >
                <el-select
                  v-model="info.data.objType"
                  :disabled="info.data.id ? true : false"
                  placeholder="Please select"
                  style="width: 100%"
                >
                  <el-option
                    v-for="item in info.BizCustDialogObjTypeOption"
                    :key="item.key"
                    :label="item.desc"
                    :value="item.key"
                  />
                </el-select>
              </el-form-item>
              <el-form-item
                v-if="info.data.dataSource === 'database'"
                label="Table Name"
                prop="objName"
                :rules="[{ required: true, message: 'Required' }]"
              >
                <el-input
                  v-model="info.data.objName"
                  class="input-with-select"
                  clearable
                  :disabled="info.data.id ? true : false"
                  placeholder="Please enter keyword search"
                >
                  <template #append>
                    <el-button :icon="Search" @click="searchList" />
                  </template>
                </el-input>
              </el-form-item>
              <el-form-item
                v-if="info.data.dataSource === 'database' && !info.data.id"
                label="Select Object"
                prop="value"
              >
                <el-select
                  v-model="info.data.objName"
                  clearable
                  :disabled="info.data.id ? true : false"
                  :fit-input-width="true"
                  placeholder="Please select"
                  style="width: 100%"
                  @change="changesourceType"
                >
                  <el-option
                    v-for="item in info.objNameOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </el-form-item>
              <el-form-item
                v-if="info.data.dataSource === 'elasticsearch'"
                label="Collection Name"
                prop="objName"
                :rules="[{ required: true, message: 'Required' }]"
              >
                <el-input
                  v-model="info.data.objName"
                  placeholder="Please enter a collection name"
                />
              </el-form-item>
              <el-form-item
                v-if="info.data.dataSource === 'http'"
                label="URL address"
                prop="objName"
                :rules="[{ required: true, message: 'Required' }]"
              >
                <el-input
                  v-model="info.data.objName"
                  placeholder="Please enter the URL address"
                />
              </el-form-item>
              <el-form-item
                v-if="info.data.dataSource === 'interface'"
                label="Interface"
                prop="objName"
                :rules="[{ required: true, message: 'Required' }]"
              >
                <el-input
                  v-model="info.data.objName"
                  placeholder="beanId.method eg:userService.list"
                />
              </el-form-item>
            </el-col>
            <el-col :span="9">
              <el-form-item
                label="Code"
                prop="code"
                :rules="[
                  { required: true, message: 'Required' },
                  { max: 50, message: 'You can enter up to 50 characters' },
                  info.rule,
                ]"
              >
                <ab-code
                  v-model="info.data.code"
                  :disabled="info.data.id ? true : false"
                  placeholder="Please enter the code"
                />
              </el-form-item>
              <el-form-item
                label="Type"
                prop="typeCode"
                :rules="[{ required: true, message: 'Required' }]"
              >
                <ab-select-tree v-model="info.data.typeCode" type-code="biz" />
              </el-form-item>
              <el-form-item label="Is it built-in" prop="system">
                <template #label>
                  <div>
                    Is it built-in
                    <el-popover
                      content="If the dialog box is displayed inside the system, it means that the project itself requires this dialog box. Users should modify it with caution, otherwise it may cause system malfunction."
                      placement="bottom-start"
                      trigger="hover"
                    >
                      <template #reference>
                        <el-icon><info-filled style="color: red" /></el-icon>
                      </template>
                    </el-popover>
                  </div>
                </template>
                <el-switch
                  v-model="info.data.system"
                  active-text="Yes"
                  :active-value="1"
                  inactive-text="No"
                  :inactive-value="0"
                  inline-prompt
                />
              </el-form-item>
              <el-form-item
                label="Size"
                prop="width"
                :rules="[{ required: true, message: 'Width and height are required' }]"
              >
                <el-input
                  v-model="info.data.width"
                  placeholder="Width"
                  style="display: inline-block; max-width: 80px"
                />
                <el-input
                  v-model="info.data.height"
                  placeholder="Height"
                  style="display: inline-block; width: 80px"
                />
              </el-form-item>
              <el-form-item
                v-if="info.data.style === 'list'"
                label="Pagination"
                prop="pageSize"
                :rules="[{ required: true, message: 'Required' }]"
              >
                <el-switch
                  v-model="info.data.page"
                  active-text="Yes"
                  :active-value="1"
                  inactive-text="No"
                  :inactive-value="0"
                  inline-prompt
                  style="margin-right: 12px"
                />
                <el-input
                  v-if="info.data.page"
                  v-model="info.data.pageSize"
                  placeholder="Page size"
                  style="display: inline-block; max-width: 150px"
                />
              </el-form-item>
              <el-form-item v-if="info.data.leftTree" label="Type">
                <el-select
                  v-model="info.data.leftTreeConfig.type"
                  placeholder="Please select"
                  style="width: 100%"
                  @change="changeLeftTreeConfigType"
                >
                  <el-option
                    v-for="item in info.BizCustDialogLeftTreeConfigTypeOption"
                    :key="item.key"
                    :label="item.desc"
                    :value="item.key"
                  />
                </el-select>
              </el-form-item>
              <el-form-item v-if="info.data.leftTree" label="Associate condition fields">
                <el-button
                  v-if="info.data.conditionFields.length <= 0"
                  size="small"
                  type="primary"
                >
                  Please configure the condition fields of the dialog box first
                </el-button>
                <el-select
                  v-if="
                    info.data.conditionFields &&
                    info.data.conditionFields.length > 0
                  "
                  v-model="info.data.leftTreeConfig.condField"
                  placeholder="Please select"
                  style="width: 100%"
                >
                  <el-option
                    v-for="item in info.data.conditionFields"
                    :key="item.columnName"
                    :label="item.showName"
                    :value="item.columnName"
                  />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </el-scrollbar>
    </el-main>
    <!-- class="alignCenterDialog" -->
    <el-dialog
      v-model="info.dialogVisible"
      :align-center="true"
      :destroy-on-close="true"
      title="Setting field parameters"
      width="1300px"
    >
      <Parameters
        v-if="info.data.style"
        ref="parametersref"
        :data="info.data"
        :parameters-obj="info.parametersObj"
        @update-table-data="updateParameters"
      />
      <template #footer>
        <span class="dialog-footer">
          <el-button
            style="margin-right: 0"
            text
            type="primary"
            @click="info.dialogVisible = false"
          >
            Cancel
          </el-button>
          <el-button type="primary" @click="saveDialog">Confirm</el-button>
        </span>
      </template>
    </el-dialog>
  </el-container>
</template>

<script setup lang="ts">
  import { reactive, ref, onMounted } from 'vue'
  import { ElMessage } from 'element-plus'
  import { sysApi, bizApi, getData, postData } from 'agilebpm'
  import { Search } from '@element-plus/icons-vue'
  import type { FormInstance } from 'element-plus'
  import Parameters from './components/parameters.vue'
  // emit event, the parent class updates itself according to the event
  const emit = defineEmits(['closeFn'])
  const parametersref = ref('parametersref')
  const selectRef = ref('selectRef')
  const formRef = ref<FormInstance>()
  const systreeref = ref()
  const props = defineProps({
    dialogSetting: {
      type: Object,
      default: null,
    },
  })
  const info: any = reactive({
    BizCustDialogStyleOption: [],
    BizCustDialogLeftTreeConfigTypeOption: [],
    leftTreeConfigTreeOption: [],
    BizCustDialogSourceTypeOption: [],
    dataSourceOptions: [],
    BizCustDialogObjTypeOption: [],
    rule: null,
    treekey: '',
    treename: '',
    data: {
      name: '',
      code: '',
      typeCode: '',
      desc: '',
      system: 0,
      style: '',
      width: '800',
      height: '750',
      multiple: 0,
      page: 1,
      pageSize: '10',
      leftTree: 0,
      leftTreeConfig: {
        condField: '',
        key: '',
        name: '',
        rootName: 'All Data',
        type: '',
      },
      dataSource: '',
      objType: '',
      objName: '',
      dsKey: '',
      fieldsList: [],
      treeConfig: {},
      displayFields: [],
      conditionFields: [],
      returnFields: [],
      sortFields: [],
    },
    dialogVisible: false,
    parametersObj: {},
    parameterstitle: '',
    displayFields: [],
    conditionFields: [],
    conditionFieldsOption: [],
    returnFields: [],
    sortFields: [],
    editDia: true,
    oneListData: {},
    updateRow: {},
    leftTreeData: [],
  })
  // Display Type
  sysApi.tools
    .getEnum('com.dstz.biz.api.constant.BizCustDialogStyle', true)
    .then(({ data }) => {
      info.BizCustDialogStyleOption = data
      // info.data.style = info.BizCustDialogStyleOption[0].key
    })

  // Type
  sysApi.tools
    .getEnum('com.dstz.biz.api.constant.BizCustDialogLeftTreeConfigType', true)
    .then(({ data }) => {
      info.BizCustDialogLeftTreeConfigTypeOption = data
    })

  const changeLeftTree = (row: any) => {
    if (row === 1) {
      info.data.leftTreeConfig.type =
        info.BizCustDialogLeftTreeConfigTypeOption[0].key
    } else {
      info.data.leftTreeConfig.type = ''
    }
  }
  // Data source
  sysApi.tools
    .getEnum('com.dstz.biz.api.constant.BizCustDialogSourceType', true)
    .then(({ data }) => {
      info.BizCustDialogSourceTypeOption = data
      // info.data.dataSource = info.BizCustDialogSourceTypeOption[0].key
    })

  // Query
  sysApi.tools
    .getEnum('com.dstz.biz.api.constant.BizCustDialogObjType', true)
    .then(({ data }) => {
      info.BizCustDialogObjTypeOption = data
      // info.data.objType = info.BizCustDialogObjTypeOption[0].key
    })

  //  Condition
  sysApi.tools
    .getEnum('com.dstz.base.query.ConditionType', true)
    .then(({ data }) => {
      info.parametersObj.ConditionTypeOption = data
    })

  //  Value source
  sysApi.tools
    .getEnum(
      'com.dstz.biz.api.constant.BizCustDialogConditionFieldValueSource',
      true
    )
    .then(({ data }) => {
      info.parametersObj.BizCustDialogConditionOption = data
    })

  // Verify before saving
  const validaftersave = () => {
    if (info.data.style == 'tree') {
      let columns = []
      if (
        info.parametersObj.tableData &&
        info.parametersObj.tableData.columns
      ) {
        columns = info.parametersObj.tableData.columns
      }
      const id = info.data.treeConfig.id
      const pid = info.data.treeConfig.pid
      info.data.treeConfig.idType = null
      info.data.treeConfig.pidType = null
      if (columns && columns.length > 0) {
        columns.forEach((item: any) => {
          if (id.toUpperCase() == item.name.toUpperCase()) {
            info.data.treeConfig.idType = item.type
          }
          if (pid.toUpperCase() == item.name.toUpperCase()) {
            info.data.treeConfig.pidType = item.type
          }
        })
      }
    }
    if (info.data.leftTree === 1) {
      if (info.data.leftTreeConfig.condField.length <= 0) {
        ElNotification({
          title: 'Error message',
          message: 'When opening the left tree, you need to configure the associated condition field',
          type: 'error',
        })
        return false
      }
    }
    if (info.data.style === 'list') {
      if (info.data.displayFields.length <= 0) {
        ElNotification({
          title: 'Error message',
          message: 'Please complete the displayed field information when saving',
          type: 'error',
        })
        return false
      }
    }
    if (info.data.style === 'tree') {
      if (
        (!info.data.treeConfig.id && info.data.treeConfig.id.length <= 0) ||
        (!info.data.treeConfig.pid && info.data.treeConfig.pid.length <= 0) ||
        (!info.data.treeConfig.showColumn &&
          info.data.treeConfig.showColumn.length <= 0)
      ) {
        ElNotification({
          title: 'Error message',
          message: 'Please complete the displayed field information when saving',
          type: 'error',
        })
        return false
      }
    }
    if (info.data.returnFields.length <= 0) {
      ElNotification({
        title: '错误提示',
        message: '保存时请完善返回字段信息',
        type: 'error',
      })
      return false
    }

    return true
  }

  // Verification form
  bizApi.bizPattern.getAllBizValidator().then((data: any) => {
    info.rule = data.varirule.rule
  })

  const changeStyle = (value: any) => {
    if (value === 'tree') {
      info.data.page = 0
    }
  }

  // Switch to data dictionary type
  const changeLeftTreeConfigType = (item: any) => {
    info.data.leftTreeConfig.key = ''
    systreeref.value.reset()
  }

  // Request when the type is data dictionary
  const queryDictTypeTree = () => {
    postData(sysApi.dict.getDictTypeTreeUrl, {}).then(
      (result) => {
        info.leftTreeData = result.data
      },
      () => {}
    )
  }

  // Select category
  const selectNodeClickFn = (row: any) => {
    // eslint-disable-next-line no-empty
    if (row.typeCode === 'system') {
    } else {
      info.data.leftTreeConfig.name = row.name
      info.data.leftTreeConfig.key = row.code
      selectRef.value.visible = false
    }
  }

  // Click the Set Parameters button
  const openpar = () => {
    info.dialogVisible = true
    queryOpenFn()
  }

  // Set parameter button request interface
  const queryOpenFn = () => {
    postData(bizApi.customDialog.bizCustDialogGetTable, {
      dsKey: info.data.dsKey,
      objType: info.data.objType,
      objName: info.data.objName,
      dataSource: info.data.dataSource,
    }).then(
      (result) => {
        info.parametersObj.tableData = result.data.columns[0].name
          ? result.data
          : { columns: [] }
      },
      () => {}
    )
  }

  // Get a list of data sources
  const getdataSourceList = () => {
        info.dataSourceOptions = [{alias:"dataSourceDefault",name:"Default data source"}];
        if (!info.data.dsKey || info.data.dsKey.length <= 0) {
          info.dataSourceOptions.forEach(
            (element: { name: string; alias: any }) => {
              if (element.name === 'Default data source') {
                info.data.dsKey = element.alias
                info.data.dsName = element.name
              }
            }
          )
        }
  }

  const changesource = (value: any) => {
    info.dataSourceOptions.forEach((element: { name: string; alias: any }) => {
      if (value === element.alias) {
        info.data.dsName = element.name
      }
    })
  }

  // Click to query
  const searchList = () => {
    if (!info.data.objType || info.data.objType.length <= 0) {
      ElMessage({
        showClose: true,
        message: 'Please select the query type first',
        type: 'error',
      })
      return false
    }
    formRef.value.validateField('objName', (valid: boolean) => {
      if (valid) {
        getData(bizApi.bizTable.BizTableSearchObjName, {
          dsKey: info.data.dsKey,
          objType: info.data.objType,
          objName: info.data.objName,
        }).then(
          ({ data }) => {
            if (data && JSON.stringify(data) !== '{}') {
              const arr = []
              for (const key in data) {
                arr.push({
                  value: key,
                  label: `${key}(${data[key]})`,
                })
              }
              info.objNameOptions = arr
              info.data.objName = info.objNameOptions[0].value
            } else {
              ElMessage({
                showClose: true,
                message: 'The query result is empty',
                type: 'error',
              })
            }
          },
          () => {}
        )
      }
    })
  }

  // Toggle selection
  const changesourceType = (val: any) => {}

  // Request dialog interface
  const getByCodeFn = () => {
    getData(`${bizApi.customDialog.getByCode}custDialog`, {}).then(
      (result) => {
        info.oneListData = result.data
      },
      () => {}
    )
  }

  const updateRowFn = (row: any) => {
    info.updateRow = row
  }

  const savetreeDialog = () => {
    info.treedialogVisible = false
    info.data.leftTreeConfig.key = info.updateRow.code_
    info.data.leftTreeConfig.name = info.updateRow.name_
  }

  // Open tree dialog box
  const openbizCustDialog = () => {
    info.treedialogVisible = true
    getByCodeFn()
  }

  // Get datatree data
  const getTreeData = async () => {
    const result = await sysApi.dict.getDictTreeUrl({
      queryParam: { typeCode$VEQ: 'system' },
    })
    info.leftTreeConfigTreeOption = result.data
  }

  const saveDialog = (row: any) => {
    info.dialogVisible = false
    parametersref.value.emitFn()
    info.data.treeConfig = info.treeConfig
    info.data.displayFields = info.displayFields
    info.data.conditionFields = info.conditionFields
    info.conditionFieldsOption = info.conditionFields.filter((item: any) => {
      return !item.value.ctrlType && item.valueSource == 'param'
    })
    info.data.returnFields = info.returnFields
    info.data.sortFields = info.sortFields

    info.parametersObj.treeConfig = info.treeConfig
    info.parametersObj.displayFields = info.displayFields
    info.parametersObj.conditionFields = info.conditionFields
    info.parametersObj.returnFields = info.returnFields
    info.parametersObj.sortFields = info.sortFields
  }

  const updateParameters = (
    treeConfig: any,
    displayFields: any,
    conditionFields: any,
    returnFields: any,
    sortFields: any
  ) => {
    info.treeConfig = treeConfig
    info.displayFields = displayFields
    info.conditionFields = conditionFields
    info.returnFields = returnFields
    info.sortFields = sortFields
  }
  const getDataFn = (data: any) => {
    info.parametersObj.treeConfig = data.treeConfig
    info.parametersObj.displayFields = data.displayFields
    info.parametersObj.conditionFields = data.conditionFields
    info.parametersObj.returnFields = data.returnFields
    info.parametersObj.sortFields = data.sortFields
    queryOpenFn()
  }
  // do not use same name with ref
  const choosezdydhklb = (list: any) => {
    info.data.leftTreeConfig.key = list[0].code
    info.data.leftTreeConfig.name = list[0].name
  }

  const backOtherFn = () => {
    emit('closeFn')
  }
  onMounted(() => {
    getTreeData()
    getdataSourceList()
    queryDictTypeTree()
  })
</script>
<style lang="scss">
  .heightformitem {
    height: 94px;
  }
  .heightformitem .el-form-item__label {
    line-height: 94px !important;
  }
</style>
