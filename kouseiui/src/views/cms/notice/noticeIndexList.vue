<template>
  <div>
    <el-card shadow="hover">
      <div v-for="item in info.notifyList" :key="item.id" class="divStyle">
        <router-link
          :to="{
            name: 'NoticeDetails',
            query: { id: item.id, type: 'NoticeIndexList' },
          }"
        >
          <div class="step title">{{ item.title }}</div>
          <div class="step tap">
            <el-tag v-if="item.isRead == 1" class="ml-2" type="success">
              Read
            </el-tag>
            <el-tag v-else class="ml-2" type="danger">Unread</el-tag>
          </div>
          <div class="step">{{ item.typeIdName }}</div>
          <div class="step date">{{ item.releaseTime }}</div>
          <div class="step custom">Read ({{ item.visitNum }})</div>
          <div class="step custom">Comments ({{ item.commentsNum }})</div>
        </router-link>
      </div>
    </el-card>
    <!--Pagination-->
    <el-pagination
      v-model:currentPage="info.query.currentPage"
      v-model:page-size="info.query.pageSize"
      :background="true"
      :disabled="false"
      layout=" prev, pager, next, jumper,total, sizes"
      :page-sizes="[2, 5, 10, 20, 30, 40, 50]"
      :total="info.total"
      @current-change="pageChange"
      @size-change="pageChange"
    />
  </div>
</template>

<script lang="ts">
  export default {
    name: 'NoticeIndexList',
  }
</script>
<script lang="ts" setup>
  import { cmsApi } from 'agilebpm'

  const info = reactive({
    notifyList: [] as {
      id: string
      title: string
      typeIdName: string
      isRead: number
      commentsNum: number
      visitNum: number
      releaseTime: string
    }[],
    query: {
      pageSize: 10,
      currentPage: 1,
      sortColumn: 'create_time_',
      sortOrder: 'DESC',
    },
    total: 0,
  })

  onActivated(() => {
    cmsApi.notify.getNotifyPage(info.query).then((result: any) => {
      info.total = result.data.total
      info.notifyList = result.data.rows
    })
  })

  const pageChange = () =>
    cmsApi.notify.getNotifyPage(info.query).then((result: any) => {
      info.total = result.data.total
      info.notifyList = result.data.rows
    })
</script>

<style scoped>
  .fontweight {
    font-size: 20px;
    font-weight: bolder;
    letter-spacing: 2px;
  }

  .step {
    display: inline-block;
    width: 120px;
    height: 50px;
    overflow: hidden;
    font-size: 14px;
    color: #3e3a39;
    text-align: center;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  .title {
    min-width: 40%;
    font-size: 17px;
    color: #2d8cf0;
    text-align: left;
  }
  .tap {
    width: 80px;
  }
  .date {
    width: 160px;
    font-size: 12px;
  }
  .custom {
    width: 55px;
    font-size: 12px;
  }
  .divStyle {
    width: 100%;
  }
</style>
