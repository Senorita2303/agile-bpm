<template>
  <div
    v-loading="!info.bpmData.defName"
    class="layout-container-demo"    style="overflow: auto"
  >
    <div class='leftBox'>
      <div v-if="info.bpmData.defName" class="header-fix-top">
        <bpm-buttons
          v-if="info.bpmData.buttonList.length"
          :bpm-data="info.bpmData"
          class="print-hidden"
        />
      </div>

      <div
        v-if="info.bpmData.defName"
        class="leftContent"
        :style="{
          height: info.height,
          overflow: info.bpmData.bpmForm.type == 'url' ? '' : 'auto',
        }"
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
import { getCurrentInstance, Ref } from 'vue'
  import {
    abForm as custForm,
    bpmButtons,
    abUrlForm,
    bpmTools,
    bpmApi,
    abTools,
  } from 'agilebpm'
  import { ElMessageBox } from 'element-plus'

  const { proxy } = abTools.useCurrentInstance()
  const vueContext = getCurrentInstance()
  const { changeTabsMeta } = useAbStoreAdapter()

  const taskId = vueContext?.proxy?.$route.query.id
  vueContext.proxy.$route.query.type = 'taskId'
  const info: any = reactive({
    bpmData: {} as Ref<BpmData>,
    formData: {},
    height: `${window.innerHeight - 279}px`,
    scale: 1,
  })

  bpmApi.bpmData.getTaskData(taskId).then((res: any) => {
    if (!res.isOk && res.code === 'bpmConfirm') {
      ElMessageBox.confirm(res.msg, 'Message', {
        confirmButtonText: 'Confirm',
        showCancelButton: false,
        type: 'warning',
      }).then(() => {
        // Jump to the task to-do page
        proxy.$router.push({
          name: 'BpmMyTaskTodoList',
        })
      })
    }
    if (res.isOk) {
      bpmTools.initBpmDataAndFormData(
        res.data,
        vueContext,
        info.bpmData,
        info.formData
      )
      info.bpmData.actionData.taskId = taskId

      changeTabsMeta({
        fullPath: proxy.$route.fullPath,
        meta: { title: `Processing tasks-${res.data.bpmInstance.title}` },
      })
    }
  })

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