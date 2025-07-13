<template>
  <div
    v-loading="!info.bpmData"
    style="overflow: auto"
    class="layout-container-demo"
  >
    <div  class=" leftBo">
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
  import { abForm as custForm, bpmButtons, abUrlForm } from 'agilebpm'

  const { proxy } = abTools.useCurrentInstance()
  const vueContext = getCurrentInstance()
  const { changeTabsMeta } = useAbStoreAdapter()

  const info: any = reactive({
    bpmData: {},
    formData: {},
    height: `${window.innerHeight - 279}px`,
    scale: 1,
  })

  const instId = vueContext?.proxy?.$route.query.id
  vueContext.proxy.$route.query.type = 'instId'

  if (instId) {
    bpmApi.bpmData.getInstDataById(instId).then(({ data }) => {
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

  const changeScale = (scale: any) => {
    info.scale = scale
  }

  onMounted(() => {
    if (proxy.$route.query.abIframeType == 'iframe') {
      window.parent.postMessage({ type: 'bpmComplateOrInstanceDetailsFn' }, '*')
      window.onmessage = (e) => {
        if (e.data.type == 'changeScale') {
          if (e.data.scale) {
            changeScale(e.data.scale)
          }
        }
      }
    }
  })
</script>

 