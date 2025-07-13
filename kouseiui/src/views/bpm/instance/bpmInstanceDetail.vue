<template>
  <div v-loading="!info.bpmData" style="overflow: auto"     class="layout-container-demo"
  >
    <div :class="showTaskDynamic(info.bpmData) ? 'leftBoxDyminc' : 'leftBox'">
      <div v-if="info.bpmData.defName">
        <bpm-buttons
          v-if="info.bpmData.buttonList.length"
          :bpm-data="info.bpmData"
          class="print-hidden"
        />
      </div>
      <div
        v-if="info.bpmData.defName"
        class="leftContent"
        :style="{ height: info.height }"
      >
        <ab-url-form
          v-if="info.bpmData.bpmForm.type == 'url'"
          ref="url"
          :bpm-form="info.bpmData.bpmForm"
        />
        <cust-form v-else ref="inner" :form-data="info.formData" />
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { useAbStoreAdapter } from '@/store/modules/abStoreAdapter'
import { bpmTools, abTools, bpmApi } from 'agilebpm'
  import { getCurrentInstance } from 'vue'
  import instanceDetail from './instanceDetail.vue'
  import { abForm as custForm, bpmButtons, abUrlForm } from 'agilebpm'
  import { showTaskDynamic } from '../myTask/components/myTaskConfig'

  const { proxy } = abTools.useCurrentInstance()
  const vueContext = getCurrentInstance()
  const { changeTabsMeta } = useAbStoreAdapter()

  const instId = vueContext?.proxy?.$route.query.instId
  const opId = vueContext?.proxy?.$route.query.opId
  const bizId = vueContext?.proxy?.$route.query.bizId
  const nodeKey = vueContext?.proxy?.$route.query.nodeKey
  const carbonId = vueContext?.proxy?.$route.query.carbonId
  const isStart = !(vueContext?.proxy?.$route.query.isStart == '0')

  // The following parameters take a new approach
  const id = vueContext?.proxy?.$route.query.id
  const type = vueContext?.proxy?.$route.query.type

  const info: any = reactive({
    bpmData: {},
    formData: {},
    height: `${window.innerHeight - 279}px`,
  })

  const getParam = () => {
    if (id) {
      if (!type) {
        vueContext.proxy.$route.query.type = 'instId'
      }
      return { id: id, type: type }
    }

    if (carbonId) {
      return { id: carbonId, type: 'carbonId' }
    }
    if (bizId) {
      return { id: bizId, type: 'bizId' }
    }
    if (opId) {
      return { id: opId, type: 'opId' }
    }
    if (instId) {
      let typeTemp = 'instId'
      if (isStart) {
        typeTemp = 'instIdStart'
      }
      return { id: instId, type: typeTemp }
    }
  }

  const initData = () => {
    const param = getParam()
    bpmApi.bpmData
      .getInstDataNew(param.id, param.type)
      .then(({ data }) => {
        bpmTools.initBpmDataAndFormData(
          data,
          vueContext,
          info.bpmData,
          info.formData
        )
        info.bpmData.actionData.instanceId = data.bpmInstance.id

        if (type && type === 'carbonId') {
          info.bpmData.actionData.extendConf.carbonId = id
        }

        changeTabsMeta({
          fullPath: proxy.$route.fullPath,
          meta: { title: `Instance details-${data.bpmInstance.title}` },
        })
      })
      .catch((error) => {
        if (error.code == 'bpm_taskIsDone') {
          proxy.$router.push({ name: 'BpmMyApproveList' })
        }
      })
  }

  initData()
</script>