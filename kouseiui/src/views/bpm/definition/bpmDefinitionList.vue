<template>
  <div class="common-layout">
    <el-container>
      <el-aside class="left-tree">
        <ab-dict-tree
          v-model="query.typeCode$VIN"
          type-code="flowType"
          @node-click="searchResetPage()"
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
                  :model="query"
                  @submit.prevent
                >
                  <el-form-item label="Process Name" prop="name$VLK">
                    <el-input
                      v-model="query.name$VLK"
                      placeholder="Please enter the process name"
                    />
                  </el-form-item>
                  <el-form-item label="Workflow Code" prop="key$VLK">
                    <el-input
                      v-model="query.key$VLK"
                      placeholder="Please enter the process code"
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
              <el-col class="left-panel">
                <el-space>
                  <router-link
                    v-if="!abUtil.checkIsPublicProject(true)"
                    :to="{
                      name: 'BpmDefinitionEdit',
                      query: { typeCode: query.typeCode$VEQ },
                    }"
                  >
                    <el-button :icon="Plus" type="primary">
                      BPMN Process Design
                    </el-button>
                  </router-link>
                  <el-button
                    v-else
                    :disabled="true"
                    :icon="Plus"
                    type="primary"
                  >
                    BPMN Process Design
                  </el-button>

                  <el-button
                    :disabled="!selectedData || selectedData.length == 0"
                    :icon="Download"
                    type="primary"
                    @click="exportFn"
                  >
                    Export
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
            is-have-tree
            :query-param="query"
            row-key="id"
            :url="bpmApi.bpmDefinition.bpmDefinitionListUrl"
          >
            <ab-column label="Process Name" min-width="120" prop="name" />
            <ab-column label="Workflow Code" min-width="130" prop="key" />
            <ab-column label="Description" prop="desc" width="150" />
            <ab-column
              ab-text-formatter="1-Support-success|0-Not supported-"
              label="Mobile"
              prop="supportMobile"
              width="130"
            />
            <ab-column
              ab-text-formatter="deploy-Release-success-dark|forbidden-Disable-danger-dark|draft-Draft-info"
              label="Status"
              prop="status"
              width="80"
            />
            <ab-column label="Version" prop="version" width="80" />
            <ab-column label="Sort" prop="sn" width="55" />
            <ab-column
              ab-text-formatter="bpmn-BPMN-success-dark|simple-Simple-primary-dark|empty-To be designed-warning-dark"
              label="Editor"
              prop="processEditor"
              width="90"
            />
            <ab-column
              ab-date-formatter="yyyy-MM-dd HH:mm"
              label="Update Time"
              prop="updateTime"
              width="160"
            />
            <ab-column label="Updater" prop="updater" width="120" />
            <ab-column
              ab-template="edit"
              fixed="right"
              label="Action"
              width="290"
            />
            <template #edit="{ scope }">
              <el-button
                text
                type="primary"
                @click="showDesign(scope.row.id)"
              >
                Design
              </el-button>
              <el-button
                text
                type="primary"
                @click="openAuthoDialog(scope.row.key)"
              >
                Author
              </el-button>
              <router-link
                :to="`/bpm/flowStart/${scope.row.key}`"
              >
                <el-button text type="primary">Start</el-button>
              </router-link>
              <el-dropdown trigger="click">
                <el-button :icon="ArrowDown" text type="primary">
                  More
                </el-button>
                <template #dropdown>
                  <el-dropdown-menu>
                    <span >
                      <router-link
                        :to="{
                          name: 'BpmDefinitionEdit',
                          query: { id: scope.row.id },
                        }"
                      >
                        <el-dropdown-item>
                          <el-button type="text" primary>Edit</el-button>
                        </el-dropdown-item>
                      </router-link>
                    </span>
                     
                    <span >
                      <el-dropdown-item
                        @click="
                          sendAction(
                            bpmApi.bpmDefinition.bpmDefinitionRemoveUrl +
                              scope.row.id,
                            `Are you sure you want to delete ${scope.row.name}?`
                          )
                        "
                      >
                        <a href="javascript:void(0)">Delete</a>
                      </el-dropdown-item>
                    </span>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </template>
          </ab-table>
          <ab-authorization
            v-model="authInfo.showAuthoDialog"
            authorization-type="FLOW"
            :rights-code="authInfo.flowKey"
          />
          <ab-designer
            v-if="designShow"
            v-model="designShow"
            :def-id="designInfo.defId"
            @close="closeDefinition()"
          />
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script lang="ts">
  // eslint-disable-next-line no-unused-vars
  import { reactive, defineComponent, getCurrentInstance, watch } from 'vue'
  import {
    abTableMix,
    bpmApi,
    abAuthorization,
    abDictTree,
    abUtil,
    abTools,
  } from 'agilebpm'
  // import abDesigner from 'abDesigner'

  import {
    Delete,
    Edit,
    Search,
    Plus,
    RefreshRight,
    Download,
    Upload,
    ArrowDown,
  } from '@element-plus/icons-vue'
  import { ElMessage } from 'element-plus'
  export default defineComponent({
    name: 'BpmDefinitionList',
    components: {
      abAuthorization,
      //abDesigner,
      abDictTree,
    },
    mixins: [abTableMix],
    setup() {
      // Query condition definition, if ts needs to define all parameters, here override the parent class, if not, you don't need to set it, the parent class has already defined the query object
      const query = reactive({
        name$VLK: '',
        key$VLK: '',
        typeCode$VIN: '',
        processEditor$VIN: ['bpmn', 'empty'],
      })

      const designShow = ref(false)

      const designInfo = reactive({
        defId: '',
        newDefinitionId: '',
      })

      const showDesign = (defId: string) => {
        window.open(abTools.getResolveUrl(`/bpmDesigner?defId=${defId}`, proxy))
      }
      const closeDefinition = () => {
        designShow.value = false
        designInfo.defId = ''
        proxy.search()
      }

      // @ts-ignore
      const { proxy } = getCurrentInstance()

      // After creating a new process, the designer will automatically open when you enter the page.
      watch(
        () => proxy.$route.query.newDefinitionId,
        (id) => {
          if (!id || designInfo.newDefinitionId == id) return
          designInfo.newDefinitionId = id
          proxy.search()
          showDesign(id)
        }
      )
      const authInfo = reactive({
        showAuthoDialog: false,
        flowKey: '',
      })
      // Authorization
      const openAuthoDialog = (flowKey: string) => {
        authInfo.showAuthoDialog = true
        authInfo.flowKey = flowKey
      }
      // Export
      const exportFn = () => {
        const noDefSetting = proxy.selectedData.find((e: any) => {
          return e.processEditor === 'empty'
        })
        if (noDefSetting) {
          ElMessage.error(
            `The process [${noDefSetting.name}] has not completed the process design and cannot be exported!`
          )
          return
        }

        const defIds = proxy.selectedData.map((row: any) => row.id).join(',')
        bpmApi.bpmDefinition.exportBpmDef(defIds, exportCallback)
      }
      // Export callback, refresh the page, remove the selected state
      const exportCallback = () => {
        proxy.selectedData.splice(0, proxy.selectedData.length)
        proxy.search()
      }
      // Import, refresh the page, remove the selected state
      watch(
        () => proxy.$route.query.doRef,
        (newValue) => {
          if (newValue) {
            exportCallback()
          }
        }
      )

      // Process definition replication information
      const bpmDefinitionDuplicateInfo = reactive({
        visible: false,
        flowKey: '',
        flowName: '',
        typeCode: '',
        show(bpmDefinition: any) {
          bpmDefinitionDuplicateInfo.flowKey = bpmDefinition.key
          bpmDefinitionDuplicateInfo.flowName = bpmDefinition.name
          bpmDefinitionDuplicateInfo.typeCode = bpmDefinition.typeCode
          bpmDefinitionDuplicateInfo.visible = true
        },
        close(newDefId: string) {
          bpmDefinitionDuplicateInfo.visible = false
          // New process definition ID
          if (newDefId) {
            designInfo.newDefinitionId = newDefId
            proxy.search()
            showDesign(newDefId)
          }
        },
      })

      return {
        abUtil,
        query,
        bpmApi,
        authInfo,
        abAuthorization,
        openAuthoDialog,
        Delete,
        Edit,
        Search,
        Plus,
        RefreshRight,
        Download,
        exportFn,
        Upload,
        designInfo,
        designShow,
        showDesign,
        closeDefinition,
        bpmDefinitionDuplicateInfo,
      }
    },
    computed: {
      ArrowDown() {
        return ArrowDown
      },
    },
  })
</script>
