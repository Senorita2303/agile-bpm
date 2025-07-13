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
    iframeSrc: '?redirect=appstore/store/myApp?abIframeType=iframe',
    loading: false,
    data: [],
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
      {
        type: 'formParent',
        isLocal: true,
        token: getToken(),
        iframeSrc: info.iframeSrc,
        url: info.url,
      },
      info.iframeSrc
    )
  }

  const getapplicationListJson = (arr: any) => {
    postData(sysApi.authApplication.applicationListJson, {
      limit: 99,
    }).then(
      (res) => {
        arr.forEach((item: any) => {
          item.selectoption = []
          if (item.supportMobile) {
            item.pcSelectoption = res.data.rows.filter(
              (s: any) => !s.appType && s.code !== 'tempOpenPlatform'
            )
            item.mobileSelectoption = res.data.rows.filter(
              (s: any) => s.appType
            )
          } else {
            item.pcSelectoption = res.data.rows.filter(
              (s: any) => !s.appType && s.code !== 'tempOpenPlatform'
            )
          }
        })
        info.loading = false
        appIframe.value.contentWindow.postMessage(
          {
            type: 'toDataList',
            data:
              arr && arr.length > 0 ? JSON.stringify(arr) : JSON.stringify([]),
          },
          info.iframeSrc
        )
      },
      () => {}
    )
  }

  // Listen to the value passed from the vue page
  window.onmessage = (e) => {
    if (e.data.type == 'formChild') {
      sendValueToIframe()
    }
    // Listen for events passed from the iframe page
    if (e.data.type == 'installFn') {
      let data = JSON.parse(e.data.data)
      let param = {
        appId: data.id,
        appstoreToken: e.data.appstoreToken,
        pcPlatformId: data.pcPlatformId,
        mobilePlatformId: data.mobilePlatformId || '',
      }
      info.loading = true
      if (data.trialStatus == 1) {
        appstoreApi.install
          .trialUrl(param)
          .then((res) => {
            if (res.isOk && res.data) {
              ElMessage({
                message: res.data,
                type: 'success',
              })
            }
            if (e.data.page == 'list') {
              appIframe.value.contentWindow.postMessage(
                { type: 'listFn' },
                info.iframeSrc
              )
            }
          })
          .catch(() => {
            info.loading = false
            if (e.data.page == 'list') {
              appIframe.value.contentWindow.postMessage(
                { type: 'listFn' },
                info.iframeSrc
              )
            }
          })
      } else {
        appstoreApi.install
          .installUrl(param)
          .then((res) => {
            if (res.isOk && res.data) {
              ElMessage({
                message: res.data,
                type: 'success',
              })
            }
            if (e.data.page == 'list') {
              appIframe.value.contentWindow.postMessage(
                { type: 'listFn' },
                info.iframeSrc
              )
            }
          })
          .catch(() => {
            info.loading = false
            if (e.data.page == 'list') {
              appIframe.value.contentWindow.postMessage(
                { type: 'listFn' },
                info.iframeSrc
              )
            }
          })
      }
    }
    if (e.data.type == 'uninstallFn') {
      let param = {
        appId: e.data.appId,
      }
      info.loading = true
      getData(appstoreApi.install.uninstallUrl, param)
        .then((res) => {
          if (res.isOk && res.data) {
            ElMessage({
              message: res.data,
              type: 'success',
            })
          }
          if (e.data.page == 'list') {
            appIframe.value.contentWindow.postMessage(
              { type: 'listFn' },
              info.iframeSrc
            )
          }
          if (e.data.page == 'details') {
            appIframe.value.contentWindow.postMessage(
              { type: 'details' },
              info.iframeSrc
            )
          }
        })
        .catch(() => {
          info.loading = false
        })
    }
    if (e.data.type == 'upgradationFn') {
      let param = {
        appId: e.data.appId,
        appstoreToken: e.data.appstoreToken,
        version: e.data.version == 'Latest version' ? null : e.data.version,
      }
      info.loading = true
      postData(appstoreApi.install.upgradeUrl, param)
        .then((res) => {
          if (res.isOk && res.data) {
            ElMessage({
              message: res.data,
              type: 'success',
            })
          }
          if (e.data.page == 'list') {
            appIframe.value.contentWindow.postMessage(
              { type: 'listFn' },
              info.iframeSrc
            )
          }
        })
        .catch(() => {
          info.loading = false
        })
    }
    if (e.data.type == 'purchasedFn') {
      let data = JSON.parse(e.data.data)
      let param = {
        appId: data.id,
        appstoreToken: e.data.appstoreToken,
      }
      info.loading = true
      postData(appstoreApi.install.purchasedUrl, param)
        .then((res) => {
          if (res.isOk && res.data) {
            ElMessage({
              message: res.data,
              type: 'success',
            })
          }
          if (e.data.page == 'list') {
            appIframe.value.contentWindow.postMessage(
              { type: 'listFn' },
              info.iframeSrc
            )
          }
        })
        .catch(() => {
          info.loading = false
        })
    }
    if (e.data.type == 'formMyappList') {
      getData(appstoreApi.install.getUninstallAppIdsUrl)
        .then((res) => {
          if (res.isOk) {
            let list = JSON.parse(e.data.list)
            if (res.data && res.data.length > 0) {
              list.forEach((item: any) => {
                if (!res.data.includes(item.id)) {
                  item.installStatus = 0
                }
              })
            } else {
              list.forEach((item: any) => {
                item.installStatus = 0
              })
            }
            info.data = JSON.parse(JSON.stringify(list))
            getapplicationListJson(info.data)
          }
        })
        .catch(() => {})
    }
    if (e.data.type == 'toformOrderCreateToOrder') {
      proxy.$router.push({
        path: '/userAppstore/user/orderList',
      })
    }
  }

  getAppstoreUrl()
  // onMounted(() => {
  //   getAppstoreUrl()
  // })
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
