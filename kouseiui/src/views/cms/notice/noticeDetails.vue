<template>
  <div class="detail-container">
    <el-button :icon="Back" style="margin-left: 20px" @click="back">
      Back
    </el-button>
    <AbPageHeader back-router-name="NoticeList" content="Details" />
    <div class="detailsbox">
      <h5>{{ info.data.title }}</h5>
      <p v-if="info.data.createTime" class="pspan">
        <span>Post Time: {{ info.data.createTime }}</span>
        <span>Views: ({{ info.data.visitNum }})</span>
        <span>Comments: ({{ info.data.commentsNum }})</span>
      </p>
      <el-divider v-if="info.data.createTime" border-style="dashed" />
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
      <div
        v-if="
          info.data.cmsNotifyShareList &&
          info.data.cmsNotifyShareList.length > 0
        "
        style="margin-top: 20px"
      >
        <span class="bmspan">Department to Notify</span>
        <el-tag v-for="item in info.data.cmsNotifyShareList" :key="item.id">
          {{ item.groupName }}
        </el-tag>
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
        <h4>Comments</h4>
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
  import { cmsApi, postData, getData, abTools, abRichEditor } from 'agilebpm'
  import { reactive } from 'vue'
  import { Back } from '@element-plus/icons-vue'
  import { ElMessage, ElMessageBox } from 'element-plus'
  // Import current instance
  const { proxy } = abTools.useCurrentInstance()
  const id = proxy.$route.query.id
  const form = proxy.$route.query.form
  // Determine which page the message came from, and return the corresponding page if it is returned
  const type = getCurrentInstance()?.proxy?.$route.query.type // Defined request parameter object generics

  const ruleFrom: any = reactive({
    commentContent: '',
  })

  const info: any = reactive({
    cmsCommentsList: [],
    data: {
      createTime: '',
    },
  })

  // Click on the attachment to download
  const downClickFn = (file: any) => {
    abTools.downFile(file)
  }

  const back = () => {
    abTools.closeTab(proxy.$route.path)
    if (!type) {
      proxy.$router.push({
        name: 'Root',
      })
    } else if (type == 'NoticeIndexList') {
      proxy.$router.push({
        name: 'NoticeIndexList',
      })
    } else if (type == 'NoticeList') {
      proxy.$router.push({
        name: 'NoticeList',
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
      commentType: 0,
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
  const getCommentsList = () => {
    getData(cmsApi.notify.cmsNotifyGetUrl + id, {}).then((result) => {
      info.data = result.data
      info.cmsCommentsList = result.data.cmsCommentsList
    })
  }
  getCommentsList()
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
    .bmspan {
      margin-right: 10px;
      font-size: 14px;
      font-weight: 600;
    }
    .linkspan {
      margin-left: 18px;
      text-decoration: underline;
      cursor: pointer;
    }
  }
</style>
