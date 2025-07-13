<template>
  <div class="comprehensive-table-container">
    <div ref="titleForm">
      <el-row class="vab-query-form">
        <el-col class="top-panel">
          <el-form
            ref="queryForm"
            :inline="true"
            label-width="62px"
            :model="query"
            @submit.prevent
          >
            <el-form-item label="Title" prop="title$VLK">
              <el-input v-model="query.title$VLK" placeholder="Please enter a title" />
            </el-form-item>
            <el-form-item label="Status" prop="status$VEQ">
              <el-select v-model="query.status$VEQ" placeholder="Please select">
                <el-option label="Unpublished" :value="0" />
                <el-option label="Published" :value="1" />
                <el-option label="Deprecated" :value="2" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button :icon="Search" type="primary" @click="search()">
                Search
              </el-button>
              <el-button :icon="RefreshRight" @click="reset()">Reset</el-button>
            </el-form-item>
          </el-form>
        </el-col>
        <el-col class="left-panel">
          <router-link to="/cms/notice/noticeEdit">
            <el-button :icon="Plus" type="primary">Add</el-button>
          </router-link>
          <el-button
            :disabled="!selectedData || selectedData.length == 0"
            :icon="Delete"
            style="margin-left: 12px"
            type="danger"
            @click="delBySeletedIds(cmsApi.notify.cmsNotifyRemoveUrl)"
          >
            Batch Delete
          </el-button>
        </el-col>
      </el-row>
    </div>
    <!-- Key fields are set to minwith, and specific enumeration fields are set to with (management columns, dates, status, dictionaries, etc.).
      Put unimportant fields at the end and manage column settings fixed="right"
    -->
    <ab-table
      ref="abTable"
      v-model="selectedData"
      :height="tableHeight"
      :query-param="query"
      row-key="id"
      :url="cmsApi.notify.cmsNotifyListUrl"
    >
      <ab-column label="Title" width="100" prop="title" />
      <ab-column label="Announcement Type" width="160" prop="typeIdName" />
      <ab-column
        ab-text-formatter="0-Unpublished-default-light|1-Published-success-light|2-Deprecated-danger-light"
        aligin="center"
        label="Status"
        prop="status"
        width="120"
      />
      <ab-column label="Release Name" prop="releaseName" width="120" />
      <ab-column label="Release Time" prop="releaseTime" width="120" />
      <ab-column
        ab-template="cmsNotifyShareList"
        label="Department to Notify"
        min-width="150"
      />
      <template #cmsNotifyShareList="{ scope }">
        <span v-for="item in scope.row.cmsNotifyShareList" :key="item.id">
          {{ item.groupName + ' ' }}
        </span>
      </template>
      <ab-column label="Visit Num" prop="visitNum" width="90" />
      <ab-column label="Comments Num" prop="commentsNum" width="150" />
      <ab-column ab-template="edit" fixed="right" label="Action" width="290" />
      <template #edit="{ scope }">
        <router-link
          v-if="
            scope.row.status === 1 ||
            scope.row.status === 0 ||
            scope.row.status === 2
          "
          :to="{
            name: 'NoticeDetails',
            query: { id: scope.row.id, form: true, type: 'NoticeList' },
          }"
        >
          <el-button text type="primary">Details</el-button>
        </router-link>
        <el-button
          v-if="scope.row.status === 1"
          text
          type="primary"
          @click="
            sendAction(
              cmsApi.notify.cmsNotifywithdrawNotifyUrl + scope.row.id,
              `Confirm to remove ${scope.row.title} ?`
            )
          "
        >
          Remove
        </el-button>
        <router-link
          v-if="scope.row.status === 0 || scope.row.status === 2"
          :to="{ name: 'NoticeEdit', query: { id: scope.row.id } }"
        >
          <el-button text type="primary">Edit</el-button>
        </router-link>
        <el-button
          v-if="scope.row.status === 0 || scope.row.status === 2"
          text
          type="primary"
          @click="
            sendAction(
              cmsApi.notify.cmsNotifyRemoveUrl + scope.row.id,
              `Confirm to delete ${scope.row.title} ?`
            )
          "
        >
          Delete
        </el-button>
        <el-button
          v-if="scope.row.status === 0 || scope.row.status === 2"
          text
          type="primary"
          @click="
            sendAction(
              cmsApi.notify.cmsNotifyreleaseNotifyUrl + scope.row.id,
              `Confirm to release ${scope.row.title} ?`
            )
          "
        >
          Release
        </el-button>
      </template>
    </ab-table>
  </div>
</template>

<script lang="ts">
  // eslint-disable-next-line no-unused-vars
  import { reactive, defineComponent } from 'vue'
  import { abTableMix, cmsApi } from 'agilebpm'
  import {
    Delete,
    Edit,
    Search,
    Plus,
    RefreshRight,
  } from '@element-plus/icons-vue'

  export default defineComponent({
    name: 'NoticeList',
    mixins: [abTableMix],
    setup() {
      // Query condition definition, if ts needs to define all parameters, here override the parent class, if not, you don't need to set it, the parent class has already defined the query object
      const query = reactive({
        title$VLK: '',
        status$VEQ: '',
      })
      return {
        query,
        cmsApi,
        Delete,
        Edit,
        Search,
        Plus,
        RefreshRight,
      }
    },
  })
</script>
