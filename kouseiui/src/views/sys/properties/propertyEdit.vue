<template>
  <el-container class="layout-container-demo">
    <el-header>
      <ab-save
        back-name="PropertyList"
        :form-ref="formRef"
        :save-data="info.data"
        :url="sysApi.property.propertySave"
      />
      <ab-load v-model="info.data" :url="sysApi.property.propertyGet" />
    </el-header>
    <el-divider class="dividermar" />
    <el-main>
      <el-scrollbar>
        <el-form ref="formRef" label-suffix="：" :model="info.data">
          <el-form-item
            label="Parameter name"
            label-width="150px"
            placeholder="Please enter the parameter name"
            prop="name"
            :rules="[{ required: true, message: 'Required' }]"
          >
            <ab-pinyin v-model="info.data.name" v-model:to="info.data.code" />
          </el-form-item>
          <el-form-item
            label="Code"
            label-width="150px"
            placeholder="Please enter the code"
            prop="code"
            :rules="[{ required: true, message: 'Required' }]"
          >
            <el-input v-model="info.data.code" :disabled="info.data.id" />
          </el-form-item>
          <el-form-item
            label="Environment"
            label-width="150px"
            prop="environment"
            :rules="[{ required: true, message: 'Required' }]"
          >
            <ab-select-tree
              v-model="info.data.environment"
              :operation="false"
              type-code="environment"
            />
          </el-form-item>
          <el-form-item
            label="Type"
            label-width="150px"
            placeholder="Please enter a type"
            prop="typeCode"
            :rules="[{ required: true, message: 'Required' }]"
          >
            <ab-select-tree
              v-model="info.data.typeCode"
              :operation="false"
              type-code="property"
            />
          </el-form-item>

          <el-form-item
            v-if="id && info.encryptItem === 1 && !info.encryptChange"
            label="Value is encrypted"
            label-width="150px"
          >
            <el-input v-model="info.encryptValue" disabled />
          </el-form-item>

          <el-form-item
            label="Value"
            label-width="150px"
            prop="value"
            :rules="[{ required: true, message: 'Required' }]"
          >
            <el-input
              v-model="info.data.value"
              :placeholder="!id ? 'Please enter the attribute value' : 'Please re-enter the attribute value'"
            />
          </el-form-item>

          <el-form-item label="Encryption" label-width="150px" prop="encrypt">
            <el-switch
              v-model="info.data.encrypt"
              :active-value="1"
              :inactive-value="0"
              @change="changeFn"
            />
          </el-form-item>
          <el-form-item label="Description" label-width="150px" prop="desc">
            <el-input v-model="info.data.desc" type="textarea" />
          </el-form-item>
        </el-form>
      </el-scrollbar>
    </el-main>
  </el-container>
</template>

<script setup lang="ts">
  import { reactive } from 'vue'
  import { sysApi, abTools, bizApi } from 'agilebpm'
  const formRef = ref()
  const info: any = reactive({
    data: {
      name: '',
      code: '',
      environment: '',
      value: '',
      desc: '',
      encrypt: 0,
      group: '',
      typeCode: '',
      categorys: [],
      category: '',
    },
    encryptValue: '******',
    encryptItem: 0,
    encryptChange: false,
    validatorMap: {},
  })
  const { proxy } = abTools.useCurrentInstance()

  info.data.typeCode = proxy.$route.query.typeCode
  const id = proxy.$route.query.id

  let autoChange = false
  watch(
    () => info.data.encrypt,
    (newValue: number) => {
      if (!info.encryptChange) {
        info.encryptItem = newValue
      }
      autoChange = true
    }
  )

  const changeFn = () => {
    info.encryptChange = true
  }

  // Encoding verification
  bizApi.bizPattern
    .getAllBizValidator()
    .then((data: any) => (info.validatorMap = data.varirule.rule))
</script>
