<template>
  <el-container
    v-loading="!info.bpmData.defName"
    class="layout-container-demo"
    style="display: block; overflow: auto"
  >
    <el-main>
      <el-container>
        <el-header v-if="info.bpmData.defName">
          <bpm-buttons
            v-if="info.bpmData.buttonList.length"
            :bpm-data="info.bpmData"
            class="print-hidden"
          />
        </el-header>
        <el-divider class="dividermar" />
        <el-main v-if="info.bpmData.defName">
          <ab-url-form
            v-if="info.bpmData.bpmForm.type == 'url'"
            ref="url"
            :bpm-form="info.bpmData.bpmForm"
          />
          <cust-form v-else ref="inner" :form-data="info.formData" />
        </el-main>
      </el-container>
    </el-main>
  </el-container>
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
  } from 'agilebpm'
  import { getCurrentInstance, Ref } from 'vue'

  const { proxy } = abTools.useCurrentInstance()
  const vueContext = getCurrentInstance()
  const { changeTabsMeta } = useAbStoreAdapter()
  const instId = vueContext?.proxy?.$route.query.instId
  const info: any = reactive({
    bpmData: {},
    formData: {},
  })

  onMounted(() => {
    bpmApi.bpmData.getDraftData(instId).then(({ data }) => {
      bpmTools.initBpmDataAndFormData(
        data,
        vueContext,
        info.bpmData,
        info.formData
      )
      info.bpmData.actionData.instanceId = instId

      changeTabsMeta({
        fullPath: proxy.$route.fullPath,
        meta: { title: `Draft started-${data.bpmInstance.title}` },
      })
    })
  })
</script>
