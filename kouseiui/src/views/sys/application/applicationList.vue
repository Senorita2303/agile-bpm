<template>
  <div class="comprehensive-table-container">
    <div ref="titleForm">
      <el-row class="vab-query-form">
        <el-col class="top-panel">
          <el-form
            ref="queryForm"
            :inline="true"
            :model="query"
            style="float: left"
            @submit.prevent
          >
            <el-form-item label="System Name"   label-width="90px" prop="name$VLK">
              <el-input v-model="query.name$VLK" placeholder="Please enter a system name" />
            </el-form-item>
            <el-form-item label="Status"    label-width="62px" prop="enabled$VEQ">
              <el-select v-model="query.enabled$VEQ" placeholder="Please select a status">
                <el-option label="Enabel" value="1" />
                <el-option label="Disable" value="0" />
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
          <el-space wrap>
            <router-link
              :to="{
                name: 'ApplicationEdit',
                query: { type: '1' },
              }"
            >
              <el-button :icon="Plus" type="primary">Add</el-button>
            </router-link>
          </el-space>
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
      :url="sysApi.authApplication.applicationListJson"
    >
      <ab-column label="System Name" min-width="120" prop="name" />
      <ab-column label="System code" min-width="120" prop="code" />
      <ab-column label="Subsystem address" min-width="160" prop="url" />
      <ab-column
        ab-text-formatter="1-Enable-success-dark|0-Disable-danger-dark"
        label="Status"
        prop="enabled"
        width="85"
      />
      <ab-column
        ab-text-formatter="1-Yes-default-light|0-No-success-light"
        label="Is it the default?"
        prop="isDefault"
        width="85"
      />
      <ab-column
        ab-text-formatter="1-Enable-default-light|0-Disable-success-light"
        label="Automatic authorization"
        prop="autoapprove"
        width="85"
      />
      <ab-column label="Creation Time" prop="createTime" width="160 " />
      <ab-column ab-template="edit" fixed="right" label="Action" width="195" />
      <template #edit="{ scope }">
        <router-link
          :to="{
            name: 'ApplicationEdit',
            query: { id: scope.row.id },
          }"
        >
          <el-button text type="primary">Details</el-button>
        </router-link>
        <router-link
          :to="{
            name: 'ApplicationEdit',
            query: { id: scope.row.id, type: '1' },
          }"
        >
          <el-button text type="primary">Edit</el-button>
        </router-link>

        <el-button
          v-if="scope.row.id > 10"
          text
          type="primary"
          @click="
            sendAction(
              sysApi.authApplication.applicationRemove + scope.row.id,
              `Confirm delete ${scope.row.name} ?`
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
  import { reactive, defineComponent } from 'vue'
  import { abTableMix } from 'agilebpm'
  import { sysApi } from 'agilebpm'
  import {
    Delete,
    Edit,
    Search,
    Plus,
    RefreshRight,
    RefreshLeft,
  } from '@element-plus/icons-vue'

  export default defineComponent({
    name: 'ApplicationList',
    mixins: [abTableMix],
    setup() {
      // Query condition definition, if ts needs to define all parameters, here override the parent class, if not, you don't need to set it, the parent class has already defined the query object
      const query = reactive({
        name$VLK: '',
        enabled$VEQ: '',
      })

      return {
        query,
        sysApi,
        Edit,
        Plus,
        Delete,
        Search,
        RefreshRight,
        RefreshLeft,
      }
    },
  })
</script>
