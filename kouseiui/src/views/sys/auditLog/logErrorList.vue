<template>
  <div class="comprehensive-table-container">
    <div>
      <div ref="titleForm">
        <el-row class="vab-query-form">
          <el-col class="top-panel">
            <el-form
              ref="queryForm"
              :inline="true"
              :model="query"
              style="float: left"
              @submit.prevent
            >
              <el-form-item label="Request URL" label-width="120px" prop="url$VLK">
                <el-input
                  v-model="query.url$VLK"
                  placeholder="Please enter the request URL"
                />
              </el-form-item>
              <el-form-item label="Status" label-width="62px" prop="status$VEQ">
                <el-select v-model="query.status$VEQ" placeholder="Please select status">
                  <el-option label="Unchecked" value="unchecked" />
                  <el-option label="Checked" value="checked" />
                  <el-option label="Fixed" value="fixed" />
                </el-select>
              </el-form-item>
              <el-form-item
                label="Error Description"
                label-width="120px"
                prop="content$VLK"
              >
                <el-input
                  v-model="query.content$VLK"
                  placeholder="Please enter the error description"
                />
              </el-form-item>

              <el-form-item>
                <el-button :icon="Search" type="primary" @click="search()">
                  Search
                </el-button>
                <el-button :icon="RefreshRight" @click="reset()">
                  Reset
                </el-button>
              </el-form-item>
            </el-form>
          </el-col>
          <el-col class="left-panel">
            <el-space wrap>
              <el-button
                v-ab-btn-rights:logError_del
                :disabled="!selectedData || selectedData.length == 0"
                :icon="Delete"
                type="danger"
                @click="delBySeletedIds(sysApi.sysAuditlog.logErrorRemove)"
              >
                Batch Delete
              </el-button>
              <el-button
                v-ab-btn-rights:logError_config
                :icon="Comment"
                type="primary"
                @click="openDialog()"
              >
                Message push configuration
              </el-button>
            </el-space>
          </el-col>
        </el-row>
      </div>
      <!-- Key fields are set to minwith, and specific enumeration fields are set to with (management columns, dates, status, dictionaries, etc.).
         Put unimportant fields at the end, manage column settings fixed="right"
    -->
      <ab-table
        ref="abTable"
        v-model="selectedData"
        :height="tableHeight"
        :query-param="query"
        row-key="id"
        :url="sysApi.sysAuditlog.logErrorList"
      >
        <ab-column label="Account" prop="account" width="80" />
        <ab-column label="IP Address" prop="ip" width="140" />
        <ab-column ab-template="param" label="IP Location" width="100" />
        <template #param="{ scope }">
          {{ scope.row.ipAddress ? scope.row.ipAddress : '-' }}
        </template>
        <ab-column
          ab-text-formatter="unchecked-Not checked-danger-dark|checked-Checked-warning-dark|fixed-Fixed-success-dark"
          label="Status"
          prop="status"
          width="85"
        />
        <ab-column label="Request URL" min-width="100" prop="url" />
        <ab-column label="Error Message" min-width="120" prop="content" />
        <ab-column
          ab-date-formatter="yyyy-MM-dd HH:mm"
          label="Creation Time"
          prop="createTime"
          width="160"
        />
        <ab-column ab-template="edit" fixed="right" label="Action" width="85" />
        <template #edit="{ scope }">
          <router-link
            v-ab-btn-rights:logError_edit
            :to="{
              name: 'LogErrorEdit',
              query: { id: scope.row.id },
            }"
          >
            <el-button text type="primary">Edit</el-button>
          </router-link>
        </template>
      </ab-table>
    </div>
    <el-dialog
      v-model="pageData.showDialog"
      :append-to-body="true"
      :destroy-on-close="true"
      title="Message push configuration"
      @open="openDialog()"
    >
      <el-form
        ref="formRef"
        label-position="top"
        label-suffix="："
        label-width="120px"
        :model="pageData.msgConfObj.confJson"
        :status-icon="false"
        width="30%"
      >
        <el-form-item label="Is it enabled?">
          <el-switch
            v-model="pageData.msgConfObj.isEnable"
            active-text="Yes"
            :active-value="1"
            inactive-text="No"
            :inactive-value="0"
            inline-prompt
          />
        </el-form-item>
        <el-form-item label="Staffing">
          <el-space>
            <el-radio-group
              v-model="pageData.identityType"
              @change="dialogChange()"
            >
              <el-radio-button label="user">User</el-radio-button>
              <el-radio-button label="org">Organization</el-radio-button>
              <el-radio-button label="role">Role</el-radio-button>
              <el-radio-button label="post">Position</el-radio-button>
            </el-radio-group>
            <ab-cust-dialog
              :dialog-key="pageData.dialogKey"
              style="margin-left: 10px"
              type="primary"
              @ok="setreceivers"
            >
              {{ pageData.dialogButtonDesc }}
            </ab-cust-dialog>
          </el-space>
          <div style="width: 100%; margin-top: 10px">
            <el-tag
              v-if="pageData.msgConfObj.confJson.receivers.length === 0"
              type="warning"
            >
              Not selected
            </el-tag>
            <el-tag
              v-for="(identity, index) in pageData.msgConfObj.confJson
                .receivers"
              :key="index"
              closable
              type="success"
              @close="deletereceivers(index)"
            >
              {{ identity.assign }}
            </el-tag>
          </div>
        </el-form-item>

        <el-form-item
          label="Message Type"
          prop="msgType"
          :rules="{
            required: true,
            message: 'Select at least one message type',
            trigger: 'change',
          }"
        >
          <ab-msg-type v-model="pageData.msgConfObj.confJson.msgType" />
        </el-form-item>

        <el-form-item
          label="Message content"
          prop="msgContent"
          :rules="{
            required: true,
            message: 'Please enter the message body',
            trigger: 'change',
          }"
        >
          <div style="width: 100%; margin-bottom: 10px">
            <el-tag
              v-for="(item, index) in msgTag"
              :key="index"
              @click="addParam(item)"
            >
              {{ item.desc }}
            </el-tag>
          </div>
          <!-- <el-input
            v-model="pageData.msgConfObj.confJson.msgContent"
            :rows="5"
            type="textarea"
          /> -->
          <ab-rich-editor v-model="pageData.msgConfObj.confJson.msgContent" />
        </el-form-item>
        <el-form-item label="Example">
          <!-- <el-input v-model="sampleData" readonly :rows="5" type="textarea" /> -->
          <ab-rich-editor v-model="sampleData" :disabled="true" />
        </el-form-item>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="closeDialog()">Cancel</el-button>
          <el-button type="primary" @click="saveData()">Save</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts">
  import { abTableMix, sysApi, abMsgType, abRichEditor } from 'agilebpm'
  export default {
    name: 'LogErrorList',
    mixins: [abTableMix, abMsgType, abRichEditor],
  }
</script>

<script setup lang="ts">
  import {
    Delete,
    Search,
    Comment,
    RefreshRight,
  } from '@element-plus/icons-vue'

  // Query condition definition, if ts needs to define all parameters, here override the parent class, if not, you don't need to set it, the parent class has already defined the query object
  const query = reactive({
    url$VLK: '',
    status$VEQ: '',
    content$VLK: '',
  })

  const pageData = reactive({
    showDialog: false,
    identityType: 'user',
    dialogKey: 'userSelector',
    dialogButtonDesc: 'Please select a user',
    identityTypeDesc: 'User',
    msgContentSampleData: '',
    msgConfObj: {
      id: null as any,
      confType: 'sysLogErrorMsgConf',
      isEnable: 1,
      confJson: {
        receivers: [] as any,
        msgType: '',
        enable: 1,
        msgContent:
          '<p>User: ${fullName}（${account}）, The system is operating abnormally.</p><p><br></p><p>The exception information is: ${content}</p><p><br></p><p>Appearance time: ${createTime}</p><p><br></p><p>IP location: ${ipAddress}（${ip}）</p><p><br></p><p>Please pay attention! </p><p><br></p><p>${path}</p>',
      },
    },
  })

  const msgTag = [
    { value: '${fullName}', desc: 'Username', default: 'Administrator' },
    { value: '${account}', desc: 'Account', default: 'admin' },
    {
      value: '${content}',
      desc: 'Abnormal information',
      default: 'NullPointerException: null',
    },
    {
      value: '${path}',
      desc: 'Request URL',
      default: 'http://localhost:8080/ab-bpm/bpm/instance/doAction',
    },
    {
      value: '${createTime}',
      desc: 'Appearance time',
      default: '2023-06-21 14:51:39',
    },
    { value: '${ip}', desc: 'IP', default: '172.0.0.1' },
    { value: '${ipAddress}', desc: 'IP location', default: 'Shenzhen.Baoan' },
  ]

  const openDialog = () => {
    pageData.showDialog = true
    // Request data echo
    sysApi.configuration
      .getSysConfObj('sysLogErrorMsgConf')
      .then((res: any) => {
        if (res.isOk && res.data) {
          pageData.msgConfObj.id = res.data.id
          pageData.msgConfObj.isEnable = res.data.isEnable
          pageData.msgConfObj.confJson = JSON.parse(res.data.json)
        }
      })
  }
  const closeDialog = () => {
    pageData.showDialog = false
  }

  const saveData = () => {
    pageData.showDialog = false
    const param = {
      id: pageData.msgConfObj.id,
      code: pageData.msgConfObj.confType,
      isEnable: pageData.msgConfObj.isEnable,
      json: JSON.stringify(pageData.msgConfObj.confJson),
    }
    sysApi.configuration.saveSysConfObj(param)
  }

  const dialogChange = () => {
    if (pageData.identityType == 'user') {
      pageData.identityTypeDesc = 'User'
      pageData.dialogKey = 'userSelector'
      pageData.dialogButtonDesc = 'Please select a user'
    }
    if (pageData.identityType == 'post') {
      pageData.identityTypeDesc = 'Position'
      pageData.dialogKey = 'postSelector'
      pageData.dialogButtonDesc = 'Please select a position'
    }
    if (pageData.identityType == 'org') {
      pageData.identityTypeDesc = 'Organization'
      pageData.dialogKey = 'orgSelector'
      pageData.dialogButtonDesc = 'Please select an organization'
    }
    if (pageData.identityType == 'role') {
      pageData.identityTypeDesc = 'Role'
      pageData.dialogButtonDesc = 'Please select a role'
      pageData.dialogKey = 'roleSelector'
    }
  }

  const setreceivers = (list: any) => {
    if (!list || list.length == 0) {
      return
    }
    list.forEach((item: any) => {
      // Deduplication
      const ident = pageData.msgConfObj.confJson.receivers.find(
        (ident: any) => {
          return (
            `${item.id}${pageData.identityType}` === `${ident.id}${ident.type}`
          )
        }
      )
      if (!ident) {
        pageData.msgConfObj.confJson.receivers.push({
          id: item.id,
          name: item.name,
          type: pageData.identityType,
          assign: `(${pageData.identityTypeDesc})${item.name}`,
        })
      }
    })
  }

  const deletereceivers = (index: number) => {
    pageData.msgConfObj.confJson.receivers.splice(index, 1)
  }

  const addParam = (msgTag: any) => {
    pageData.msgConfObj.confJson.msgContent =
      pageData.msgConfObj.confJson.msgContent + msgTag.value
  }

  const sampleData = computed(() => {
    let result = pageData.msgConfObj.confJson.msgContent
    msgTag.forEach((tag: any) => {
      result = result.replaceAll(tag.value, tag.default)
    })
    return result
  })
</script>
