<template>
  <div class="printBox">
    <el-container>
      <el-header class="noprint" style="margin-top: 20px">
        <el-space>
          <el-button v-print="print" type="primary" @click="print">
            Print
          </el-button>
          <el-button type="primary" @click="closeWindow">Cancel</el-button>
          <el-checkbox
            v-model="info.showApproveHistory"
            label="Print approval history"
            size="large"
          />
        </el-space>
      </el-header>

      <el-divider class="dividermar" />
      <div id="printTest" ref="printTest" class="print">
        <el-main v-if="info.bpmData.bpmForm && info.bpmData.bpmForm.type">
          <ab-url-form
            v-if="info.bpmData.bpmForm.type == 'url'"
            ref="url"
            :bpm-form="info.bpmData.bpmForm"
          />
          <cust-form v-else ref="inner" :form-data="info.formData" />
        </el-main>
        <el-footer>
          <div v-if="info.showApproveHistory && info.bpmData.bpmInstance">
            <h4>Approval History</h4>
            <approve-history-table
              :instance-id="historyParam.instId"
              :opinion-id="historyParam.opId"
              :task-id="historyParam.taskId"
            />
          </div>
        </el-footer>
      </div>
    </el-container>
  </div>
</template>

<script lang="ts" setup>
import { useAbStoreAdapter } from '@/store/modules/abStoreAdapter'
import {
    abForm as custForm,
    bpmButtons,
    abUrlForm,
    bpmTools,
    abTools,
    bpmApi,
    ApproveHistoryTable,
  } from 'agilebpm'

  import { getCurrentInstance, Ref } from 'vue'
  const { changeTabsMeta } = useAbStoreAdapter()

  const spacer = h(ElDivider, { direction: 'vertical' })
  const vueContext = getCurrentInstance()

  const info: any = reactive({
    bpmData: {} as any,
    formData: {} as any,
    showApproveHistory: false,
    instanceId: '',
    nodeKey: '',
  })

  const historyParam = computed(() => {
    const resultParam = {
      instId: info.bpmData.bpmInstance?.id,
      taskId: info.bpmData.bpmTask?.id,
    }
    const newParam = getParam()

    if (!newParam || !newParam.type) {
      return resultParam
    }
    if (type == 'opId') {
      resultParam.opId = newParam.id
    } else if (type == 'taskId') {
      resultParam.taskId = newParam.id
    }
    return resultParam
  })
  // Here are the configuration items for printing
  // const print = ref({
  //   id: 'printTest', // The id here is the print area id we mentioned above, which allows you to print wherever you point
  //   popTitle: 'Print', // Prints the header above the configuration page
  //   extraHead:
  //     '<meta http-equiv="Content-Language"content="zh-cn"/>,<style>  #printId {  100%;  } <style>', // The top header text, additional tags attached to the head tag, separated by commas
  //   preview: false, // Whether to start preview mode, the default is false
  //   previewTitle: 'Preview title', // Print preview title
  //   previewPrintBtnLabel: 'Preview is over, start printing', // The button text below the print preview title, click to enter the print
  //   zIndex: 20002, // The z-index of the preview window is 20002 by default, and it is best to be higher than the default value
  //   previewBeforeOpenCallback() {
  //     console.log('Loading preview window!')
  //   }, // Callback before the preview window is opened
  //   previewOpenCallback() {
  //     console.log('The preview window has been loaded and the preview is open!')
  //   }, // Callback when the preview window is opened
  //   beforeOpenCallback() {
  //     console.log('Before you start printing!')
  //   }, // Callback before printing starts
  //   openCallback() {
  //     console.log('Execute the print!')
  //   }, // Callback when printing
  //   closeCallback() {
  //     console.log('The printing tool is closed!')
  //   }, // Turn off the callback for printing (cannot distinguish between confirmation or cancellation)
  //   clickMounted() {
  //     console.log('Click the button bound to v-print!')
  //   },
  // })
  const { proxy } = abTools.useCurrentInstance()

  const instId = vueContext?.proxy?.$route.query.instId
  const opId = vueContext?.proxy?.$route.query.opId
  const bizId = vueContext?.proxy?.$route.query.bizId
  const nodeKey = vueContext?.proxy?.$route.query.nodeKey
  const carbonId = vueContext?.proxy?.$route.query.carbonId
  const isStart = !(vueContext?.proxy?.$route.query.isStart == '0')
  // id is taskId
  const taskId = vueContext?.proxy?.$route.query.id

  // Use a new method to get the print form
  const id = vueContext?.proxy?.$route.query.id
  const type = vueContext?.proxy?.$route.query.type

  const { delVisitedRoute } = useTabsStore()
  const closeWindow = () => {
    const route = proxy.$route
    delVisitedRoute(route.path)
    proxy.$router ? proxy.$router.back() : window.history.back()
  }
  const print = () => {
    window.print()
  }

  const getParam = () => {
    if (id && type) {
      return { id: id, type: type }
    }

    if (carbonId) {
      return { id: carbonId, type: 'carbonId' }
    }
    if (bizId) {
      return { id: bizId, type: 'bizId' }
    }
    if (opId) {
      return { id: 'opId', type: 'opId' }
    }
    if (instId) {
      let typeTemp = 'instId'
      if (isStart) {
        typeTemp = 'instIdStart'
      }
      return { id: 'instId', type: typeTemp }
    }
  }

  const initData = () => {
    const param = getParam()
    bpmApi.bpmData
      .getInstDataNew(param.id, param.type, true)
      .then(({ data }) => {
        bpmTools.initBpmDataAndFormData(
          data,
          vueContext,
          info.bpmData,
          info.formData
        )
        info.bpmData.actionData.instanceId = data.bpmInstance.id

        changeTabsMeta({
          fullPath: proxy.$route.fullPath,
          meta: { title: `Instance details-${data.bpmInstance.title}` },
        })
      })
  }
  initData()
</script>

<style lang="scss" scoped>
  @media print {
    .noprint {
      display: none;
    }
  }
  .printBox {
    height: 100vh;
    overflow: auto;
    background: #fff;
  }
</style>
