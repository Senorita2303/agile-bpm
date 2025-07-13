<template>
  <div class="common-layout" style="overflow: hidden;">
    <div class="left-box" width="350px">
      <tree
        ref="treeRef"
        :loading-des="state.loading"
        @add-fn="addaFn"
        @enable-app-fn="enableAppFn"
        @reset-fields="resetFields"
        @update-api-button-data="updateApiButtonDataFn"
        @update-fetchdata="updatedesdata"
        @update-loading="loadingFn"
      />
    </div>
    <div class="right-box">
      <div v-show="state.form.parentId">
        <div v-if="state.isSave" class="flex" style="padding: 15px">
          <el-button
            :loading="state.saveloading"
            type="primary"
            @click="saveFn"
          >
            Save
          </el-button>
          <el-button @click="cancelFn">Cancel</el-button>
        </div>
        <div v-else class="flex" style="padding: 15px">
          <el-button type="primary" @click="editFn">Edit</el-button>
          <!-- <el-button
              v-if="state.form.id && state.form.id.length > 0"
              @click="removeFn"
            >
              Delete
            </el-button> -->
        </div>
        <el-divider style="margin: 0 0 20px 0" />
        <el-form
          ref="formRef"
          v-loading="state.loading"
          :inline="false"
          label-width="160px"
          :model="state.form"
          :rules="state.rules"
        >
          <el-form-item label="Resource name" prop="name">
            <ab-pinyin
              v-model="state.form.name"
              v-model:to="state.form.code"
              :disabled="!state.isSave"
            />
          </el-form-item>
          <el-form-item label="Parent resource name" prop="parentName">
            <span>{{ state.form.parentName }}</span>
          </el-form-item>
          <el-form-item
            v-if="state.BizValidatorMap.varirule"
            label="Code"
            prop="code"
            :rules="[
              { required: true, message: 'Required' },
              state.BizValidatorMap.varirule.rule,
            ]"
          >
            <el-input
              v-model="state.form.code"
              :disabled="!state.isSave"
              placeholder="Resource code, globally unique"
            />
          </el-form-item>
          <el-form-item label="Request URL" prop="url">
            <el-tooltip
              class="item"
              content="Buttons and links need to correctly configure the request link, which will be used for system authentication"
              effect="dark"
              placement="right-end"
            >
              <el-input
                v-model="state.form.url"
                :disabled="!state.isSave"
                placeholder="Please enter the request address"
              />
            </el-tooltip>
          </el-form-item>
          <!-- <el-form-item label="Type" prop="type">
            <el-radio-group v-model="state.form.type" :disabled="!state.isSave">
              <el-radio-button label="menu">Menu</el-radio-button>
              <el-radio-button label="button">Button</el-radio-button>
              <el-radio-button label="API">API</el-radio-button>
            </el-radio-group>
          </el-form-item> -->
          <el-form-item label="Status" prop="enable">
            <el-radio-group
              v-model="state.form.enable"
              :disabled="!state.isSave"
            >
              <el-radio-button :label="0">Disable</el-radio-button>
              <el-radio-button :label="1">Enable</el-radio-button>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="Default expansion" prop="opened">
            <el-radio-group
              v-model="state.form.opened"
              :disabled="!state.isSave"
            >
              <el-radio :label="1">Yes</el-radio>
              <el-radio :label="0">No</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item
            v-if="state.enableApp == 1"
            label="Mobile phone icon"
            prop="icon"
          >
            <ab-choose-svg v-model="state.form.icon" />
            <svg-icon
              :icon="state.form.icon"
              style="width: 30px; height: 30px; margin-left: 12px"
            />
          </el-form-item>
          <el-form-item v-else label="Font icons" prop="icon">
            <el-input
                v-model="state.form.icon"
                placeholder="Please enter the font icon"
              />
            <!-- <el-icon>
              <component :is="'el-icon-menu'"/>
            </el-icon> -->
            <!-- <choose-icon-new
              v-model="state.form.icon"
              :disabled="!state.isSave"
            /> -->
          </el-form-item>

          <el-form-item label="Order">
            <span>Automatically generate after saving</span>
          </el-form-item>
          <el-form-item label="Related resources">
            <el-table
              border="1"
              :data="state.apiButtonTableData"
              style="width: 90%"
              
            >
              <el-table-column label="Resource name" prop="name" width="130">
                <template #default="scope">
                  <el-input
                    v-model="scope.row.name"
                    placeholder="Please enter a resource name"
                    size="small"
                  />
                </template>
              </el-table-column>
              <el-table-column label="Code" prop="code">
                <template #default="scope">
                  <el-input v-model="scope.row.code" placeholder="Please enter the code"  size="small"/>
                </template>
              </el-table-column>
              <el-table-column label="Authentication address" prop="url">
                <template #default="scope">
                  <el-input
                    v-model="scope.row.url"
                    placeholder="Please enter the authentication address"
                    size="small"
                  />
                </template>
              </el-table-column>
              <el-table-column label="Type" prop="type" width="150">
                <template #default="scope">
                  <el-radio-group v-model="scope.row.type"  size="small">
                    <el-radio-button label="button">Button</el-radio-button>
                    <el-radio-button label="API">API</el-radio-button>
                  </el-radio-group>
                </template>
              </el-table-column>
              <el-table-column label="Status" prop="enable" width="150">
                <template #default="scope">
                  <el-radio-group v-model="scope.row.enable" size="small">
                    <el-radio-button :label="0">Disable</el-radio-button>
                    <el-radio-button :label="1">Enable</el-radio-button>
                  </el-radio-group>
                </template>
              </el-table-column>
              <el-table-column label="Action" prop="enable" width="120">
                <template #header>
                  <span>Action</span>
                  <el-button
                    size="small"
                    style="float: right"
                    type="primary"
                    @click="addApi"
                  >
                    Add
                  </el-button>
                </template>
                <template #default="scope">
                  <el-button
                    size="small"
                    type="text"
                    @click="delApiButton(scope.row, scope.$index)"
                  >
                    Delete
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-form-item>
        </el-form>
      </div>
      <div v-show="!state.form.parentId">
        <el-empty description="Click on the resource on the left to view details" />
      </div>
    </div>
  </div>
</template>

<script setup >
  import { reactive, ref, getCurrentInstance } from 'vue'
  import { ElMessage, ElMessageBox } from 'element-plus'
  import tree from './components/tree.vue'
  // import RemixIcon from './components/remixIcon.vue'
  import { abChooseSvg, chooseIconNew, svgIcon } from 'agilebpm'
  const $baseLoading = inject('$baseLoading')
  const formRef = ref('formRef')
  const treeRef = ref('treeRef')
  import { bizApi, authApi } from 'agilebpm'
  const state = reactive({
    BizValidatorMap: {},
    form: {
      name: '',
      code: '',
      url: '',
      type: 'menu',
      enable: 1,
      opened: 0,
      icon: '',
      children: [],
      checked: false,
    },
    rules: {
      name: [{ required: true, trigger: 'blur', message: 'Required' }],
      code: [{ required: true, trigger: 'blur', message: 'Required' }],
    },
    saveloading: false,
    loading: false,
    isSave: false,
    enableApp: 0,
    apiButtonTableData: [],
  })
  bizApi.bizPattern.getAllBizValidator().then((data) => {
    state.BizValidatorMap = data
  })
  const addApi = () => {
    state.apiButtonTableData.unshift({
      name: '',
      code: '',
      url: '',
      type: 'button',
      enable: 1,
      parentId: state.form.id,
      appId: state.form.appId,
    })
  }
  // Update details on the right
  const updatedesdata = (data) => {
    state.isSave = true
    state.form = data
  }
  // loading box control
  const loadingFn = (bool) => {
    state.loading = bool
  }
  // Click to edit
  const editFn = () => {
    state.isSave = true
  }
  // Cancel
  const cancelFn = () => {
    formRef.value.clearValidate()
    state.isSave = false
  }
  // Save
  const saveFn = () => {
    formRef.value.validate(async (valid) => {
      if (valid) {
        state.saveloading = true
        try {
          authApi.saveResource(state.form).then(
            (data) => {
              state.form.children =
                state.apiButtonTableData.length > 0
                  ? state.apiButtonTableData
                  : []
              if (state.form.children.length > 0) {
                state.form.children.forEach((item) => {
                  item.parentId = data.data
                })
              }
              authApi.saveTreeUrl(state.form).then(
                (dataa) => {
                  ElMessage({
                    type: 'success',
                    message: 'Save successfully',
                    onClose: () => {},
                  })
                  state.saveloading = false
                  // Refresh tree
                  treeRef.value.fetchTreeDataAndData(data.data)
                  state.isSave = false
                },
                () => {
                  state.saveloading = false
                }
              )
            },
            () => {
              state.saveloading = false
            }
          )
        } catch (err) {
          state.saveloading = false
        }
      }
    })
  }
  const resetFields = () => {
    state.form = {}
  }
  // Click to add
  const addaFn = (row) => {
    state.isSave = true
    state.loading = true
    setTimeout(() => {
      formRef.value.resetFields()
      state.form = {
        name: '',
        code: '',
        url: '',
        type: 'menu',
        enable: 1,
        opened: 0,
        icon: '',
        children: [],
        checked: false,
      }
      state.apiButtonTableData = []
      Object.assign(state.form, {
        appId: row.appId,
        parentId: row.id,
      })
      state.form.parentName = row.name
      state.loading = false
    }, 500)
  }
  const enableAppFn = (enableApp) => {
    state.enableApp = enableApp
    state.form.parentId = false
  }
  const updateApiButtonDataFn = (data) => {
    state.apiButtonTableData = data
  }
  const delApiButton = (row, index) => {
    ElMessageBox.confirm(`Confirm to delete【${row.name}】is this ${row.type}`, 'Message', {
      confirmButtonText: 'Confirm',
      cancelButtonText: 'Cancel',
      type: 'warning',
    }).then(() => {
      if (row.id) {
        authApi.removeResource({ id: row.id }).then(({ msg }) => {
          ElMessage({
            type: 'success',
            message: 'Deleted',
            onClose: () => {},
          })
          // Refresh tree
          treeRef.value.fetchTreeDataAndData(row.parentId)
        })
      } else {
        state.apiButtonTableData.splice(index, 1)
      }
    })
  }
</script>

<style lang="scss" scoped>
  .selectstyle {
    width: 100%;
    margin-bottom: 10px;
  }
  .custom-tree-node {
    display: flex;
    flex: 1;
    align-items: center;
    justify-content: space-between;
    padding-right: 8px;
    font-size: 14px;
  }
  .el-input {
    max-width: 500px;
  }
  .left-box {
    float: left;
    width: 350px;
    border-right: 10px solid #f5f7f9;
  }
  .right-box {
    float: left;
    width: calc(100% - 350px);
  }
</style>
