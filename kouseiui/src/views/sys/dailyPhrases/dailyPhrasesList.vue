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
            <el-form-item label="Commonly used words" label-width="76px" prop="locution$VLK">
              <el-input
                v-model="query.locution$VLK"
                placeholder="Please enter a common phrase"
              />
            </el-form-item>
            <el-form-item label="Status" label-width="62px" prop="enable$VEQ">
              <el-select v-model="query.enable$NEQ" placeholder="Please select a status">
                <el-option label="Enable" value="1" />
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
                name: 'DailyPhrasesEdit',
                query: { type: '1' },
              }"
            >
              <el-button :icon="Plus" type="primary">Add</el-button>
            </router-link>
            <el-button
              :disabled="!selectedData || selectedData.length == 0"
              :icon="Delete"
              type="danger"
              @click="
                delBySeletedIds(sysApi.sysDailyPhrases.dailyPhrasesRemove)
              "
            >
              Batch Delete
            </el-button>
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
      :height="tableHeight"
      :query-param="query"
      row-key="id"
      :url="sysApi.sysDailyPhrases.dailyPhrasesListJson"
    >
      <ab-column label="Commonly used words" min-width="160" prop="locution" />
      <ab-column ab-template="changeEnable" label="Is it enabled?" width="100" />
      <template #changeEnable="{ scope }">
        <el-switch
          v-model="scope.row.enable"
          :active-value="1"
          :inactive-value="0"
          inline-prompt
          @change="changeEnable(scope.row.id, scope.row.enable)"
        />
      </template>
      <ab-column
        ab-text-formatter="1-Yes-default-light|0-No-success-light"
        label="Is it built-in"
        prop="isDefault"
        width="120"
      />
      <ab-column label="Creator" prop="creator" width="160" />
      <ab-column label="Creation Time" prop="createTime" width="200" />
      <ab-column label="Update Time" prop="updateTime" width="200" />
      <ab-column ab-template="edit" fixed="right" label="Action" width="195" />
      <template #edit="{ scope }">
        <router-link
          :to="{
            name: 'DailyPhrasesEdit',
            query: { id: scope.row.id },
          }"
        >
          <el-button text type="primary">Details</el-button>
        </router-link>
        <router-link
          :to="{
            name: 'DailyPhrasesEdit',
            query: { id: scope.row.id, type: '1' },
          }"
        >
          <el-button text type="primary">Edit</el-button>
        </router-link>
        <el-button
          v-if="scope.row.isDefault === 0"
          text
          type="primary"
          @click="
            sendAction(
              sysApi.sysDailyPhrases.dailyPhrasesRemove + scope.row.id,
              `Confirm delete ${scope.row.locution} ?`
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
  import { ElMessage } from 'element-plus'
  import { abTableMix } from 'agilebpm'
  import { sysApi, postData } from 'agilebpm'

  import {
    Delete,
    Edit,
    Search,
    Plus,
    RefreshRight,
    RefreshLeft,
  } from '@element-plus/icons-vue'

  export default defineComponent({
    name: 'DailyPhrasesList',
    mixins: [abTableMix],
    setup() {
      // Query condition definition, if ts needs to define all parameters, here override the parent class, if not, you don't need to set it, the parent class has already defined the query object
      const query = reactive({
        locution$VLK: '',
        enable$NEQ: '',
      })
      const loading = ref(false)

      const changeEnable = (id: string, enable: number) => {
        if (id)
          postData(sysApi.sysDailyPhrases.dailyPhrasesUpdateEnable, {
            id: id,
            enable: enable,
          })
            .then(({ msg }) => {
              ElMessage.success('Operation successful')
              loading.value = true
            })
            .catch(() => (loading.value = true))
      }
      return {
        changeEnable,
        query,
        sysApi,
        Plus,
        Edit,
        Delete,
        Search,
        RefreshRight,
        RefreshLeft,
      }
    },
  })
</script>
