<template>
  <div class="common-layout">
    <el-container>
      <el-aside v-if="!info.typeGroup" class="left-tree">
        <flow-type-tree
          :tree-url="bpmApi.myTask.approveListTypeTreeUrl"
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
                  label-width="62px"
                  :model="query"
                  @submit.prevent
                >
                  <el-form-item label="Title" prop="title$VLK">
                    <el-input
                      v-model="query.title$VLK"
                      placeholder="Please enter a title"
                    />
                  </el-form-item>
                  <el-form-item label="Workflow" prop="name$VLK">
                    <el-input
                      v-model="query.name$VLK"
                      placeholder="Please enter a name"
                    />
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

          <ab-table
            ref="abTable"
            v-model="selectedData"
            :checkable="false"
            :height="tableHeight"
            :query-param="query"
            row-key="id"
            :url="bpmApi.myTask.bpmMyApproveListUrl"
          >
            <ab-column label="Title" min-width="340" prop="title" />
            <ab-column label="Workflow" min-width="240" prop="defName" />
            <ab-column label="Node" min-width="225" prop="nodeName" />
            <ab-column label="Initiator" prop="creator" width="120" />
            <ab-column label="Handling Time" prop="approveTime" width="160" />
            <ab-column ab-template="durationMs" label="Processing Time" width="140" />
            <template #durationMs="{ scope }">
              {{ abUtil.timeLag(scope.row.durationMs, 2) }}
            </template>
            <ab-column
              ab-tag-type="statusCss"
              label="Instance Status"
              prop="statusDesc"
              width="130"
            />
            <ab-column
              ab-tag-type="approveStatusLabelCss"
              label="Processing Result"
              prop="approveStatusValue"
              width="140"
            />
            <ab-column
              ab-template="edit"
              fixed="right"
              label="Action"
              prop="key"
              width="85"
            />
            <template #edit="{ scope }">
              <router-link
                :to="{
                  name: 'BpmInstanceDetail',
                  query: {
                    id: scope.row.id,
                    type: 'opId',
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
  import { reactive, defineComponent, getCurrentInstance } from 'vue'
  import { abTableMix, bpmApi, abUtil } from 'agilebpm'
  import flowTypeTree from './components/flow-type-tree.vue'
  import {
    Delete,
    Edit,
    Search,
    Plus,
    RefreshRight,
    InfoFilled,
  } from '@element-plus/icons-vue'
  import PendingApprovalTaskPopover from './components/pending-approval-task-popover.vue'

  export default defineComponent({
    name: 'BpmMyApproveList',
    components: { PendingApprovalTaskPopover, flowTypeTree },
    mixins: [abTableMix],
    setup() {
      // Query condition definition, if ts needs to define all parameters, here override the parent class, if not, you don't need to set it, the parent class has already defined the query object
      const query: any = reactive({
        defName$VLK: '',
        'def.typeCode$VIN': '',
        defKey$VEQ: '',
        title$VLK: '',
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
        query['def.typeCode$VIN'] = proxy.$route.query.typeGroup
      }
      const searchData = (type: string, value: string) => {
        if (type === 'default') {
          query['def.typeCode$VIN'] = null
          query['def.key$VEQ'] = null
        }
        if (type === 'flow') {
          query['def.typeCode$VIN'] = null
          query.defKey$VEQ = value
        } else if (type === 'tree') {
          query['def.typeCode$VIN'] = value
          query.defKey$VEQ = null
        }
        proxy.search()
      }
      return {
        query,
        info,
        bpmApi,
        abUtil,
        searchData,
        Search,
        Plus,
        RefreshRight,
        Delete,
        Edit,
        InfoFilled,
      }
    },
  })
</script>
