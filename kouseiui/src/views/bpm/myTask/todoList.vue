<template>
  <div class="common-layout">
    <el-container>
      <el-aside v-if="!info.typeGroup" class="left-tree">
        <flow-type-tree
          v-model="randomModel"
          :tree-url="bpmApi.myTask.todoListTypeTreeUrl"
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
                  label-width="90px"
                  :model="query"
                  @submit.prevent
                >
                  <el-form-item label="Task Title" prop="title$VLK">
                    <el-input
                      v-model="query.title$VLK"
                      placeholder="Please enter a title"
                    />
                  </el-form-item>
                  <el-form-item label="Task Name" prop="task.name$VLK">
                    <el-input
                      v-model="query['task.name$VLK']"
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
            :url="bpmApi.myTask.bpmMyTaskTodoListUrl"
          >
            <ab-column label="Task Title" min-width="70" prop="title" />
            <ab-column label="Task Name" prop="name" width="120" />
            <ab-column
              ab-tag-type="statusCss"
              label="Status"
              prop="statusDesc"
              width="80"
            />
            <ab-column
              ab-tag-type="typeCss"
              label="Type"
              prop="typeDesc"
              width="95"
            />
            <ab-column ab-template="priority" label="Priority" width="85" />
            <template #priority="{ scope }">
              <el-tag
                :color="custFormat(scope.row.priority)?.color"
                effect="light"
                style="color: aliceblue"
              >
                {{ custFormat(scope.row.priority)?.name }}
              </el-tag>
            </template>
            <ab-column label="Task Creation Time" prop="createTime" width="160" />
            <ab-column ab-template="durMs" label="Task Duration" width="160" />
            <template #durMs="{ scope }">
              {{ abUtil.timeLag(scope.row.durMs, 2) }}
            </template>
            <ab-column ab-template="dueTime" label="Task Expiration Time" width="160" />
            <template #dueTime="{ scope }">
              <span
                v-if="new Date(scope.row.dueTime) < new Date()"
                style="color: crimson"
              >
                {{ scope.row.dueTime }}
              </span>
              <span v-else>{{ scope.row.dueTime }}</span>
            </template>
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
                  name: 'BpmTaskComplate',
                  query: {
                    id: scope.row.id,
                    type: 'taskId',
                    backR: 'BpmMyTaskTodoList',
                  },
                }"
              >
                <el-button text type="primary">Process</el-button>
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
    Search,
    RefreshRight,
    InfoFilled,
  } from '@element-plus/icons-vue'

  export default defineComponent({
    name: 'BpmMyTaskTodoList',
    components: { flowTypeTree },
    mixins: [abTableMix],
    setup() {
      // Query condition definition, if ts needs to define all parameters, here override the parent class, if not, you don't need to set it, the parent class has already defined the query object
      const query: any = reactive({
        'task.name$VLK': '',
        'def.typeCode$VIN': '',
        'def.key$VEQ': '',
        title$VLK: '',
      })

      const randomModel = ref('')
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

      const custFormat = (value: number) => {
        if (!value) return
        if (value < 51) {
          return {
            name: 'Normal',
            type: '',
            color: '#409EFF',
          }
        }
        if (value == 51) {
          return { name: 'Priority', type: 'success', color: '#ffadd2' }
        }
        if (value == 52) {
          return { name: 'Expedited', type: 'info', color: '#E6A23C' }
        }
        if (value == 53) {
          return { name: 'Emergency', type: 'warning', color: '#f90' }
        }
        if (value == 54) {
          return { name: 'Urgent', type: 'warning', color: '#ed4014' }
        }
        if (value >= 55) {
          return { name: 'Urgent', type: 'danger', color: '#ed4014' }
        }
      }

      const search = () => {
        // Random number changes Re-obtain tree
        randomModel.value = `${Math.floor(Math.random() * 100)}`
        proxy.$refs['abTable'].getData()
      }

      const searchData = (type: string, value: string) => {
        if (type === 'default') {
          query['def.typeCode$VIN'] = null
          query['def.key$VEQ'] = null
          proxy.$refs['abTable'].getData()
          return
        }
        if (type === 'flow') {
          query['def.typeCode$VIN'] = null
          query['def.key$VEQ'] = value
          proxy.$refs['abTable'].getData()
          return
        }
        if (type === 'tree') {
          query['def.key$VEQ'] = null
          query['def.typeCode$VIN'] = value
          proxy.$refs['abTable'].getData()
        }
      }

      return {
        custFormat,
        searchData,
        query,
        info,
        bpmApi,
        Search,
        RefreshRight,
        Delete,
        InfoFilled,
        abUtil,
        search,
        randomModel,
      }
    },
  })
</script>
