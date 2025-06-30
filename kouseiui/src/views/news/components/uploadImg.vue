<template>
  <el-upload
    accept="image/png, image/jpeg"
    action="uploadUrl"
    :file-list="fileList"
    :http-request="uploadFn"
    list-type="picture-card"
    :on-preview="handlePictureCardPreview"
    :on-remove="handleRemove"
    :on-success="handleSuccess"
  >
    <el-icon><Plus /></el-icon>
  </el-upload>
  <el-dialog v-model="dialogVisible" width="700px">
    <div style="width: 100%; text-align: center">
      <img alt="" :src="dialogImageUrl" style="width: 100%" />
    </div>
  </el-dialog>
</template>
<script lang="ts" setup>
  import { sysApi, postData, request } from 'agilebpm'
  import { Plus } from '@element-plus/icons-vue'
  import type { UploadFile, UploadUserFile } from 'element-plus'
  const emit = defineEmits(['updateFileList'])
  const props = defineProps({
    // By default, the user will define it as Object any. If the type is specified, then: type: Object as PropType<User>,
    images: {
      type: Array as () => Array<string>, //(string can also be other custom interfaces)
      required: false,
    },
    // The key of the file type data dictionary is convenient for classifying which module the file belongs to. (Required)
    dicCode: {
      type: String,
      required: true, // Must be transmitted
    },
  })
  const dialogVisible = ref(false)
  const fileList = ref<UploadUserFile[]>([])

  const dialogImageUrl = ref('')
  watch(
    () => dialogVisible.value,
    (amount, prevamount) => {
      if (!amount) {
        dialogImageUrl.value = ''
      }
    }
  )

  // Custom upload operations
  const uploadFn = (item: any) => {
    const formData = new FormData()
    formData.append('file', item.file)
    formData.append('dicCode', props.dicCode)
    request({
      url: sysApi.sysFile.uploadUrl,
      method: 'post',
      data: formData,
      timeout: 60000,
      headers: {
        'Content-Type': 'multipart/form-data',
      },
      onUploadProgress: (progressEvent) => {
        const complete = parseInt(
          `${((progressEvent.loaded / progressEvent.total) * 100) | 0}`,
          10
        )
        // This method has a parameter "percent", give it the progress value complete
        item.onProgress({ percent: complete })
      },
    })
      .then(
        (result: any) => {
          if (result.data) {
            item.onSuccess() // After the upload is successful, change the status attribute to success
          }
          fileList.value.push({
            id: result.data,
          })
          emit('updateFileList', fileList.value)
        },
        () => {}
      )
      .catch((err: any) => {
        if (err) {
          console.log(err)
          item.onError()
          item.message = 'Upload failed'
        }
      })
  }
  const handleRemove = (file: UploadFile, fileList: UploadFile[]) => {
    emit('updateFileList', fileList)
  }

  const handlePictureCardPreview = (file: UploadFile) => {
    // eslint-disable-next-line @typescript-eslint/no-non-null-assertion
    dialogVisible.value = true
    dialogImageUrl.value = sysApi.sysFile.getViewFileUrl(file.id)
  }

  onMounted(() => {
    if (props.images) {
      fileList.value = JSON.parse(props.images)
      JSON.parse(props.images).forEach((element: any, index: any) => {
        if (element.id) {
          fileList.value[index].url = sysApi.sysFile.getViewFileUrl(element.id)
        }
      })
    }
  })
</script>
