<template>
  <el-button @click="countPlus">{{ user.userName }} - Click to increase the price</el-button>
  <el-input-number v-model="num" :max="10" :min="1" @change="num++" />
</template>
<script lang="ts" setup>
  import { ref, onMounted } from 'vue'
  import { abTools } from 'agilebpm'

  const num = ref(2)
  // Definition of input parameters for all components
  const props = defineProps({
    // By default, the user will define it as Object any. If the type is specified, then: type: Object as PropType<User>,
    user: { required: true, type: Object },
    // Step length
    stepLenth: { required: false, default: 1, type: Number },
  })

  // emit Event, the parent class updates itself according to the event
  const emit = defineEmits(['custEvent'])
  //Self-reference is similar to the previous this, such as obtaining routing and other information
  const { proxy } = abTools.useCurrentInstance()
  console.info(proxy)

  // mounted timing
  onMounted(() => {
    // It cannot be used externally
    console.info(props.user.userName)
  })

  // function
  const countPlus = () => {
    // Make the parent component unsmooth
    emit('custEvent', num.value)
  }

  const childConst = ref('I am some function object in the sub-sub-component, etc.')
  // Allow external methods to be called
  defineExpose({ countPlus, childConst })
</script>
