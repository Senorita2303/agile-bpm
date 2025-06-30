<template>
  <el-container class="layout-container-demo">
    <el-header>
      <ab-save
        back-name="NoticeList"
        :before-save-fn="handlebeforesaveFn"
        :form-ref="formRef"
        :save-data="info.data"
        :url="
          id ? cmsApi.notify.cmsNotifyUpdateUrl : cmsApi.notify.cmsNotifySaveUrl
        "
      />
      <ab-load
        v-model="info.data"
        :url="cmsApi.notify.cmsNotifyGetUrl"
        @after-fn="afterFn"
      />
    </el-header>
    <el-divider class="dividermar" />
    <el-main>
      <el-scrollbar>
        <el-form
          ref="formRef"
          label-suffix="："
          label-width="170px"
          :model="info.data"
          :status-icon="false"
        >
          <el-form-item
            label="Title"
            placeholder="Please enter a title"
            prop="title"
            :rules="[{ required: true, message: 'Required' }]"
          >
            <el-input v-model="info.data.title" />
          </el-form-item>

          <el-form-item
            label="Notice Type"
            prop="typeId"
            :rules="[{ required: true, message: 'Required' }]"
          >
            <el-select v-model="info.data.typeId" value-key="key">
              <el-option
                v-for="item in info.arr"
                :key="item.code"
                :label="item.name"
                :value="item.code"
              />
            </el-select>
          </el-form-item>

          <el-form-item
            label="Department to Notify"
            :rules="[{ required: true, message: 'Required' }]"
          >
            <template v-if="info.cmsNotifyShareList.length > 0">
              <el-tag
                v-for="(tag, index) in info.cmsNotifyShareList"
                :key="index"
                class="mx-1"
                closable
                @close="handleClose(tag)"
              >
                {{ tag.groupName ? tag.groupName : tag.name }}
              </el-tag>
            </template>

            <ab-cust-dialog
              v-model="info.cmsNotifyShareList"
              dialog-key="orgSelector"
              :style="{
                marginLeft: info.cmsNotifyShareList.length > 0 ? '10px' : '0px',
              }"
              type="primary"
            >
              Select
            </ab-cust-dialog>
          </el-form-item>
          <el-form-item
            label="Content"
            prop="content"
            :rules="[{ required: true, message: 'Required' }]"
          >
            <ab-rich-editor v-model="info.data.content" />
          </el-form-item>
          <el-form-item label="Upload">
            <!-- <ab-upload-file
              v-if="id && info.data.attachments.length > 0"
              v-model="info.data.attachments"
              dic-code="notice"
            />
            <ab-upload-file
              v-else
              v-model="info.data.attachments"
              dic-code="notice"
            /> -->
            <ab-upload-file
              v-model="info.data.attachments"
              dic-code="notice"
            />
          </el-form-item>
        </el-form>
      </el-scrollbar>
    </el-main>
  </el-container>
</template>

<script setup lang="ts">
  import {
    sysApi,
    cmsApi,
    abRichEditor,
    abUploadFile,
    abTools,
  } from 'agilebpm'
  import { reactive, ref, onMounted } from 'vue'
  import { ElMessage } from 'element-plus'
  const formRef = ref()
  // Import current instance
  const { proxy } = abTools.useCurrentInstance()
  const id = proxy.$route.query.id
  const typeAlloptions = ref([])
  const info: any = reactive({
    cmsNotifyShareList: [],
    arr: [],
    data: {
      title: '',
      typeId: '',
      groupIdList: [],
      content: '',
      attachments: '',
    },
  })
  // Before saving, check that each submitted field is not empty
  const handlebeforesaveFn = () => {
    if (info.data.title.length <= 0) {
      ElMessage({
        message: 'Please fill in the title',
        type: 'warning',
      })
      return false
    }

    info.data.groupIdList = []
    info.cmsNotifyShareList.forEach((element: { groupId: any; id: any }) => {
      info.data.groupIdList.push(element.groupId ? element.groupId : element.id)
    })
    if (info.data.groupIdList.length <= 0) {
      ElMessage({
        message: 'Please select the notification department',
        type: 'warning',
      })
      return false
    }
    if (!info.data.content || info.data.content.length <= 0) {
      ElMessage({
        message: 'Please fill in the content',
        type: 'warning',
      })
      return false
    }
    return true
  }
  const handleClose = (tag: string) => {
    info.cmsNotifyShareList.splice(info.cmsNotifyShareList.indexOf(tag), 1)
  }
  // Get classification (data dictionary)
  sysApi.dict.getDictListByCode('gglx').then((result) => {
    info.arr = result
  })
  // After modifying the notification department, it is rendered as a tag display
  const getdepart = (tags: any) => {
    info.cmsNotifyShareList = tags
  }

  const afterFn = (data: any) => {
    info.cmsNotifyShareList = data.cmsNotifyShareList
  }

  onMounted(() => {})
</script>
