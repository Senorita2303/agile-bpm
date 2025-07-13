<template>
  <div class="comprehensive-table-container">
    <div ref="titleForm">
      <el-space>
        <el-button :icon="Back" type="primary" @click="back">Back</el-button>
        <!-- <el-button
          :disabled="!selectedData || selectedData.length == 0"
          :icon="Download"
          type="primary"
          @click="exportFn"
        >
          Export
        </el-button> -->
      </el-space>
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
      :url="bpmApi.bpmDefinition.bpmDefinitionListUrl"
    >
      <ab-column label="Process Name" min-width="120" prop="name" />
      <ab-column label="Workflow Code" min-width="100" prop="key" />
      <ab-column label="Description" min-width="150" prop="desc" />
      <ab-column
        ab-text-formatter="1-Support-success|0-Not supported-"
        label="Mobile"
        prop="supportMobile"
        width="90"
      />
      <ab-column
        ab-text-formatter="deploy-Publish-success-dark|forbidden-Disable-danger-dark|draft-Draft-info"
        label="Status"
        prop="status"
        width="80"
      />
      <ab-column label="Version" prop="version" width="55" />
      <ab-column
        ab-text-formatter="1-Yes-success-dark|0-No-info-dark"
        label="Major version"
        prop="isMain"
        width="80"
      >
        Major version
      </ab-column>
      <ab-column label="Order" prop="sn" width="55" />
      <ab-column
        ab-date-formatter="yyyy-MM-dd"
        label="Update Time"
        prop="updateTime"
        width="120"
      />
      <ab-column ab-template="edit" fixed="right" label="Action" width="315" />
      <template #edit="{ scope }">
        <el-button
          v-show="scope.row.processEditor !== 'sim'"
          v-ab-btn-rights:bpmDefinition_preview
          text
          type="primary"
          @click="openOverView(scope.row)"
        >
          Configuration Preview
        </el-button>

        <el-button text type="primary" @click="showDesign(scope.row)">
          Design
        </el-button>
        <router-link :to="`/bpm/flowStart/${scope.row.key}`">
          <el-button text type="primary">Start</el-button>
        </router-link>
        <el-button
          v-if="scope.row.isMain === 0"
          v-show="scope.row.processEditor !== 'sim'"
          v-ab-btn-rights:bpmDefinition_setMain
          text
          type="primary"
          @click="
            sendAction(
              bpmApi.bpmDefinition.bpmDefinitionSetMainUrl + scope.row.id,
              `Are you sure you want to set version ${scope.row.version} as the primary version?`
            )
          "
        >
          Set as main version
        </el-button>
        <el-button
          v-if="scope.row.isMain === 0"
          v-show="scope.row.processEditor === 'sim'"
          v-ab-btn-rights:simDefinition_setMain
          text
          type="primary"
          @click="
            sendAction(
              bpmApi.bpmDefinition.bpmDefinitionSetMainUrl + scope.row.id,
              `Are you sure you want to set version ${scope.row.version} as the primary version?`
            )
          "
        >
          Set as main version
        </el-button>
      </template>
    </ab-table>
    <bpm-definition-overall-view
      v-model="overViewInfo.isShow"
      :def-id="overViewInfo.defId"
      :title="overViewInfo.title"
    />
    <ab-designer
      v-if="designInfo.show"
      v-model="designInfo.show"
      :def-id="designInfo.defId"
      @close="closeDefinition()"
    />
  </div>
</template>

<script lang="ts">
  // eslint-disable-next-line no-unused-vars
  import { reactive, defineComponent, getCurrentInstance } from 'vue'
  import { abTableMix, bpmApi, abTools } from 'agilebpm'
  import { Back, Download } from '@element-plus/icons-vue'
  import { useAbStoreAdapter } from '@/store/modules/abStoreAdapter'
  // import abDesigner from 'abDesigner'

  export default defineComponent({
    name: 'BpmDefinitionVersionList',
    components: {  },
    mixins: [abTableMix],
    setup() {
      // @ts-ignore
      const { proxy } = getCurrentInstance()
      const defKey = proxy.$route.query.key

      console.log(proxy.$router.getRoutes())

      // Query condition definition, if ts needs to define all parameters, here override the parent class, if not, you don't need to set it, the parent class has already defined the query object
      const query = reactive({
        name$VLK: '',
        key$VEQ: defKey,
        typeCode$VEQ: '',
        isVersions: true,
      })
      const designInfo = reactive({
        show: false,
        defId: '',
      })
      const { delVisitedRoute } = useAbStoreAdapter()
      const back = () => {
        proxy.$router ? proxy.$router.back() : window.history.back()
      }
      const overViewInfo = reactive({
        isShow: false,
        title: '',
        defId: '',
      })
      const openOverView = (bemDefiniton: any) => {
        overViewInfo.isShow = true
        overViewInfo.title = bemDefiniton.name
        overViewInfo.defId = bemDefiniton.id
      }
      // Export
      const exportFn = () => {
        const defIds = proxy.selectedData.map((row: any) => row.id).join(',')
        bpmApi.bpmDefinition.exportBpmDef(defIds, exportCallback)
      }
      // Export callback, refresh the page, remove the selected state
      const exportCallback = () => {
        proxy.selectedData.splice(0, proxy.selectedData.length)
        proxy.search()
      }
      const showDesign = (row: any) => {
        if (row.processEditor == 'sim') {
          window.open(`/simpleWF?id=${row.id}&mode=diyBo`)
        } else {
          window.open(
            abTools.getResolveUrl(`/bpmDesigner?defId=${row.id}`, proxy)
          )
        }
      }
      const closeDefinition = () => {
        designInfo.show = false
        designInfo.defId = ''
        proxy.search()
      }
      return {
        query,
        bpmApi,
        Back,
        Download,
        back,
        openOverView,
        overViewInfo,
        showDesign,
        closeDefinition,
        exportFn,
        designInfo,
      }
    },
  })
</script>
