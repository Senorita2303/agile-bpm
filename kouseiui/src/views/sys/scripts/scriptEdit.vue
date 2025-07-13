<template>
  <el-container class="layout-container-demo">
    <el-header>
      <ab-save
        back-name="ScriptList"
        :form-ref="formRef"
        :save-data="info.data"
        :url="sysApi.script.scriptSave"
      />
      <ab-load v-model="info.data" :url="sysApi.script.scriptGet" />
      <el-button
        v-ab-btn-rights:script_test
        style="margin-left: 20px"
        type="primary"
        @click="scriptTest(info.data.name, info.data.script)"
      >
        Script testing
      </el-button>
    </el-header>
    <el-divider class="dividermar" />
    <el-main>
      <el-scrollbar>
        <el-form
          ref="formRef"
          label-suffix="："
          :model="info.data"
          :status-icon="false"
        >
          <el-form-item
            label="Name"
            label-width="170px"
            placeholder="Please enter a name"
            prop="name"
            :rules="[{ required: true, message: 'Required' }]"
          >
            <el-input v-model="info.data.name" />
          </el-form-item>
          <el-form-item
            label="Script"
            label-width="170px"
            placeholder="Please enter the script"
            prop="script"
            :rules="[{ required: true, message: 'Required' }]"
          >
            <codemirror
              v-model="info.data.script"
              :autofocus="true"
              :indent-with-tab="true"
              placeholder="Please enter the groovy code (language like Java)"
              :style="{
                height: `400px`,
                border: `1px solid #e4e7ed`,
                width: `500px`,
              }"
            />
          </el-form-item>
          <el-form-item
            label="Script Classification"
            label-width="170px"
            prop="typeCode"
            :rules="[{ required: true, message: 'required' }]"
          >
            <ab-select-tree v-model="info.data.typeCode" type-code="script" />
          </el-form-item>
          <el-form-item
            label="Remark"
            label-width="170px"
            placeholder="Please enter a comment"
            prop="desc"
            :rules="[{ required: true, message: 'Required' }]"
          >
            <el-input v-model="info.data.desc" type="textarea" />
          </el-form-item>
        </el-form>
      </el-scrollbar>
    </el-main>
  </el-container>
</template>

<script setup lang="ts">
  import { reactive, ref } from 'vue'
  import { sysApi, abTools } from 'agilebpm'
  import { Codemirror } from 'vue-codemirror'
  import { ElMessage } from 'element-plus'
  const formRef = ref()
  const info: any = reactive({
    data: {
      id: '',
      name: '',
      script: '',
      typeCode: '',
      value: '',
      desc: '',
    },
  })

  const scriptTest = (name: string, script: string) => {
    if (!script || script === '') {
      ElMessage.warning('Please enter the script')
      return
    }
    sysApi.script.scriptTest(name, script).then((result: any) => {
      ElMessage({
        message: result.data,
        type: 'success',
        offset: 300,
      })
    })
  }
  const { proxy } = abTools.useCurrentInstance()
  onMounted(() => {
    info.data.typeCode = proxy.$route.query.typeCode
  })
</script>
