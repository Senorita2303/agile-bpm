<template>
  <div class="comprehensive-table-container userApp">
    <div v-loading="info.loading" style="width: 100%; height: 100%">
      <iframe
        v-if="info.iframeSrc && info.iframeSrc.length > 0"
        frameborder="no"
        :src="info.iframeSrc"
        class="iframe"
        ref="orderIframe"
      ></iframe>
    </div>
  </div>
</template>
<script lang="ts" setup>
  import { getData, appstoreApi, abTools, postData } from 'agilebpm'
  import { getToken } from '@/utils/token'
  const { proxy } = abTools.useCurrentInstance()

  const info = reactive({
    iframeSrc: '?redirect=appstore/appManage/myOrder?abIframeType=iframe',
    loading: false,
  })

  const getAppstoreUrl = async () => {
    const data = await appstoreApi.install.getAppStoretUrl()
    if (data.data) {
      info.iframeSrc = data.data + info.iframeSrc
    }
  }

  const orderIframe = ref()

  const sendOrderToIframe = () => {
    orderIframe.value.contentWindow.postMessage(
      { type: 'formOrderParent', token: getToken(), iframeSrc: info.iframeSrc },
      info.iframeSrc
    )
  }

  // Listen to the value passed from the vue page
  window.onmessage = (e) => {
    if (e.data.type == 'formOrderChild') {
      sendOrderToIframe()
    }
  }

  onMounted(() => {
    getAppstoreUrl()
  })
</script>
<style lang="scss" scoped>
  .userApp {
    height: calc(100vh - 60px - 50px - 20px * 2 - 55px);
    padding: 0 !important;
    .iframe {
      width: 100%;
      height: 100%;
    }
  }
</style>
