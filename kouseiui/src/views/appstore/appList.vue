<template>
  <div class="comprehensive-table-container userApp">
    <div v-loading="info.loading" style="width: 100%; height: 100%">
      <iframe
        v-if="info.iframeSrc && info.iframeSrc.length > 0"
        frameborder="no"
        :src="info.iframeSrc"
        class="iframe"
        ref="appIframe"
      ></iframe>
    </div>
  </div>
</template>
<script lang="ts" setup>
  import { getData, appstoreApi, sysApi, abTools, postData } from 'agilebpm'
  import { getToken } from '@/utils/token'
  const { proxy } = abTools.useCurrentInstance()

  const info = reactive({
    iframeSrc: 'appstoreAppList?abIframeType=iframe',
    loading: false,
    url: '',
  })

  const getAppstoreUrl = async () => {
    const data = await appstoreApi.install.getAppStoretUrl()
    if (data.data) {
      info.url = data.data
      info.iframeSrc = data.data + info.iframeSrc
    }
  }

  const appIframe = ref()

  const sendValueToIframe = () => {
    appIframe.value.contentWindow.postMessage(
      { type: 'toAppList', url: info.url },
      info.iframeSrc
    )
  }

  // Listen to the value passed from the vue page
  window.onmessage = (e) => {
    if (e.data.type == 'formAppListChild') {
      sendValueToIframe()
    }
    if (e.data.type == 'formOrderCreate') {
      proxy.$router.push({
        path: '/userAppstore/user/userApp',
      })
    }
    if (e.data.type == 'formOrderCreateToOrder') {
      proxy.$router.push({
        path: '/userAppstore/user/orderList',
      })
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
