<template>
  <div class="comprehensive-table-container">
    <div ref="titleForm">
      <el-row class="vab-query-form">
        <el-col class="top-panel">
          <el-form
            ref="queryForm"
            :inline="true"
            label-width="120px"
            :model="query"
            @submit.prevent
          >
            <el-form-item label="Workflow Title" prop="title$VLK">
              <el-input
                v-model="query.title$VLK"
                placeholder="Please enter the workflow title"
              />
            </el-form-item>
            <el-form-item label="Workflow Code" prop="defKey$VLK">
              <el-input v-model="query.defKey$VLK" placeholder="Please enter the code" />
            </el-form-item>
            <el-form-item>
              <el-button :icon="Search" type="primary" @click="search()">
                Search
              </el-button>
              <el-button :icon="RefreshRight" @click="reset()">Reset</el-button>
            </el-form-item>
          </el-form>
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
      :url="bpmApi.myTask.bpmMyDraftTaskListUrl"
    >
      <ab-column label="Workflow Title" min-width="250" prop="title" />
      <ab-column label="Workflow Code" min-width="100" prop="defKey" />
      <ab-column
        ab-tag-type="statusCss"
        label="Status"
        prop="statusDesc"
        width="90"
      />
      <ab-column label="Creation Time" prop="createTime" width="160" />
      <ab-column ab-template="edit" fixed="right" label="Manage" width="160" />
      <template #edit="{ scope }">
        <router-link
          :to="{
            name: 'DraftStart',
            query: {
              instId: scope.row.id,
            },
          }"
        >
          <el-button text type="primary">Edit</el-button>
        </router-link>
        <el-button
          text
          type="primary"
          @click="
            sendAction(
              bpmApi.instance.bpmInstanceDeleteUrl + scope.row.id,
              `Are you sure you want to delete ${scope.row.title}?`
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
  // eslint-disable-next-line no-unused-vars
  import { reactive, defineComponent, onMounted, getCurrentInstance } from 'vue'
  import { abTableMix, bpmApi } from 'agilebpm'
  import {
    Delete,
    Edit,
    Search,
    Plus,
    RefreshRight,
  } from '@element-plus/icons-vue'
  export default defineComponent({
    name: 'BpmMyDraftList',
    mixins: [abTableMix],

    setup() {
      const query = reactive({
        title$VLK: '',
        defKey$VLK: '',
      })
      return {
        query,
        bpmApi,
        Delete,
        Edit,
        Search,
        Plus,
        RefreshRight,
      }
    },
  })
</script>
