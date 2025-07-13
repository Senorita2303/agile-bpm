<template>
  <div class="comprehensive-table-container">
    <div ref="titleForm">
      <el-row class="vab-query-form">
        <el-col class="top-panel">
          <el-form
            ref="queryForm"
            :inline="true"
            label-width="50px"
            :model="query"
            @submit.prevent
          >
            <el-form-item label="Name" prop="typeName$VLK">
              <el-input v-model="query.typeName$VLK" placeholder="Please enter a name" />
            </el-form-item>
            <el-form-item>
              <el-button icon="search" type="primary" @click="search()">
                Search
              </el-button>
              <el-button icon="refreshRight" @click="reset()">Reset</el-button>
            </el-form-item>
          </el-form>
        </el-col>
        <el-col class="left-panel">
          <router-link to="/cms/notice/noticeTypeEdit">
            <el-button icon="Plus" type="primary">Add</el-button>
          </router-link>
        </el-col>
      </el-row>
    </div>
    <!-- Key fields are set to minwith, and specific enumeration fields are set to with (management columns, dates, status, dictionaries, etc.).
         Put unimportant fields at the end and manage column settings fixed="right"
    -->
    <ab-table
      ref="abTable"
      v-model="selectedData"
      :checkable="false"
      :height="tableHeight"
      :query-param="query"
      row-key="id"
      :url="cmsNotifyTypeListUrl"
    >
      <ab-column label="Name" min-width="120" prop="typeName" />
      <ab-column label="备注" min-width="120" prop="remark" />
      <ab-column ab-template="edit" fixed="right" label="Action" width="160" />
      <template #edit="{ scope }">
        <router-link :to="{ name: 'NoticeTypeEdit', params: scope.row }">
          <el-button type="text">Edit</el-button>
        </router-link>
        <el-button
          type="text"
          @click="
            sendAction(
              cmsNotifyTypeRemoveUrl + scope.row.id,
              `Confirm to delete ${scope.row.typeName} ?`
            )
          "
        >
          Delete
        </el-button>
      </template>
    </ab-table>
  </div>
</template>

<script lang="ts">
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  import { abTableMix, cmsApi } from 'agilebpm'
  // import {
  //   cmsNotifyTypeListUrl,
  //   cmsNotifyTypeRemoveUrl,
  // } from '@/api/cms/ab-cms-notify'
  export default defineComponent({
    mixins: [abTableMix],
    setup() {
      // Query condition definition, if ts needs to define all parameters, here override the parent class, if not, you don't need to set it, the parent class has already defined the query object
      const query = reactive({
        typeName$VLK: '',
      })
      // return {
      //   query,
      //   cmsNotifyTypeListUrl,
      //   cmsNotifyTypeRemoveUrl,
      // }
    },
  })
</script>
