<template>
  <div v-loading="!props.bpmData" style="overflow: auto">
    <div :class="showTaskDynamic(info.bpmData) ? 'leftBoxDyminc' : 'leftBox'">
      <div v-if="props.bpmData.defName">
        <bpm-buttons
          v-if="props.bpmData.buttonList.length"
          :bpm-data="props.bpmData"
          class="print-hidden"
        />
      </div>
      <div
        v-if="props.bpmData.defName"
        class="leftContent"
        :style="{ height: height }"
      >
        <ab-url-form
          v-if="props.bpmData.bpmForm.type == 'url'"
          ref="url"
          :bpm-form="props.bpmData.bpmForm"
        />
        <cust-form v-else ref="inner" :form-data="props.formData" />
      </div>
    </div>
    <div v-if="showTaskDynamic(info.bpmData)" class="rightBox">
      <bpm-task-dynamic :instance="props.bpmData.bpmInstance" />
    </div>
  </div>
</template>

<script lang="ts" setup>
  import { abForm as custForm, bpmButtons, abUrlForm } from 'agilebpm'
  import bpmTaskDynamic from '../myTask/components/task-dynamic.vue'
  import { showTaskDynamic } from '../myTask/components/myTaskConfig'

  const props = defineProps({
    bpmData: { type: Object, required: true },
    formData: { type: Object, required: true },
  })

  const height = ref(`${window.innerHeight - 279}px`)
</script>
<style lang="scss" scoped>
  @import '../myTask/components/taskDynamic.scss';
</style>
