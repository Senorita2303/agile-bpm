<template>
  <div
    v-if="pageInfo.data"
    class="previewBox"
    :class="
      pageInfo.data.designJson.theme == '2'
        ? 'gatewayThemeClass'
        : pageInfo.data.designJson.theme == '3'
        ? 'largeScreenThemeClass'
        : pageInfo.data.designJson.theme == '4'
        ? 'redThemeClass'
        : 'formThemeClass'
    "
    style="position: relative"
    :style="setDashboardBg()"
  >
    <!-- <div style="position: absolute; top: 0; right: 2px; z-index: 99">
      <span style="cursor: pointer" text type="default" @click="back()">
        <el-icon :size="20"><Close /></el-icon>
      </span>
    </div> -->
    <FormCombinationPreview
      v-if="pageInfo.data"
      :design-code="pageInfo.data.code"
      :design-json="pageInfo.data.designJson"
      :params="pageInfo.data.varibales"
    />
  </div>
</template>
<script lang="ts" setup>
  import { abFormCombinationPreview as FormCombinationPreview } from 'abLayoutDesigner'
  import { bizApi, abUtil, abTools, sysApi } from 'agilebpm'
  import { Close } from '@element-plus/icons-vue'

  const { proxy } = abTools.useCurrentInstance()
  /* const { changeTabsMeta } = useTabsStore()

  if (proxy.$route.params.code) {
    bizApi.formCombination
      .getByCode(proxy.$route.params.code)
      .then((rel: any) => {
        changeTabsMeta({
          fullPath: proxy.$route.fullPath,
          meta: { title: `${rel.data.name}` },
        })
      })
  } */

  const pageInfo = reactive({
    data: null as any,
    isLoad: false,
    fullPath: '',
  })

  // changeTabsMeta({
  //   fullPath: proxy.$route.fullPath,
  //   meta: { title: `Detailed examples` },
  // })

  const loadData = () => {
    const code = proxy.$route.params.code

    const params = {} as any
    abUtil.easyClone(proxy.$route.params, params)
    abUtil.easyClone(proxy.$route.query, params)
    if (code) {
      bizApi.formCombination.getDesignJson(code, params).then((result) => {
        pageInfo.data = result.data
      })
    }
  }

  loadData()
  // Cache wake-up, or load the page when entering for the first time
  // onActivated(() => {
  //   if (pageInfo.fullPath != proxy.$route.fullPath) {
  //     pageInfo.fullPath = proxy.$route.fullPath
  //     loadData()
  //   }
  // })

  const back = () => {
    abTools.closeTab(proxy.$route.path)
    proxy.$router ? proxy.$router.back() : window.history.back()
  }

  const setDashboardBg = () => {
    const designJson = pageInfo.data.designJson
    let backgroundStyle = {}
    // If it is a picture background
    if (designJson.dashboardBg == 'picture') {
      let url = ''
      if (designJson.dashboardBgPicture.length > 0) {
        const dashboardBgPicture = JSON.parse(designJson.dashboardBgPicture)
        if (
          dashboardBgPicture[0] &&
          dashboardBgPicture[0].id &&
          dashboardBgPicture[0].id.length > 0
        ) {
          url = sysApi.sysFile.getViewFileUrl(dashboardBgPicture[0].id)
        }
      } else {
        url = ''
      }
      if (url.length > 0) {
        backgroundStyle = {
          backgroundImage: `url("${url}")`,
          backgroundRepeat: ' no-repeat' /* Set the background image to not repeat */,
          backgroundSize: 'cover' /* Set the background image to adapt to the container size */,
          backgroundPosition: 'center' /* Set the background image to be centered in the container */,
          opacity: designJson.dashboardTransparency,
        }
      } else {
        // backgroundStyle = {
        //   background: `#fff`,
        // }
      }
    }
    // If it is a color background
    if (designJson.dashboardBg && designJson.dashboardBg == 'color') {
      if (
        designJson.dashboardBgColor &&
        designJson.dashboardBgColor.length > 0
      ) {
        backgroundStyle = {
          background: designJson.dashboardBgColor,
        }
      } else {
        // backgroundStyle = {
        //   background: `#fff`,
        // }
      }
    }
    return backgroundStyle
  }
</script>
<style lang="scss" scoped></style>
