<template>
  <div class="common-layout">
    <el-container>
      <el-aside class="left-tree">
        <ab-dict-tree
          v-model="info.query.queryParam.typeCode$VIN"
          type-code="biz"
          @node-click="nodeClickFn"
        />
      </el-aside>
      <el-main>
        <div class="comprehensive-table-container">
          <div ref="titleForm">
            <el-row class="vab-query-form">
              <el-col class="top-panel">
                <el-form
                  ref="queryForm"
                  :inline="true"
                  label-width="120px"
                  :model="info.query.queryParam"
                  @submit.prevent
                >
                  <el-form-item label="Dictionary Name" prop="name$VLK">
                    <el-input
                      v-model="info.query.queryParam.name$VLK"
                      placeholder="Please enter the dictionary name"
                      @keyup.enter.native="listFn()"
                    />
                  </el-form-item>
                  <el-form-item label="Dictionary Code" prop="code$VLK">
                    <el-input
                      v-model="info.query.queryParam.code$VLK"
                      placeholder="Please enter the dictionary code"
                      @keyup.enter.native="listFn()"
                    />
                  </el-form-item>
                  <el-form-item>
                    <el-button :icon="Search" type="primary" @click="listFn()">
                      Search
                    </el-button>
                    <el-button :icon="RefreshRight" @click="resetquery()">
                      Reset
                    </el-button>
                  </el-form-item>
                </el-form>
              </el-col>
              <el-col class="left-panel">
                <el-button
                  :icon="Plus"
                  type="primary"
                  @click="addClickFn"
                >
                  Add
                </el-button>
              </el-col>
            </el-row>
          </div>

          <el-table
            ref="abTable"
            v-loading="info.loading"
            border
            :data="info.tableData"
            :height="info.tableHeight"
            style="width: 100%; margin-top: 20px"
            @cell-click="clickTable"
          >
            <el-table-column label="Dictionary Name" min-width="120" prop="name" />
            <el-table-column label="Dictionary Key" min-width="120" prop="code" />
            <el-table-column
              align="center"
              label="Is System"
              prop="isSystem"
              width="90"
            >
              <template #default="scope">
                <el-tag
                  v-if="scope.row.isSystem == 1"
                  effect="dark"
                  type="success"
                >
                  Yes
                </el-tag>
                <el-tag
                  v-if="scope.row.isSystem == 0"
                  effect="dark"
                  type="warning"
                >
                  No
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column
              format="yyyy-MM-dd HH:mm"
              label="Update Time"
              min-width="120"
              prop="updateTime"
            />
            <el-table-column label="Updater" min-width="120" prop="updater" />
            <el-table-column
              align="left"
              fixed="right"
              label="Action"
              prop="key"
              width="140"
            >
              <template #default="scope">
                <el-button
                  text
                  type="primary"
                  @click.stop="editClickFn(scope.row)"
                >
                  Edit
                </el-button>
                <el-button
                  v-if="scope.row.isSystem !== 1"
                  text
                  type="primary"
                  @click.stop="removeDict(scope.row)"
                >
                  Delete
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <!--分页-->
          <el-pagination
            v-model:currentPage="info.query.currentPage"
            v-model:page-size="info.query.pageSize"
            :background="true"
            :disabled="false"
            layout=" prev, pager, next, jumper,total, sizes"
            :page-sizes="[2, 5, 10, 20, 30, 40, 50]"
            :total="info.total"
            @current-change="listFn"
            @size-change="listFn"
          />
        </div>
      </el-main>
      <el-aside
        v-loading="info.loadingdictData"
        class="right-tree"
        @contextmenu.prevent
      >
        <ab-tree
          v-if="info.dictTreeData.length > 0"
          :batch-operation="true"
          :dynamic-operation="info.dynamicOperation"
          :tree-data="info.dictTreeData"
          @batch-operation-click="batchOperationClickFn"
          @reload-click="dictreloadClickFn"
        >
          <p
            v-if="!info.dictTreeData[0].children"
            style="color: #ff4d4f; text-align: center"
          >
            （右键添加字典项）
          </p>
        </ab-tree>
        <div v-else>
          <el-empty description="Please select the data dictionary to maintain" :image-size="120" />
        </div>
      </el-aside>
    </el-container>
    <el-dialog v-model="info.dialogVisible" title="Data dictionary" width="30%">
      <el-form
        ref="ruleFormRef"
        class="demo-ruleForm"
        label-width="70px"
        :model="info.ruleForm"
      >
        <el-form-item
          label="Type"
          prop="typeCode"
          :rules="[{ required: true, message: 'Required' }]"
        >
          <ab-select-tree
            v-model="info.ruleForm.typeCode"
            :has-root="false"
            type-code="biz"
          />
        </el-form-item>
        <el-form-item
          label="Name"
          prop="name"
          :rules="[{ required: true, message: 'Required' }]"
        >
          <ab-pinyin
            v-model="info.ruleForm.name"
            v-model:to="info.ruleForm.code"
            placeholder="Please enter a name"
          />
        </el-form-item>
        <el-form-item
          label="Code"
          prop="code"
          :rules="[{ required: true, message: 'Required' }]"
        >
          <ab-code
            v-model="info.ruleForm.code"
            :disabled="info.ruleForm.id"
            placeholder="Please enter the code"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button text type="primary" @click="info.dialogVisible = false">
            Cancel
          </el-button>
          <el-button
            :loading="info.saveLoading"
            type="primary"
            @click="submitForm(ruleFormRef)"
          >
            Save
          </el-button>
        </span>
      </template>
    </el-dialog>
    <el-dialog v-model="info.dictdialogVisible" title="Dictionary items" width="500">
      <el-table
        class="dataDictList"
        :data="info.dictTableData"
        height="300"
        row-key="snRowKey"
        style="width: 100%"
      >
        <el-table-column label="Serial number" prop="sn" width="80">
          <template #default="scope">
            <el-input v-model="scope.row.sn" :min="1" type="number" />
          </template>
        </el-table-column>
        <el-table-column label="Name" prop="name">
          <template #default="scope">
            <ab-pinyin
              v-model="scope.row.name"
              v-model:to="scope.row.code"
              placeholder="Please enter "
            />
          </template>
        </el-table-column>
        <el-table-column label="Code" prop="code">
          <template #default="scope">
            <el-input v-model="scope.row.code" placeholder="Please enter " />
          </template>
        </el-table-column>
        <el-table-column label="Is it disabled?" prop="sn" width="90">
          <template #default="scope">
            <el-switch
              v-model="scope.row.disable"
              :active-value="1"
              :inactive-value="0"
            />
          </template>
        </el-table-column>
        <el-table-column label="Add" prop="code" width="90">
          <template #header>
            <span>Add</span>
            <el-button
              circle
              :icon="Plus"
              size="small"
              style="margin-left: 12px"
              type="primary"
              @click="addDictList"
            />
          </template>
          <template #default="scope">
            <el-button
              circle
              class="move-btn"
              :icon="Sort"
              size="small"
              type="primary"
            />
            <el-button
              circle
              :icon="Delete"
              size="small"
              type="danger"
              @click="removeDictList(scope.row, scope.$index)"
            />
          </template>
        </el-table-column>
      </el-table>
      <template #footer>
        <span class="dialog-footer">
          <el-button
            text
            type="primary"
            @click="info.dictdialogVisible = false"
          >
            Cancel
          </el-button>
          <el-button
            :loading="info.dictsaveLoading"
            type="primary"
            @click="dictsubmitForm()"
          >
            Save
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts">
  import {
    // abTableMix,
    sysApi,
    abTools,
    getData,
    postData,
    abDictTree,
  } from 'agilebpm'
  import { remove } from 'lodash'
  // export default {
  //   mixins: [abTableMix],
  // }
</script>
<script setup lang="ts">
  // eslint-disable-next-line no-unused-vars
  import { reactive, defineComponent, ref, nextTick } from 'vue'
  import {
    Search,
    Plus,
    RefreshRight,
    Delete,
    Sort,
  } from '@element-plus/icons-vue'
  import Sortable from 'sortablejs'
  import { ElMessage, ElMessageBox } from 'element-plus'
  import type { FormInstance } from 'element-plus'
  // Import current instance
  const { proxy } = abTools.useCurrentInstance()
  // Query condition definition, if ts needs to define all parameters, here override the parent class, if not, you don't need to set it, the parent class has already defined the query object
  const ruleFormRef = ref<FormInstance>()

  const info: any = reactive({
    tableHeight: `${window.innerHeight - 340}px`,
    loading: false,
    total: 0,
    query: {
      pageSize: 10,
      currentPage: 1,
      sortColumn: '',
      sortOrder: '',
      queryParam: {
        name$VLK: '',
        typeCode$VIN: '',
      },
    },
    tableData: [],
    treeData: [],
    dialogVisible: false,
    ruleForm: {
      dictType: 'dict',
      typeName: '',
      typeCode: '',
      name: '',
      code: '',
    },
    options: [
      {
        label: '',
        options: [],
      },
    ],
    // Dynamically display button configuration based on whether it is built-in data
    dynamicOperation: {
      conlem: 'isSystem',
      conlemOption: [
        {
          conlemValue: 1,
          option: ['add', 'refresh'],
        },
        {
          conlemValue: 0,
          option: ['add', 'edit', 'delete', 'refresh'],
        },
      ],
    },

    saveLoading: false,
    treeDataLoading: false,
    loadingDictData: false,
    dictTreeData: [],
    dictKey: '',
    dictdialogVisible: false,
    dictsaveLoading: false,

    dictrow: {},
    isEdit: false,
    typeName: '',
    dictTableData: [],
    testcode: false,
  })
  // Watch the value of a two-way binding to monitor changes
  watch(
    () => info.dialogVisible,
    (amount, prevamount) => {
      if (!amount) {
        info.ruleForm.name = ''
        info.ruleForm.code = ''
        info.ruleForm.typeName = info.typeName
        info.ruleForm.typeCode = info.query.queryParam.typeCode$VIN
      }
      nextTick(() => {
        ruleFormRef.value.clearValidate()
      })
    }
  )

  // const restaurants = ref<RestaurantItem[]>([])
  // const querySearch = (queryString: string, cb: any) => {
  //   const results = queryString
  //     ? restaurants.value.filter(createFilter(queryString))
  //     : restaurants.value
  //   // call callback function to return suggestions
  //   cb(results)
  // }
  // Request left tree list
  const getDataTree = () => {
    sysApi.dict
      .getDictTreeUrl({
        queryParam: {
          typeCode$VEQ: 'system',
        },
      })
      .then((result) => {
        info.treeData = result.data
      })
  }
  // Click to add
  const addaFn = (row: any) => {}
  // Request list
  const listFn = () => {
    info.loading = true
    postData(sysApi.dict.getDictListUrl, info.query).then((result) => {
      info.tableData = result.data.rows
      info.total = result.data.total
      info.loading = false
    })
  }
  const resetquery = () => {
    info.query.currentPage = 1
    info.query.queryParam.name$VLK = ''
    info.query.queryParam.code$VLK = ''
    listFn()
  }
  // Click on the left tree node
  const nodeClickFn = (row: any) => {
    info.query.currentPage = 1
    if (row.parentId == '0') {
      info.query.queryParam.typeCode$VIN = ''
      info.ruleForm.typeCode = ''
      info.ruleForm.typeName = ''
      info.typeName = ''
      listFn()
      return
    }
    if (row.code) {
      info.ruleForm.typeCode = row.code
      info.ruleForm.typeName = row.name
      info.typeName = row.name
    } else {
      info.ruleForm.typeCode = ''
      info.ruleForm.typeName = ''
      info.typeName = ''
    }
    listFn()
  }

  // Click to add
  const addClickFn = () => {
    info.ruleForm = { dictType: 'dict' }
    info.ruleForm.typeCode = info.query.queryParam.typeCode$VIN
    info.dialogVisible = true
  }
  // Click to edit
  const editClickFn = (row: any) => {
    info.dialogVisible = true
    info.ruleForm = JSON.parse(JSON.stringify(row))
  }

  // Click to delete
  const removeDict = (row: any) => {
    if (row.id) {
      ElMessageBox.confirm('Are you sure to delete? ', 'Message', {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        type: 'warning',
      })
        .then(() => {
          getData(sysApi.dict.removeDataDictUrl + row.id, {}).then(
            ({ msg }) => {
              ElMessage({
                showClose: true,
                message: msg,
                type: 'success',
              })
              info.dictTreeData = []
              listFn()
            }
          )
        })
        .catch(() => {})
    }
  }

  // Submit form
  const submitForm = async (formEl: FormInstance | undefined) => {
    if (!formEl) return
    await formEl.validate((valid, fields) => {
      if (valid) {
        info.saveLoading = true
        postData(sysApi.dict.saveDataDictUrl, {
          ...info.ruleForm,
          dictKey: info.ruleForm.code,
        })
          .then(
            ({ msg }) => {
              ElMessage({
                showClose: true,
                message: 'Save successfully',
                type: 'success',
              })
              listFn()
              info.dialogVisible = false
              info.saveLoading = false
            },
            () => {
              info.saveLoading = false
            }
          )
          .catch(() => {
            info.saveLoading = false
          })
      } else {
        console.log('error submit!', fields)
      }
    })
  }
  const getDictData = () => {
    info.loadingDictData = true
    sysApi.dict.getDictTreeByCodeNoCache(info.dictKey).then((result) => {
      info.dictTreeData = result.data
      info.loadingDictData = false
    })
  }
  // Click table
  const clickTable = (row: any, column: any, cell: any, event: any) => {
    nextTick(() => {
      info.dictKey = row.dictKey
      getDictData()
    })
  }
  // Click on Batch Operation
  const batchOperationClickFn = (row: any) => {
    info.dictdialogVisible = true
    rowDrop()
    info.dictrow = row
    info.dictTableData = []
    if (info.dictrow.children) {
      info.dictTableData = JSON.parse(JSON.stringify(info.dictrow.children))
      info.dictTableData.forEach((item: any, index: any) => {
        item.snRowKey = index + 1
        item.children = []
      })
    }
  }
  const isRepeat = (arr: any) => {
    const hash = {}
    for (const i in arr) {
      if (hash[arr[i].code]) {
        return true
      }
      // If the element does not exist, the value is assigned to true. You can assign any value and modify the if judgment condition accordingly
      hash[arr[i].code] = true
    }
    return false
  }
  // Submit form
  const dictsubmitForm = () => {
    let isOk = true
    info.dictTableData.forEach((item: any) => {
      if (item.name.length <= 0 || item.code.length <= 0) {
        isOk = false
      }
    })
    if (!isOk) {
      ElMessage({
        showClose: true,
        message: 'Please complete the list',
        type: 'warning',
      })
      return false
    }

    // Sort the array and check if the previous and next items are equal

    if (isRepeat(info.dictTableData)) {
      ElMessage({
        showClose: true,
        message: 'Coding repetition',
        type: 'error',
      })
      return false
    }
    info.dictsaveLoading = true
    postData(sysApi.dict.saveBatchDataDictUrl, info.dictTableData)
      .then(
        ({ msg }) => {
          info.dictdialogVisible = false
          info.dictsaveLoading = false
          ElMessage({
            showClose: true,
            message: msg,
            type: 'success',
          })
          getDictData()
        },
        () => {
          getDictData()
          info.dictsaveLoading = false
        }
      )
      .catch(() => {
        info.dictsaveLoading = false
      })
  }
  // reloadClick
  const dictreloadClickFn = (row: any) => {
    getDictData()
  }

  // Delete button click
  const removeDictList = (row: any, index: any) => {
    ElMessageBox.confirm('Are you sure to delete?', 'Message', {
      confirmButtonText: 'Confirm',
      cancelButtonText: 'Cancel',
      type: 'warning',
    })
      .then(() => {
        if (row.id) {
          getData(sysApi.dict.removeDataDictUrl + row.id, {}).then(
            ({ msg }) => {
              ElMessage({
                showClose: true,
                message: msg,
                type: 'success',
              })
              getDictData()
              info.dictTableData.splice(index, 1)
            }
          )
        } else {
          info.dictTableData.splice(index, 1)
        }
      })
      .catch(() => {})
  }

  const addDictList = () => {
    info.dictTableData.push({
      snRowKey: info.dictTableData.length + 1,
      sn: info.dictTableData.length + 1,
      name: '',
      code: '',
      dictType: 'node',
      dictKey: info.dictKey,
      parentId: info.dictrow.id,
      typeCode: info.dictrow.typeCode,
    })
  }
  // Drag and drop to sort
  // Line drag
  const rowDrop = () => {
    setTimeout(() => {
      const table = document.querySelector(
        '.dataDictList .el-table__body-wrapper tbody'
      )
      Sortable.create(table, {
        animation: 300,
        handle: '.move-btn',
        onEnd: (evt: any) => {
          const t = info.dictTableData.splice(evt.oldIndex, 1)[0]
          info.dictTableData.splice(evt.newIndex, 0, t)
          info.dictTableData.forEach((item: any, index: any) => {
            item.sn = index + 1
          })
        },
      })
    })
  }

  onMounted(() => {
    getDataTree()
    listFn()
  })
</script>
