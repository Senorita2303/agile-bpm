<template>
  <el-container style="padding: 20px">
    <el-main>
      <ab-load v-model="pageInfo.data" url="/ab-bpm/demo/applyOrder/get" />
      <el-form ref="formRef" label-suffix="：" :model="pageInfo.data">
        <el-form-item label="Name">
          <el-input v-model="pageInfo.data.qdjl" />
        </el-form-item>
        <el-form-item label="Notes">
          <el-input v-model="pageInfo.data.remark" type="textarea" />
        </el-form-item>
        <el-form-item label="Current page URL">
          {{ url }}
        </el-form-item>
      </el-form>
    </el-main>
  </el-container>
</template>

<script lang="ts" setup>
  import { reactive, ref } from 'vue'
  import { ElMessage } from 'element-plus'
  const formRef = ref()
  const pageInfo: any = reactive({
    data: {
      qdjl: '',
      remark: '',
    },
  })

  const url = ref('')

  onMounted(() => {
    url.value = window.location.href

    // [1] Notify the BPM page that there is an event interaction. If the parent page is not notified, the parent page will not call back. Get the data verification form
    window.parent.postMessage(
      { type: 'callback', name: 'subIframeCallback' },
      '*'
    )

    // [2] Listen to the "getDataReady" event of the main page to get data, return the page JSON, and check whether the form verification is passed
    window.onmessage = (e) => {
      const data = e.data || {}
      if (data.type != 'getDataReady') {
        return
      }

      // [3] Self-check the current form and return the result
      let valid = true
      let errorMsg = ''
      if (!pageInfo.data.qdjl) {
        valid = false
        errorMsg = 'Name cannot be empty'
        // Provide form error prompts as needed, and non-submit actions such as clicking on flowcharts do not require verification
        if (data.isValid) {
          ElMessage({
            message:
              'The business form verification failed, the name cannot be empty! Please provide form error prompts as needed',
            type: 'warning',
          })
        }
      }

      // The logic of obtaining data, [must be copied] is important
      const formData = JSON.parse(JSON.stringify(pageInfo.data))

      // Send data reporting event
      window.parent.postMessage(
        {
          type: 'getData',
          url: document.location.href,
          valid: valid,
          errorMsg: errorMsg,
          data: formData,
        },
        data.url
      )
    }
  })
</script>
