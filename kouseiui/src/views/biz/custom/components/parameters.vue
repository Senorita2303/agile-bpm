<template>
  <div>
    <div class="common-layout">
      <el-container v-if="state.tableData.tableData">
        <el-aside width="400px">
          <p>Get field information</p>
          <el-table
            ref="multipleTableRef"
            border
            :data="
              state.tableData.tableData.columns
                ? state.tableData.tableData.columns
                : []
            "
            height="563"
            style="width: 100%; margin-top: 10px"
            @selection-change="handleSelectionChange"
          >
            <el-table-column type="selection" width="55" />
            <el-table-column label="Fields">
              <template #default="scope">
                <el-input v-model="scope.row.name" />
              </template>
            </el-table-column>
            <el-table-column label="Type" prop="type">
              <template #default="scope">
                <el-select
                  v-model="scope.row.type"
                  clearable
                  :disabled="state.data.dataSource == 'database'"
                  placeholder=""
                >
                  <el-option label="String" value="varchar" />
                  <el-option label="Digital type" value="number" />
                  <el-option label="Large text" value="clob" />
                  <el-option label="Time" value="date" />
                </el-select>
              </template>
            </el-table-column>
            <el-table-column label="">
              <template #header>
                <span>Notes</span>
                <el-button
                  :icon="Plus"
                  size="small"
                  style="margin-left: 10px"
                  type="primary"
                  @click="addLeftList()"
                />
              </template>
              <template #default="scope">
                <el-input v-model="scope.row.comment" />
              </template>
            </el-table-column>
          </el-table>
        </el-aside>
        <div class="btnbox">
          <el-button :icon="DArrowRight" type="primary" @click="addRightList" />
        </div>
        <el-main>
          <p>Get field information</p>
          <el-tabs
            v-model="state.activeName"
            class="demo-tabs"
            @tab-click="handleClick"
          >
            <el-tab-pane label="Display fields" name="first">
              <el-form
                v-if="props.data.style === 'tree'"
                label-width="100px"
                :model="state.treeConfig"
              >
                <el-form-item label="ID">
                  <el-input v-model="state.treeConfig.id" />
                </el-form-item>
                <el-form-item label="Parent ID">
                  <el-input v-model="state.treeConfig.pid" />
                </el-form-item>
                <el-form-item label="Display name">
                  <el-input v-model="state.treeConfig.showColumn" />
                </el-form-item>
                <el-form-item label="Is it asynchronous?">
                  <el-switch
                    v-model="state.treeConfig.sync"
                    active-text="Yes"
                    inactive-text="No"
                    inline-prompt
                  />
                </el-form-item>
                <el-form-item label="Parent ID initial value">
                  <el-input
                    v-model="state.treeConfig.pidInitVal"
                    type="textarea"
                  />
                  <div>
                    <el-checkbox
                      v-model="state.treeConfig.pidInitValScript"
                      label="Script"
                      style="margin-left: 10px"
                    />
                  </div>
                </el-form-item>
              </el-form>
              <el-table
                v-else
                ref="multipleTableRef"
                border
                class="displayFieldsTable"
                :data="state.tableDatafirst"
                height="500"
                row-key="id"
                style="width: 100%; margin-top: 10px"
              >
                <el-table-column label="Field name">
                  <template #header>
                    <span style="float: left">Field name</span>
                    <el-popover
                      content="The first field is the echoed display field"
                      effect="dark"
                      placement="top-start"
                      style="float: left"
                      trigger="hover"
                      :width="200"
                    >
                      <template #reference>
                        <el-icon style="margin-top: 4px; margin-left: 3px">
                          <question-filled />
                        </el-icon>
                      </template>
                    </el-popover>
                  </template>
                  <template #default="scope">
                    <el-input v-model="scope.row.columnName" />
                  </template>
                </el-table-column>
                <el-table-column label="Display name">
                  <template #default="scope">
                    <el-input v-model="scope.row.showName" />
                  </template>
                </el-table-column>
                <el-table-column label="Format type">
                  <template #default="scope">
                    <el-select
                      v-model="scope.row.formatterType"
                      clearable
                      placeholder=""
                      @change="changeFormatterType($event, scope.row)"
                    >
                      <el-option
                        v-for="(item, index) in state.formatterTypeOption"
                        :key="index"
                        :label="item.label"
                        :value="item.value"
                      />
                    </el-select>
                  </template>
                </el-table-column>
                <el-table-column label="Formatting">
                  <template #default="scope">
                    <el-input
                      v-if="scope.row.formatterType === 'js'"
                      v-model="scope.row.formatter"
                      type="textarea"
                    />
                    <el-popover
                      v-if="scope.row.formatterType === 'json'"
                      placement="top"
                      trigger="click"
                      :width="500"
                    >
                      <template #reference>
                        <el-button size="small">Settings</el-button>
                      </template>
                      <div>
                        <el-button
                          style="margin-bottom: 4px"
                          text
                          type="primary"
                          @click="
                            scope.row.jsonList.push({
                              value: '',
                              key: '',
                              styleValue: '',
                            })
                          "
                        >
                          Add+
                        </el-button>
                        <el-table
                          :data="scope.row.jsonList"
                          style="width: 100%"
                        >
                          <el-table-column label="Database value" prop="value">
                            <template #default="scopeJson">
                              <el-input
                                v-model="scopeJson.row.value"
                                placeholder="Database value"
                                size="small"
                              />
                            </template>
                          </el-table-column>
                          <el-table-column label="Display value" prop="key">
                            <template #default="scopeJson">
                              <el-input
                                v-model="scopeJson.row.key"
                                placeholder="Display value"
                                size="small"
                              />
                            </template>
                          </el-table-column>
                          <el-table-column label="Corresponding style" prop="styleValue">
                            <template #default="scopeJson">
                              <el-select
                                v-model="scopeJson.row.styleValue"
                                class="m-2 valueStyle"
                                clearable
                                placeholder="Please select"
                                :popper-append-to-body="false"
                                size="small"
                              >
                                <el-option label="default" value="default">
                                  <span style="float: left">default</span>
                                  <div
                                    style="
                                      float: right;
                                      width: 10px;
                                      height: 10px;
                                      margin-top: 13px;
                                      background: #409eff;
                                    "
                                  ></div>
                                </el-option>
                                <el-option label="success" value="success">
                                  <span style="float: left">success</span>
                                  <div
                                    style="
                                      float: right;
                                      width: 10px;
                                      height: 10px;
                                      margin-top: 13px;
                                      background: #67c23a;
                                    "
                                  ></div>
                                </el-option>
                                <el-option label="info" value="info">
                                  <span style="float: left">info</span>
                                  <div
                                    style="
                                      float: right;
                                      width: 10px;
                                      height: 10px;
                                      margin-top: 13px;
                                      background: #909399;
                                    "
                                  ></div>
                                </el-option>
                                <el-option label="warning" value="warning">
                                  <span style="float: left">warning</span>
                                  <div
                                    style="
                                      float: right;
                                      width: 10px;
                                      height: 10px;
                                      margin-top: 13px;
                                      background: #e6a23c;
                                    "
                                  ></div>
                                </el-option>
                                <el-option label="danger" value="danger">
                                  <span style="float: left">danger</span>
                                  <div
                                    style="
                                      float: right;
                                      width: 10px;
                                      height: 10px;
                                      margin-top: 13px;
                                      background: #f56c6c;
                                    "
                                  ></div>
                                </el-option>
                              </el-select>
                            </template>
                          </el-table-column>
                          <el-table-column
                            align="center"
                            label="Action"
                            width="90"
                          >
                            <template #default="scopeJson">
                              <el-button
                                text
                                type="primary"
                                @click="delJson(scope.row, scopeJson)"
                              >
                                Delete
                              </el-button>
                            </template>
                          </el-table-column>
                        </el-table>
                      </div>
                    </el-popover>
                    <el-tag
                      v-if="
                        scope.row.formatterType === 'dict' &&
                        scope.row.formatterJson.code &&
                        scope.row.formatterJson.code.length > 0
                      "
                      closable
                      style="margin-right: 12px"
                      @close="closeQueryDict(scope.row)"
                    >
                      {{ scope.row.formatterJson.name }}
                    </el-tag>
                    <ab-cust-dialog
                      v-if="scope.row.formatterType === 'dict'"
                      dialog-key="sjzdsjq"
                      :dialog-setting="{ multiple: false }"
                      size="small"
                      style="display: inline-block"
                      @ok="chooseDictQuery($event, scope.row)"
                    >
                      Select
                    </ab-cust-dialog>
                    <el-select
                      v-if="scope.row.formatterType === 'date'"
                      v-model="scope.row.formatter"
                      clearable
                      default-first-option
                      placeholder="Please select"
                    >
                      <el-option
                        label="Year, month, day (YYYY-MM-DD)"
                        value="yyyy-MM-dd"
                      />
                      <el-option
                        label="Year month day hour minute second (YYYY-MM-DD HH:mm:ss)"
                        value="yyyy-MM-dd HH:mm:ss"
                      />
                      <el-option label="年(YYYY)" value="yyyy" />
                    </el-select>
                  </template>
                </el-table-column>
                <el-table-column label="Manage" width="110">
                  <template #default="scope">
                    <el-button
                      circle
                      class="displayFields-move-btn"
                      :icon="Sort"
                      type=""
                    />
                    <el-button
                      :icon="CloseBold"
                      size="small"
                      @click="state.tableDatafirst.splice(scope.$index, 1)"
                    />
                  </template>
                </el-table-column>
              </el-table>
            </el-tab-pane>
            <el-tab-pane label="Conditional fields" name="second">
              <el-table
                ref="multipleTableRef"
                border
                class="conditionFieldsTable"
                :data="state.tableDatasecond"
                height="500"
                row-key="id"
                style="width: 100%; margin-top: 10px"
              >
                <el-table-column label="Field name">
                  <template #default="scope">
                    <el-input v-model="scope.row.columnName" size="small" />
                  </template>
                </el-table-column>
                <el-table-column label="Display name">
                  <template #default="scope">
                    <el-input v-model="scope.row.showName" size="small" />
                  </template>
                </el-table-column>
                <el-table-column label="Condition">
                  <template #default="scope">
                    <el-select
                      v-model="scope.row.condition"
                      class="m-2"
                      placeholder="Please select"
                      size="small"
                    >
                      <template
                        v-for="item in state.tiaojianoptions
                          .ConditionTypeOption"
                        :key="item.key"
                      >
                        <el-option
                          v-if="item.supports.indexOf(scope.row.dbType) > -1"
                          :label="item.desc"
                          :value="item.key"
                        />
                      </template>
                    </el-select>
                  </template>
                </el-table-column>
                <el-table-column label="Type">
                  <template #default="scope">
                    <el-select
                      v-model="scope.row.valueSource"
                      class="m-2"
                      clearable
                      placeholder="Please select"
                      size="small"
                      @change="changeValueSource(scope.row)"
                    >
                      <template
                        v-for="item in state.laiyuanoptions
                          .BizCustDialogConditionOption"
                        :key="item.key"
                      >
                        <el-option
                          v-if="!item.show"
                          :label="item.desc"
                          :value="item.key"
                        />
                      </template>
                    </el-select>
                  </template>
                </el-table-column>
                <el-table-column label="Default value">
                  <template #header>
                    <span style="float: left">Default value</span>
                    <el-popover
                      content="1: Run as script enclosed in #{}, eg:#{sysScript.getCurrentUserName()}
                        2: ${} represents constants, as follows:
                        ${currentUserId}: Current user ID (string)
                        ${currentUserName}: Current username (string)
                        ${currentUserAccount}: Current user account (string)
                        ${currentOrgId}: Current user organization ID (string)
                        ${currentOrgName}: Current user organization name (string)
                        ${currentOrgCode}: Current user organization code (string)
                        ${currentDateTime}: Current time (Date, can only be used in date fields)"
                      effect="dark"
                      placement="top-start"
                      style="float: left"
                      trigger="hover"
                      :width="200"
                    >
                      <template #reference>
                        <el-icon style="margin-top: 4px; margin-left: 3px">
                          <question-filled />
                        </el-icon>
                      </template>
                    </el-popover>
                  </template>
                  <template #default="scope">
                    <el-input
                      v-if="scope.row.valueSource === 'fixedValue'"
                      v-model="scope.row.value.text"
                      placeholder="Please enter "
                      size="small"
                    />
                    <el-input
                      v-if="scope.row.valueSource === 'inputText'"
                      v-model="scope.row.value.text"
                      placeholder="Please enter "
                      size="small"
                    />
                    <el-tag
                      v-if="
                        scope.row.valueSource === 'dict' &&
                        scope.row.value.text &&
                        scope.row.value.text.length > 0
                      "
                      closable
                      style="margin-right: 12px"
                      @close="closeDict(scope.row)"
                    >
                      {{ scope.row.value.ctrlType }}
                    </el-tag>
                    <ab-cust-dialog
                      v-if="scope.row.valueSource === 'dict'"
                      dialog-key="sjzdsjq"
                      :dialog-setting="{ multiple: false }"
                      size="small"
                      style="display: inline-block"
                      @ok="chooseDict($event, scope.row)"
                    >
                      Select
                    </ab-cust-dialog>
                    <el-select
                      v-if="scope.row.valueSource === 'date'"
                      v-model="scope.row.value.text"
                      clearable
                      default-first-option
                      placeholder="Please select"
                      size="small"
                    >
                      <el-option
                        label="Year, month, day (YYYY-MM-DD)"
                        value="YYYY-MM-DD"
                      />
                      <el-option
                        label="Year month day hour minute second (YYYY-MM-DD HH:mm:ss)"
                        value="YYYY-MM-DD HH:mm:ss"
                      />
                      <el-option label="Year (YYYY)" value="YYYY" />
                    </el-select>
                    <!-- <el-select
                      v-if="scope.row.valueSource === 'param'"
                      v-model="scope.row.value.ctrlType"
                      class="m-2"
                      placeholder="Please select"
                      size="small"
                      style="float: left; width: 90px"
                    >
                      <el-option key="" label="Dynamic parameters" value="" />
                      <el-option
                        key="inputText"
                        label="Single-line text box"
                        value="inputText"
                      />
                    </el-select> -->
                  </template>
                </el-table-column>
                <el-table-column label="Manage" width="110">
                  <template #default="scope">
                    <el-button
                      circle
                      class="conditionFields-move-btn"
                      :icon="Sort"
                      type=""
                    />
                    <el-button
                      :icon="CloseBold"
                      size="small"
                      @click="state.tableDatasecond.splice(scope.$index, 1)"
                    />
                  </template>
                </el-table-column>
              </el-table>
            </el-tab-pane>
            <el-tab-pane label="Return field" name="third">
              <el-table
                ref="multipleTableRef"
                border
                class="returnFieldsTable"
                :data="state.tableDatathird"
                height="500"
                row-key="id"
                style="width: 100%; margin-top: 10px"
              >
                <el-table-column label="Field name">
                  <template #header>
                    <span style="float: left">Field name</span>
                    <el-popover
                      content="Please do not return the field name “parent”、“children”、“isParent”、“childCount” the fields may be abnormal in some scenarios."
                      effect="dark"
                      placement="top-start"
                      style="float: left"
                      trigger="hover"
                      :width="200"
                    >
                      <template #reference>
                        <el-icon style="margin-top: 4px; margin-left: 3px">
                          <question-filled />
                        </el-icon>
                      </template>
                    </el-popover>
                  </template>
                  <template #default="scope">
                    <el-input v-model="scope.row.columnName" />
                  </template>
                </el-table-column>
                <el-table-column label="Notes">
                  <template #default="scope">
                    <el-input v-model="scope.row.showName" />
                  </template>
                </el-table-column>
                <el-table-column label="Return name">
                  <template #default="scope">
                    <el-input v-model="scope.row.returnName" />
                  </template>
                </el-table-column>
                <el-table-column label="Manage" width="110">
                  <template #default="scope">
                    <el-button
                      circle
                      class="returnFields-move-btn"
                      :icon="Sort"
                      type=""
                    />
                    <el-button
                      :icon="CloseBold"
                      size="small"
                      @click="state.tableDatathird.splice(scope.$index, 1)"
                    />
                  </template>
                </el-table-column>
              </el-table>
            </el-tab-pane>
            <el-tab-pane label="Sort fields" name="fourth">
              <el-table
                ref="multipleTableRef"
                border
                class="sortFieldsTable"
                :data="state.tableDatafourth"
                height="500"
                row-key="id"
                style="width: 100%; margin-top: 10px"
              >
                <el-table-column label="Field name">
                  <template #default="scope">
                    <el-input v-model="scope.row.columnName" />
                  </template>
                </el-table-column>
                <el-table-column label="Sorting Type">
                  <template #default="scope">
                    <el-select
                      v-model="scope.row.sortType"
                      class="m-2"
                      placeholder="Select"
                      size="small"
                    >
                      <el-option
                        v-for="item in state.paixuoptions"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value"
                      />
                    </el-select>
                  </template>
                </el-table-column>
                <el-table-column label="Manage" width="110">
                  <template #default="scope">
                    <el-button
                      circle
                      class="sortFields-move-btn"
                      :icon="Sort"
                      type=""
                    />
                    <el-button
                      :icon="CloseBold"
                      size="small"
                      @click="state.tableDatafourth.splice(scope.$index, 1)"
                    />
                  </template>
                </el-table-column>
              </el-table>
            </el-tab-pane>
          </el-tabs>
        </el-main>
      </el-container>
    </div>
  </div>
</template>
<script setup lang="ts">
  import { reactive, ref } from 'vue'
  import { sysApi } from 'agilebpm'
  import {
    DArrowRight,
    ArrowUpBold,
    ArrowDownBold,
    CloseBold,
    Plus,
    QuestionFilled,
    Sort,
  } from '@element-plus/icons-vue'
  const props = defineProps({
    parametersObj: {
      type: Object,
      default: null,
    },
    data: {
      type: Object,
      required: true,
    },
  })
  const emit = defineEmits(['updateTableData', 'updateTreeData'])
  const state: any = reactive({
    data: props.data,
    multipleSelection: [],
    activeName: 'first',
    tableData: props.parametersObj,
    tableDatafirst: [],
    treeConfig: {
      id: '',
      pid: '',
      showColumn: '',
      sync: false,
      pidInitVal: '',
      pidInitValScript: false,
    },
    tableDatasecond: [],
    tiaojianoptions: props.parametersObj,
    laiyuanoptions: props.parametersObj,
    tableDatathird: [],
    tableDatafourth: [],
    paixuoptions: [
      {
        label: 'Ascending',
        value: 'asc',
      },
      {
        label: 'Descending',
        value: 'desc',
      },
    ],
    formatterTypeOption: [
      {
        label: 'js',
        value: 'js',
      },
      {
        label: 'json',
        value: 'json',
      },
      {
        label: 'Data dictionary',
        value: 'dict',
      },
      {
        label: 'Date',
        value: 'date',
      },
    ],
  })
  const multipleTableRef = ref<InstanceType<typeof ElTable>>()
  const toggleSelection = (rows?: User[]) => {
    if (rows) {
      rows.forEach((row) => {
        // TODO: improvement typing when refactor table
        // eslint-disable-next-line @typescript-eslint/ban-ts-comment
        // @ts-expect-error
        multipleTableRef.value?.toggleRowSelection(row, undefined)
      })
    } else {
      multipleTableRef.value?.clearSelection()
    }
  }
  const handleSelectionChange = (val: User[]) => {
    state.multipleSelection = val
  }

  const activeName = ref('first')

  const handleClick = (tab: TabsPaneContext, event: Event) => {
    console.log(tab, event)
  }
  // Click to add
  const addLeftList = () => {
    state.tableData.tableData.columns.push({
      comment: '',
      decimal: 0,
      length: 50,
      name: '',
      primary: 0,
      required: false,
      type: 'varchar',
    })
  }
  // Click the middle button
  const addRightList = () => {
    if (state.multipleSelection.length > 0) {
      if (state.activeName === 'first') {
        state.multipleSelection.forEach(
          (element: { name: any; comment: any }, index: any) => {
            state.tableDatafirst.push({
              index: index,
              columnName: element.name,
              showName: element.comment,
              formatter: '',
              formatterType: 'js',
              id:generateRandom()
            })
          }
        )
      }
      if (state.activeName === 'second') {
        state.multipleSelection.forEach(
          (element: { name: any; comment: any }, index: any) => {
            state.tableDatasecond.push({
              index: index,
              columnName: element.name,
              showName: element.comment,
              condition: state.tiaojianoptions.ConditionTypeOption[0].key,
              dbType: element.type,
              valueSource:
                state.laiyuanoptions.BizCustDialogConditionOption[1].key,
              value: { text: '' },
              id:generateRandom()
            })
          }
        )
      }
      if (state.activeName === 'third') {
        state.multipleSelection.forEach(
          (element: { name: any; comment: any; primary: any }, index: any) => {
            let returnName = ''
            returnName = element.name.tuoFeng()
            if (returnName.endsWith('_')) {
              returnName = returnName.substring(0, returnName.length - 1)
            }
            state.tableDatathird.push({
              columnName: element.name,
              showName: element.comment,
              returnName: returnName,
              primary: element.primary,
              id:generateRandom()
            })
          }
        )
      }
      if (state.activeName === 'fourth') {
        state.multipleSelection.forEach((element: any, index: any) => {
          state.tableDatafourth.push({
            index: index,
            columnName: element.name,
            sortType: state.paixuoptions[0].value,
            id:generateRandom()
          })
        })
      }
    }
    emitFn()
  }
  const changeValueSource = (row: any) => {
    row.value.text = ''
  }
  // Select dialog box OK event
  const chooseDict = (list: any, row: any) => {
    row.value.text = list[0].code
    row.value.ctrlType = list[0].name
  }
  // Select the query dialog box OK event
  const chooseDictQuery = (list: any, row: any) => {
    row.formatterJson.code = list[0].code
    row.formatterJson.name = list[0].name
    sysApi.dict.getDictTreeByCodeNoRoot(row.formatterJson.code).then((data) => {
      row.formatterJson.dictList = data
    })
  }
  // Delete
  const closeDict = (row: any) => {
    row.value.text = ''
    row.value.ctrlType = ''
  }
  // Delete
  const closeQueryDict = (row: any) => {
    row.formatterJson.code = ''
    row.formatterJson.name = ''
    row.formatterJson.dictList = []
  }
  // Line drag
  const rowDrop = () => {
     
  }
  rowDrop()
  const changeFormatterType = (value: any, row: any) => {
    if (value == 'js') {
      row.formatter = ''
    }
    if (value == 'json') {
      if (!row.jsonList) {
        row.jsonList = []
      }
    }
    if (value == 'date') {
      row.formatter = 'yyyy-MM-dd'
    }
    if (value == 'dict') {
      row.formatter = ''
      row.formatterJson = {}
    }
  }
  const emitFn = () => {
    // Processing json data
    state.tableDatafirst.forEach((item: any, index: any) => {
      if (item.formatterType == 'json') {
        item.formatter = JSON.stringify(item.jsonList)
      }
      if (item.formatterType == 'dict') {
        item.formatter = JSON.stringify(item.formatterJson)
      }
    })
    emit(
      'updateTableData',
      state.treeConfig,
      state.tableDatafirst,
      state.tableDatasecond,
      state.tableDatathird,
      state.tableDatafourth
    )
  }
  const delJson = (row: any, scopeJson: any) => {
    row.jsonList.splice(scopeJson.$index, 1)
  }

  const generateRandom = () => {
    return Math.random().toString(16).slice(2)
  }

  onMounted(() => {
    state.treeConfig = props.parametersObj.treeConfig
      ? props.parametersObj.treeConfig
      : {
          id: '',
          pid: '',
          showColumn: '',
          sync: false,
          pidInitVal: '',
          pidInitValScript: false,
        }
    state.tableDatafirst = props.parametersObj.displayFields
      ? props.parametersObj.displayFields
      : []
    state.tableDatafirst.forEach((item: any, index: any) => {
      if(!item.id){
        item.id = generateRandom()
      }
      item.index = index
      if (item.formatterType == 'json') {
        if (item.formatter && item.formatter.length > 0) {
          item.jsonList = JSON.parse(item.formatter)
        } else {
          item.jsonList = []
        }
      }
      if (item.formatterType == 'dict') {
        if (item.formatter && item.formatter.length > 0) {
          item.formatterJson = JSON.parse(item.formatter)
        } else {
          item.formatterJson = []
        }
      }
    })
    state.tableDatasecond = props.parametersObj.conditionFields
      ? props.parametersObj.conditionFields
      : []
    state.tableDatasecond.forEach((item: any, index: any) => {
      if(!item.id){
        item.id = generateRandom()
      }
      item.index = index
      // Compatible with old data
      if (item.valueSource == 'param' && item.value.ctrlType == 'inputText') {
        item.valueSource = 'inputText'
      }
      if (item.valueSource == 'param' && item.value.ctrlType == '') {
        item.valueSource = 'dynamicParam'
      }
    })
    state.tableDatathird = props.parametersObj.returnFields
      ? props.parametersObj.returnFields
      : []
    state.tableDatathird.forEach((item: any, index: any) => {
      if(!item.id){
        item.id = generateRandom()
      }
      item.index = index
    })
    state.tableDatafourth = props.parametersObj.sortFields
      ? props.parametersObj.sortFields
      : []
    state.tableDatafourth.forEach((item: any, index: any) => {
      if(!item.id){
        item.id = generateRandom()
      }
      item.index = index
    })
    if (
      state.laiyuanoptions.BizCustDialogConditionOption &&
      props.data.style == 'tree'
    ) {
      state.laiyuanoptions.BizCustDialogConditionOption.forEach(
        (item: any, index: any) => {
          if (item.key == 'fixedValue') {
            item.show = false
          } else if (item.key == 'dynamicParam') {
            item.show = false
          } else {
            item.show = true
          }
        }
      )
    }
  })
  defineExpose({
    emitFn,
  })
</script>
<style lang="scss" scoped>
  .demo-tabs > .el-tabs__content {
    padding: 32px;
    font-size: 32px;
    font-weight: 600;
    color: #6b778c;
  }
  .btnbox {
    padding: 0 20px;
    line-height: 500px;
  }
</style>
