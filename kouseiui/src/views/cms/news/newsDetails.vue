<template>
  <div class="detail-container">
    <el-button :icon="Back" style="margin-left: 20px" @click="back">
      Back
    </el-button>

    <AbPageHeader back-router-name="NewsList" content="News details" />
    <div class="detailsbox">
      <h5>{{ info.data.title }}</h5>
      <p v-if="info.data.createTime" class="pspan">
        <span>Post Time: {{ info.data.createTime }}</span>
        <span>Views:（{{ info.data.visitNum }}）</span>
        <span>Comments：（{{ info.data.commentsNum }}）</span>
      </p>
      <el-divider v-if="info.data.createTime" border-style="dashed" />

      <el-carousel
        v-if="info.data.img && info.data.img.length > 0"
        arrow="always"
        height="400px"
        indicator-position="outside"
        style="
          width: 100%;
          padding: 12px;
          margin-bottom: 40px;
          border: 1px solid #dcdfe6;
          border-radius: 10px;
        "
      >
        <el-carousel-item
          v-for="item in info.data.img"
          :key="item"
          style="text-align: center"
        >
          <div
            class="carouselBox"
            :style="{
              background: `url(${item}) no-repeat center center`,
              backgroundSize: 'auto 100%',
            }"
          ></div>
          <!-- <el-image :src="item" /> -->
        </el-carousel-item>
      </el-carousel>

      <div v-html="info.data.content"></div>
      <div v-if="info.data.attachments && info.data.attachments.length > 0">
        <span>Attachment:</span>
        <span
          v-for="item in JSON.parse(info.data.attachments)"
          :key="item.id"
          class="linkspan"
          @click="downClickFn(item)"
        >
          {{ item.name }}
        </span>
      </div>
      <div v-if="info.cmsCommentsList.length > 0">
        <h4>All comments</h4>
        <div
          v-for="item in info.cmsCommentsList"
          :key="item.id"
          style="padding: 12px 8px"
        >
          <div style="overflow: hidden">
            <span style="float: left; font-size: 16px; font-weight: 600">
              {{ item.creator }}
            </span>
            <span style="float: right; font-size: 14px; color: #666">
              {{ item.createTime }}
            </span>
          </div>
          <div
            style="padding: 12px 0; font-size: 16px; color: #666"
            v-html="item.commentContent"
          ></div>
          <div style="padding-bottom: 10px; text-align: right">
            <el-button
              v-if="form"
              size="small"
              type="danger"
              @click="deleteComment(item)"
            >
              Delete
            </el-button>
          </div>
          <el-divider style="margin: 8px 0" />
        </div>
      </div>
      <div v-if="!form">
        <h3>Comments</h3>
        <ab-rich-editor v-model="ruleFrom.commentContent" :height="150" />
        <div style="margin-top: 12px; text-align: right">
          <el-button type="primary" @click="savecommentContent">
            Post a comment
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
  import {
    cmsApi,
    getData,
    postData,
    abRichEditor,
    sysApi,
    abTools,
  } from 'agilebpm'
  import { Back } from '@element-plus/icons-vue'
  import { ElMessage, ElMessageBox } from 'element-plus'
  // Determine which page the message came from, and return the corresponding page if it is returned. 
  const type = getCurrentInstance()?.proxy?.$route.query.type // Defined request parameter object generics

  // Import current instance
  const { proxy } = abTools.useCurrentInstance()
  const id = proxy.$route.query.id
  const form = proxy.$route.query.form

  const ruleFrom: any = reactive({
    commentContent: '',
  })
  const info: any = reactive({
    data: {},
    cmsCommentsList: [],
  })
  // Download attachment
  const downClickFn = (file: any) => {
    abTools.downFile(file)
  }

  const back = () => {
    abTools.closeTab(proxy.$route.path)
    if (!type) {
      proxy.$router.push({
        name: 'Root',
      })
    } else if (type == 'NewsIndexList') {
      proxy.$router.push({
        name: 'NewsIndexList',
      })
    } else if (type == 'NewsList') {
      proxy.$router.push({
        name: 'NewsList',
      })
    } else {
      proxy.$router.push({
        name: 'Portal',
        params: {
          code: type,
        },
      })
    }
  }

  // Post a comment
  const savecommentContent = () => {
    const reg = /(?!<(img|video).*?>)<.*?>/g
    // strings are the parsed content, excluding tags
    const strings = ruleFrom.commentContent.replace(reg, '')
    if (strings <= 0) {
      ElMessage({
        message: 'Please enter your comment content first',
        type: 'error',
      })
      return false
    }
    postData(cmsApi.comments.savecmsCommentsUrl, {
      msgId: id,
      commentType: 1,
      commentContent: ruleFrom.commentContent,
    }).then((result) => {
      ElMessage({
        message: result.message,
        type: 'success',
      })
      ruleFrom.commentContent = ''
      getCommentsList()
    })
  }
  // Delete
  const deleteComment = (row: any) => {
    ElMessageBox.confirm('Are you sure you want to delete this comment?', 'Message', {
      confirmButtonText: 'Confirm',
      cancelButtonText: 'Cancel',
      type: 'warning',
    })
      .then(() => {
        postData(cmsApi.comments.removecmsCommentsUrl + row.id, {}).then(
          (result) => {
            ElMessage({
              message: result.message,
              type: 'success',
            })
            getCommentsList()
          }
        )
      })
      .catch(() => {})
  }
  const getCommentsList = () => {
    getData(cmsApi.news.cmsNewsUrl + id, {}).then((result) => {
      info.cmsCommentsList = result.data.cmsCommentsList
    })
  }
  onMounted(() => {
    getData(cmsApi.news.cmsNewsUrl + id, {}).then((result) => {
      info.data = result.data
      info.cmsCommentsList = info.data.cmsCommentsList
      info.data.img = []
      JSON.parse(info.data.images).forEach((element: any) => {
        if (element.id) {
          info.data.img.push(sysApi.sysFile.getViewFileUrl(element.id))
        }
        // request({
        //   url: downloadUrl + element.id,
        //   method: 'get',
        //   responseType: 'blob',
        // }).then((result) => {
        //   const blob = new Blob([result])
        //   const imageUrl = window.URL.createObjectURL(blob)
        //   info.data.img.push(imageUrl)
        //   info.data.img.forEach((item: any) => {
        //     const img = new Image()
        //     // Image address
        //     img.src = item
        //     img.onload = function () {
        //       console.log('width:' + img.width + ',height:' + img.height)
        //     }
        //   })
        // })
      })
    })
  })
</script>
<style lang="scss" scoped>
  .detailsbox {
    width: 1000px;
    margin: 0 auto;
    h5 {
      margin: 12px 0;
      font-size: 20px;
      color: #3892f2;
      text-align: center;
    }
    .pspan {
      font-size: 14px;
      color: #afafaf;
      text-align: center;
      span {
        margin: 0 10px;
      }
    }
    .linkspan {
      margin-left: 18px;
      text-decoration: underline;
      cursor: pointer;
    }
  }
  .el-carousel__item {
    overflow: auto;
  }
  .carouselBox {
    height: 100%;
  }
</style>
