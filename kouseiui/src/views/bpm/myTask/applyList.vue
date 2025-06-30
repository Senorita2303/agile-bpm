<template>
  <div class="common-layout">
    <el-container>
      <el-aside v-if="!info.typeGroup" class="left-tree">
        <flow-type-tree
          :tree-url="bpmApi.myTask.applyListTypeTreeUrl"
          @node-click="searchData"
        />
      </el-aside>
      <el-main>
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
                  <el-form-item label="Process Title" prop="title$VLK">
                    <el-input
                      v-model="query.title$VLK"
                      placeholder="Please enter workflow title"
                    />
                  </el-form-item>
                  <el-form-item label="Workflow Status" prop="inst.status$VEQ">
                    <el-select
                      v-model="query['inst.status$VEQ']"
                      placeholder="Please enter status"
                    >
                      <el-option
                        v-for="(item, index) in statusList"
                        :key="index"
                        :label="item.value"
                        :value="item.key"
                      />
                    </el-select>
                  </el-form-item>
                  <el-form-item>
                    <el-button :icon="Search" type="primary" @click="search()">
                      Search
                    </el-button>
                    <el-button :icon="RefreshRight" @click="reset()">
                      Reset
                    </el-button>
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
            :url="bpmApi.myTask.bpmMyApplyTaskListUrl"
          >
            <ab-column label="Process" min-width="100" prop="defName" />
            <ab-column label="Process Title" min-width="100" prop="title" />
            <ab-column
              ab-tag-type="statusCss"
              label="Status"
              prop="statusDesc"
              width="150"
            />
            <ab-column label="Creation Time" prop="createTime" width="180" />
            <ab-column label="Completion Time" prop="endTime" width="160" />
            <ab-column
              ab-template="edit"
              fixed="right"
              label="Manage"
              width="85"
            />
            <template #edit="{ scope }">
              <router-link
                :to="{
                  name: 'BpmInstanceDetail',
                  query: {
                    id: scope.row.id,
                    type: 'instIdStart',
                  },
                }"
              >
                <el-button text type="primary">Details</el-button>
              </router-link>
            </template>
          </ab-table>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script lang="ts">
  // eslint-disable-next-line no-unused-vars
  import { reactive, defineComponent, onMounted, getCurrentInstance } from 'vue'
  import { abTableMix, bpmApi, abUtil, sysApi } from 'agilebpm'
  import flowTypeTree from './components/flow-type-tree.vue'
  import { Search, Plus, RefreshRight } from '@element-plus/icons-vue'
  import PendingApprovalTaskPopover from './components/pending-approval-task-popover.vue'

  export default defineComponent({
    name: 'BpmMyApplyList',
    components: { PendingApprovalTaskPopover, flowTypeTree },
    mixins: [abTableMix],
    setup() {
      const query: any = reactive({
        title$VLK: '',
        defKey$VEQ: '',
        'def.typeCode$VIN': '',
        'inst.status$VEQ': '',
      })
      // @ts-ignore
      const { proxy } = getCurrentInstance()
      const info: any = reactive({
        typeGroup: false,
      })
      if (
        proxy.$route.query.typeGroup &&
        proxy.$route.query.typeGroup.length > 0
      ) {
        info.typeGroup = true
        query['def.typeCode$VEQ'] = proxy.$route.query.typeGroup
      }
      const statusList = ref([])
      onMounted(() => {
        sysApi.tools
          .getEnum('com.dstz.bpm.api.enums.InstanceStatus')
          .then((result: any) => {
            statusList.value = result.data
          })
      })
      const searchData = (type: string, value: string) => {
        if (type === 'default') {
          query.defKey$VEQ = null
          query['def.typeCode$VIN'] = null
        }
        if (type === 'flow') {
          query.defKey$VEQ = value
          query['def.typeCode$VIN'] = null
        } else if (type === 'tree') {
          query['def.typeCode$VIN'] = value
          query.defKey$VEQ = null
        }
        proxy.search()
      }

      query['inst.status$VEQ'] = proxy.$route.query.status

      return {
        query,
        info,
        bpmApi,
        statusList,
        searchData,
        abUtil,
        Search,
        Plus,
        RefreshRight,
      }
    },
  })
</script>
