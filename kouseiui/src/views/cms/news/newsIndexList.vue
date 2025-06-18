<template>
  <div>
    <el-card shadow="hover">
      <div v-for="(item, index) in info.newsList" :key="item.id">
        <div class="divStyle">
          <el-carousel height="100px" indicator-position="outside">
            <el-carouselItem v-for="image in item.imagesurl" :key="image">
              <img class="imgStyle" :src="image" />
            </el-carouselItem>
          </el-carousel>
        </div>

        <router-link
          :to="{
            name: 'NewsDetails',
            query: { id: item.id, type: 'NewsIndexList' },
          }"
        >
          <div class="titleDiv commonDiv">
            {{ item.title }}
          </div>
          <div class="contentDiv commonDiv">
            {{ filterHTMLTag(item.content) }}
          </div>
          <div class="dateDiv commonDiv">
            {{ item.releaseTime }} &nbsp; &nbsp; &nbsp; &nbsp; Read ({{
              item.visitNum
            }}) &nbsp; &nbsp; Comments ({{ item.commentsNum }})
          </div>
        </router-link>
        <el-divider v-if="index != info.newsList.length - 1" />
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
    name: 'NewsIndexList',
  }
</script>

<script lang="ts" setup>
  import { sysApi, cmsApi } from 'agilebpm'
  const info = reactive({
    newsList: [] as {
      id: string
      content: string
      images: string
      releaseTime: string
      imagesurl: string[]
      title: string
      commentsNum: string
      visitNum: string
    }[],
    query: {
      pageSize: 10,
      currentPage: 1,
      sortColumn: 'create_time_',
      sortOrder: 'DESC',
    },
    total: 0,
  })

  const pageChange = () =>
    cmsApi.news.getNewsPage(info.query).then((result: any) => {
      info.total = result.data.total
      info.newsList = result.data.rows
      if (info.newsList)
        for (const i in info.newsList) {
          info.newsList[i].imagesurl = []
          JSON.parse(info.newsList[i].images).forEach(
            (element: any) => {
              if (element.id) {
                info.newsList[i].imagesurl.push(
                  sysApi.sysFile.getViewFileUrl(element.id)
                )
              }
            }
            // getDownload(element.id).then((result) => {
            //     const blob = new Blob([result])
            //     const imageUrl = window.URL.createObjectURL(blob)
            //     info.newsList[i].imagesurl.push(imageUrl)
            //   })
          )
        }
    })

  pageChange()

  const filterHTMLTag = (msg: string) => {
    msg = msg || ''
    msg = msg.replace(/<\/?[^>]*>/g, '') // Remove HTML Tag
    msg = msg.replace(/[|]*\n/, '') // Remove trailing spaces
    msg = msg.replace(/&npsp;/gi, '') // Remove npsp
    return msg
  }
</script>

<style scoped>
  .fontweight {
    font-size: 20px;
    font-weight: bolder;
    letter-spacing: 2px;
  }
  .titleDiv {
    font-size: 17px;
    font-weight: bold;
  }
  .contentDiv {
    height: 50px;
    margin-top: 12px;
    overflow: auto;
    font-size: 15px;
    line-height: 160%;
  }
  .dateDiv {
    margin-top: 20px;
    font-size: 12px;
  }
  .imgStyle {
    width: 200px;
    height: 100px;
  }
  .divStyle {
    float: left;
    width: 180px;
    height: 100px;
  }

  .commonDiv {
    margin-left: 220px;
    overflow: auto;
    color: #3e3a39;
    letter-spacing: 1.3px;
  }
</style>
