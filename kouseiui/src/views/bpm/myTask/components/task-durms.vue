<template>
  <vab-chart
    class="branch-echart"
    :init-options="info.initOptions"
    :option="info.option"
    theme="vab-echarts-theme"
  />
</template>

<script setup lang="ts">
  import VabChart from '@/plugins/VabChart'
  const props = defineProps({
    dataList: { type: Array, required: true },
  })

  const colors = ['#5470C6', '#91CC75', '#EE6666']
  const info = reactive({
    initOptions: {
      renderer: 'svg',
    },
    option: {
      title: {
        text: 'Process timeliness prediction',
        left: 'center',
      },
      tooltip: {
        trigger: 'item',
        formatter: '{b}<br/> ({d}%)',
      },
      series: [
        {
          name: 'Node time consumption',
          type: 'pie',
          radius: ['25%', '45%'],
          itemStyle: {
            borderRadius: 10,
            borderColor: '#fff',
            borderWidth: 2,
          },
          emphasis: {
            label: {
              show: true,
            },
          },
          data: props.dataList,
        },
      ],
    },
  })
</script>
<style lang="scss">
  .branch-echart {
    width: 400px;
    height: 350px;
  }
</style>
