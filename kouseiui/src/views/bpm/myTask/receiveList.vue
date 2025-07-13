<template>
  <div class="common-layout">
    <el-container>
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
                  <el-form-item label="Title" prop="b.title$VLK">
                    <el-input
                      v-model="query['b.title$VLK']"
                      placeholder="Please enter a title"
                    />
                  </el-form-item>
                  <el-form-item label="Mission" prop="taskName$VLK">
                    <el-input
                      v-model="query.taskName$VLK"
                      placeholder="Please enter a name"
                    />
                  </el-form-item>
                  <el-form-item
                    v-show="collapse"
                    label="Status"
                    prop="a.status$VEQ"
                  >
                    <el-select v-model="query['a.status$VEQ']" placeholder="">
                      <el-option label="All" value="" />
                      <el-option label="Read" value="read" />
                      <el-option label="Unread" value="unread" />
                    </el-select>
                  </el-form-item>
                  <el-form-item
                    v-show="collapse"
                    label="Type"
                    prop="b.type$VEQ"
                  >
                    <el-select v-model="query['b.type$VEQ']" placeholder="">
                      <el-option label="All" value="" />
                      <el-option label="Cc" value="cc" />
                      <el-option label="Circulate" value="circularize" />
                    </el-select>
                  </el-form-item>
                  <el-form-item>
                    <el-button :icon="Search" type="primary" @click="search()">
                      Search
                    </el-button>
                    <el-button :icon="RefreshRight" @click="reset()">
                      Reset
                    </el-button>
                    <el-button type="text" @click="handleCollapse()">
                      <span v-if="!collapse" type="text">Show Details</span>
                      <span v-else type="text">Collapse</span>
                      <el-icon class="el-icon--right">
                        <ArrowUp v-if="collapse" />
                        <ArrowDown v-else />
                      </el-icon>
                    </el-button>
                  </el-form-item>
                </el-form>
              </el-col>
              <el-col class="left-panel">
                <el-button
                  :disabled="!selectedData || selectedData.length == 0"
                  type="primary"
                  @click="
                    delBySeletedIds(
                      bpmApi.bpmPlugincarboncopy.bpmCarbonCopyUpdateReadUrl,
                      'Please confirm to update the selected records as read?'
                    )
                  "
                >
                  Mark as read
                </el-button>
              </el-col>
            </el-row>
          </div>

          <ab-table
            ref="abTable"
            v-model="selectedData"
            :height="tableHeight"
            :query-param="query"
            row-key="id"
            :url="bpmApi.bpmPlugincarboncopy.bpmMyReceiveListUrl"
          >
            <ab-column label="Title" min-width="120" prop="title" />
            <ab-column label="Mission" min-width="60" prop="taskName" />
            <ab-column label="Sender" prop="triggerUserName" width="120" />
            <ab-column
              ab-text-formatter="cc-Cc-success|circularize-Circulate-success"
              label="Cc/Circulate"
              prop="type"
              width="90"
            />
            <ab-column
              ab-text-formatter="read-Read-info|unread-Unread-warning|reviewed-Reviewed-success"
              label="Status"
              prop="status"
              width="110"
            />
            <ab-column label="Sending time" prop="receiveTime" width="180" />
            <ab-column
              ab-template="edit"
              fixed="right"
              label="Action"
              prop="key"
              width="210"
            />
            <template #edit="{ scope }">
              <router-link
                :to="{
                  name: 'BpmInstanceDetail',
                  query: {
                    id: scope.row.id,
                    type: 'carbonId',
                  },
                }"
              >
                <el-button
                  text
                  type="primary"
                  @click="
                    bpmApi.bpmPlugincarboncopy.updateReadStatus(scope.row)
                  "
                >
                  Details
                </el-button>
              </router-link>
              <el-button text type="primary" @click="showMsg(scope.row)">
                Reminder message content
              </el-button>
            </template>
          </ab-table>
        </div>
      </el-main>
    </el-container>
  </div>
</template>
<script lang="ts">
  import { reactive, defineComponent } from 'vue'
  import { abTableMix, bpmApi, abTools } from 'agilebpm'
  import { ElMessageBox } from 'element-plus'
  import {
    Delete,
    Edit,
    Search,
    Plus,
    RefreshRight,
    InfoFilled,
    ArrowUp,
    ArrowDown,
  } from '@element-plus/icons-vue'
  import { Codemirror } from 'vue-codemirror'

  export default defineComponent({
    name: 'BpmMyReceiveList',
    components: { ArrowUp, ArrowDown },
    mixins: [abTableMix],
    setup() {
      // Query condition definition, if ts needs to define all parameters, here override the parent class, if not, you don't need to set it, the parent class has already defined the query object
      const query: any = reactive({
        'b.title$VLK': '',
        taskName$VLK: '',
        'a.status$VEQ': '',
        'b.type$VEQ': '',
      })
      const showMsg = (row: any) => {
        bpmApi.bpmPlugincarboncopy
          .getCarbonCopyRecord(row.recordId)
          .then((result: any) => {
            ElMessageBox.alert(result.data.content, row.subject, {
              confirmButtonText: 'Close',
              dangerouslyUseHTMLString: true,
            })
          })
      }
      // @ts-ignore
      const { proxy } = abTools.useCurrentInstance()
      const status = proxy.$route.query.status
      if (status) {
        query['a.status$VEQ'] = 'unread'
      }
      return {
        bpmApi,
        showMsg,
        query,
        Search,
        Plus,
        RefreshRight,
        Delete,
        Edit,
        InfoFilled,
        ArrowUp,
        ArrowDown,
      }
    },
  })
</script>
