<template>
  <DemoChild ref="childRef" :user="user" @cust-event="custEventFn" />
  <h1>amount :{{ amount }}</h1>
  <h2>No two-way binding number:{{ number }}</h2>
  <abTest />
  <chooseIconNew />
  <abUploadFile />
</template>

<script lang="ts" setup>
  import {
    reactive,
    onMounted,
    ref,
    computed,
    watch,
    getCurrentInstance,
  } from 'vue'

  import DemoChild from './demoPageChild'
  //import {abTest,chooseIconNew,abUploadFile} from '@agilebpm/build/ab-core/ab-core.umd.min.js'
  const vueContext = getCurrentInstance()
  // Source code import，
  // Library file import
  //import abBpm from '~/libs/ab-bpm/ab-bpm.umd.min.js'

  // reactive Returns a responsive copy of an objec
  const user = reactive({
    userName: 'Wang Xiaoer',
    age: 11,
    phone: '111',
  })
  // Basic types
  const count = ref(1)
  // Specify object types, etc. using generics, any any type, multiple types <string | number>
  const price = ref<number>(1)
  // Computed type properties
  const amount = computed(() => {
    return price.value * count.value
  })

  // Define type let as a mutable type const is immutable, similar to the java final modifier
  const numberType = 100
  // Automatically infer the type based on the default value
  let number = 100
  // The value can only be a few values ​​of a certain type
  // let size: 100 | 200 | 300
  //size = 110 error
  const size = 100
  // Define the type as two or any type any
  let numbers: number | string

  // Define function: define input parameter type
  const custEventFn = (step: number) => {
    // The package type must be obtained by value
    count.value = count.value + step
    // Without ref, there will be no two-way binding, and the value cannot be rendered when it is modified
    number = number + step
  }

  // Get the reference of the subcomponent (cannot be used during setup, can only be used during onMounted)  => void }| null
  const childRef = ref<{ childConst: string; countPlus: () => void } | null>(
    null
  )

  // Watch the value of a two-way binding to monitor changes
  watch(
    () => amount.value,
    (amount, prevamount) => {
      console.info(`newValue ${amount}，oldValue ${prevamount}`)
    }
  )

  // Actions that will be executed when mounted, more opportunities such as beforeMount, updated, etc.
  onMounted(() => {
    // Calling subcomponent content through subcomponent reference
    console.info(childRef.value?.childConst)
    childRef.value?.countPlus()

    console.info(
      `You can initialize the action. You can use the input parameters, such as ${vueContext}. For example: ${count.value}
      There is no two-way binding in the template ${numberType}, ${number} ，${size} ,${numbers}
      `
    )
  })
</script>
