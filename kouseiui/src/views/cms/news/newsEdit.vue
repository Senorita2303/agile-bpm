<template>
  <el-container class="layout-container-demo">
    <el-header>
      <ab-save
        back-name="NewsList"
        :before-save-fn="handlebeforesaveFn"
        :form-ref="formRef"
        :save-data="info.data"
        :url="cmsApi.news.cmssaveOrUpdateUrl"
      />
      <ab-load v-model="info.data" :url="cmsApi.news.cmsNewsUrl" />
    </el-header>
    <el-divider class="dividermar" />
    <el-main>
      <el-scrollbar>
        <el-form
          ref="formRef"
          label-suffix="："
          label-width="140px"
          :model="info.data"
          :status-icon="false"
        >
          <el-form-item
            label="Title"
            placeholder="Please enter a title"
            prop="title"
            :rules="[
              {
                required: true,
                message: 'Required',
              },
              {
                message: 'Length is less than 100',
                max: 100,
              },
            ]"
          >
            <el-input v-model="info.data.title" />
          </el-form-item>
          <el-form-item
            label="Content"
            prop="content"
            :rules="[{ required: true, message: 'Required' }]"
          >
            <ab-rich-editor v-model="info.data.content" />
          </el-form-item>
          <el-form-item
            label="Carousel Image"
            :rules="[{ required: true, message: 'Required' }]"
          >
            <UploadImg
              v-if="id && info.data.images.length > 0"
              dic-code="news"
              :images="info.data.images"
              @update-file-list="updateimages"
            />
            <UploadImg
              v-else
              dic-code="news"
              @update-file-list="updateimages"
            />
          </el-form-item>
          <el-form-item label="Upload">
            <!-- <ab-upload-file
              v-if="id && info.data.attachments.length > 0"
              v-model="info.data.attachments"
              dic-code="news"
              size="small"
            /> -->
            <!-- <ab-upload-file
              v-else
              v-model="info.data.attachments"
              dic-code="news"
              size="small"
            /> -->
             <ab-upload-file
              v-model="info.data.attachments"
              dic-code="news"
              size="small"
            />
          </el-form-item>
        </el-form>
      </el-scrollbar>
    </el-main>
  </el-container>
</template>

<script setup lang="ts">
  import { reactive, ref } from 'vue'
  import { ElMessage } from 'element-plus'
  import UploadImg from './components/uploadImg.vue'
  import { abRichEditor, cmsApi, abTools, abUploadFile } from 'agilebpm'
  const formRef = ref()
  // Import current instance
  const { proxy } = abTools.useCurrentInstance()
  const id = proxy.$route.query.id
  const info: any = reactive({
    data: {
      title: '',
      content: '',
      attachments: '',
      images: [],
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
    if (!info.data.content || info.data.content.length <= 0) {
      ElMessage({
        message: 'Please fill in the content',
        type: 'warning',
      })
      return false
    }
    if (info.data.images.length <= 0) {
      ElMessage({
        message: 'Please upload the carousel image',
        type: 'warning',
      })
      return false
    }
    return true
  }

  // Update the slideshow file
  const updateimages = (fileList: any) => {
    info.data.images = []
    fileList.forEach((element: any) => {
      if (element.response) {
        info.data.images.push({
          name: element.raw.name,
          id: element.response.data,
        })
      } else {
        info.data.images.push({
          name: element.name,
          id: element.id,
        })
      }
    })
    info.data.images = JSON.stringify(info.data.images)
  }
</script>
