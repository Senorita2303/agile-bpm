<template>
  <el-container class="layout-container-demo">
    <el-header height="80px">
      <ab-save
        :after-save-fn="saveAfter"
        back-name="BpmDefinitionList"
        :form-ref="formRef"
        :save-data="data.bpmDefinition"
        :url="bpmApi.bpmDefinition.bpmDefinitionSaveUrl"
      />
      <ab-load
        v-model="data.bpmDefinition"
        :get-param="id"
        :url="bpmApi.bpmDefinition.bpmDefinitionGetUrl"
      />
    </el-header>
    <el-divider class="dividermar" />
    <el-main>
      <el-form
        ref="formRef"
        label-suffix="："
        label-width="150px"
        :model="data.bpmDefinition"
        :status-icon="false"
      >
        <el-form-item
          label="Process Name"
          prop="name"
          :rules="[
            { required: true, message: 'Required' },
            { max: 50, message: 'You can enter up to 50 characters' },
          ]"
        >
          <ab-pinyin
            v-model="data.bpmDefinition.name"
            v-model:copy="data.bpmDefinition.desc"
            v-model:to="data.bpmDefinition.key"
          />
        </el-form-item>
        <el-form-item
          label="Workflow Code"
          prop="key"
          :rules="[
            { required: true, message: 'Required' },
            { max: 50, message: 'You can enter up to 50 characters' },
            {
              pattern: /^[a-zA-Z]\w*$/,
              message: 'It can only start with a letter, letters, numbers and underscores are allowed',
            },
          ]"
        >
          <ab-code
            v-model="data.bpmDefinition.key"
            :disabled="data.bpmDefinition.id"
          />
        </el-form-item>
        <el-form-item
          label="Type"
          prop="typeCode"
          :rules="[{ required: true, message: 'Required' }]"
        >
          <ab-select-tree
            v-model="data.bpmDefinition.typeCode"
            type-code="flowType"
          />
        </el-form-item>
        <el-form-item
          label="Description"
          prop="desc"
          :rules="[
            { required: true, message: 'Required' },
            { max: 500, message: 'You can enter up to 500 characters' },
          ]"
        >
          <el-input v-model="data.bpmDefinition.desc" type="textarea" />
        </el-form-item>
        <el-form-item label="Order" prop="sn">
          <el-input v-model="data.bpmDefinition.sn" type="number" />
        </el-form-item>
        <el-form-item label="ICON" prop="iconStyle">
          <ab-choose-svg v-model="data.bpmDefinition.iconStyle" />
        </el-form-item>
        <el-form-item label="Hidden or not" prop="hidden">
          <el-tooltip
            content="After hiding, it will no longer be displayed on the Personal Office-Initiate Application page, but you can still initiate the process through other methods"
          >
            <el-switch
              v-model="data.bpmDefinition.hidden"
              active-text="Yes"
              :active-value="1"
              inactive-text="No"
              :inactive-value="0"
              inline-prompt
            />
          </el-tooltip>
        </el-form-item>
      </el-form>
    </el-main>
  </el-container>
</template>

<script setup lang="ts">
  import { reactive, ref } from 'vue'
  import type { FormInstance } from 'element-plus'
  import { bpmApi, abUtil, abTools } from 'agilebpm'
  import { abChooseSvg } from 'agilebpm'

  // Import current instance
  const { proxy } = abTools.useCurrentInstance()
  const formRef = ref<FormInstance>()
  const id = proxy.$route.query.id
  const data = reactive({
    bpmDefinition: {
      name: '',
      key: '',
      desc: '',
      sn: '10',
      iconStyle: '',
      typeCode: '',
      id: null as any,
      processEditor: 'empty',
      hidden: 0,
    },
  })

  const saveAfter = (data: any) => {
    if (!id) {
      proxy.$router.push({
        name: 'BpmDefinitionList',
        query: { newDefinitionId: data },
      })
    } else {
      proxy.$router.push({ name: 'BpmDefinitionList' })
    }

    return false
  }

  onMounted(() => {
    data.bpmDefinition.typeCode = proxy.$route.query.typeCode
  })
</script>
