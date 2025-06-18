<template>
  <el-container class="layout-container-demo">
    <el-header>
      <router-link
        v-if="type != '1'"
        :to="{
          name: 'ApplicationList',
        }"
      >
        <el-button :icon="Back">Back</el-button>
      </router-link>
      <ab-save
        v-else
        back-name="ApplicationList"
        :form-ref="formRef"
        :save-data="info.data"
        :url="sysApi.authApplication.applicationSaveOrUpdate"
      />
      <ab-load
        v-model="info.data"
        :url="sysApi.authApplication.applicationGet"
      />
    </el-header>
    <el-divider class="dividermar" />
    <el-main>
      <el-scrollbar>
        <el-form
          ref="formRef"
          label-suffix="："
          label-width="120px"
          :model="info.data"
          :status-icon="false"
        >
          <el-form-item
            label="System Name"
            prop="name"
            :rules="[{ required: true, message: 'Required' }]"
          >
            <el-input
              v-model="info.data.name"
              :disabled="type != '1'"
              placeholder="Please enter a system name"
            />
          </el-form-item>

          <el-form-item
            label="System code"
            prop="code"
            :rules="[{ required: true, message: 'Required' }, info.validatorMap]"
          >
            <el-input
              v-model="info.data.code"
              :disabled="info.data.id"
              placeholder="Please enter the system code"
            />
          </el-form-item>
          <el-form-item label="Application Type" prop="appType">
            <el-radio-group v-model="info.data.appType">
              <el-radio :label="0" size="large">Website</el-radio>
              <el-radio :label="1" size="large">Mobile</el-radio>
              <el-radio :label="2" size="large">Third-party applications</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item
            label="url address"
            prop="url"
            v-if="info.data.appType == 2"
          >
            <el-input
              v-model="info.data.url"
              :disabled="type != '1'"
              placeholder="Please enter the system URL address"
            />
          </el-form-item>

          <el-form-item
            label="Callback url address"
            prop="redirectUri"
            v-if="info.data.appType == 2"
          >
            <el-input
              v-model="info.data.redirectUri"
              :disabled="type != '1'"
              placeholder="Please enter the callback URL address"
            />
          </el-form-item>

          <el-form-item
            label="How to open"
            prop="openType"
            v-if="info.data.appType == 2"
          >
            <el-radio
              v-model="info.data.openType"
              :disabled="type != '1'"
              :label="'0'"
            >
              Jump
            </el-radio>
            <el-radio
              v-model="info.data.openType"
              :disabled="type != '1'"
              :label="'1'"
            >
              Redirect
            </el-radio>
          </el-form-item>

          <el-form-item label="Key" prop="secret">
            <el-input
              v-model="info.data.secret"
              :disabled="type != '1'"
              placeholder="Please enter the key"
            />
          </el-form-item>

          <el-form-item
            label="refresh validity period"
            prop="refreshTokenValidity"
            :rules="[
              { pattern: /^-?[0-9]\d*$/, message: 'Please do not enter any characters other than numbers' },
            ]"
          >
            <el-input
              v-model="info.data.refreshTokenValidity"
              :disabled="type != '1'"
              placeholder="Please enter the refresh token validity period (unit: seconds)"
            />
          </el-form-item>

          <el-form-item
            label="access validity period"
            prop="accessTokenValidity"
            :rules="[
              { pattern: /^-?[0-9]\d*$/, message: 'Please do not enter any characters other than numbers' },
            ]"
          >
            <el-input
              v-model="info.data.accessTokenValidity"
              :disabled="type != '1'"
              placeholder="Please enter the validity period of the key token (unit: seconds)"
            />
          </el-form-item>

          <el-form-item label="Available status" prop="enabled">
            <el-switch
              v-model="info.data.enabled"
              :active-value="1"
              :disabled="type != '1'"
              :inactive-value="0"
            />
          </el-form-item>

          <el-form-item label="Automatic authorization" prop="autoapprove">
            <el-switch
              v-model="info.data.autoapprove"
              :active-value="1"
              :disabled="type != '1'"
              :inactive-value="0"
            />
          </el-form-item>
          <el-form-item label="Is it the default system?" prop="isDefault">
            <el-switch
              v-model="info.data.isDefault"
              :active-value="1"
              :disabled="type != '1'"
              :inactive-value="0"
            />
          </el-form-item>
          <el-form-item
            label="Authorization Type"
            placeholder="Authorization Type"
            prop="grantTypes"
          >
            <el-checkbox-group v-model="info.grantType" :disabled="type != '1'">
              <el-checkbox key="refresh_token" label="refresh_token" />
              <el-checkbox key="password" label="password" />
              <el-checkbox
                key="authorization_code"
                label="authorization_code"
              />
              <el-checkbox key="implicit" label="implicit" />
              <el-checkbox
                key="client_credentials"
                label="client_credentials"
              />
            </el-checkbox-group>
          </el-form-item>
          <el-form-item label="Scope of authorization" placeholder="Scope of authorization" prop="scope">
            <el-input
              v-model="info.data.scope"
              :disabled="type != '1'"
              placeholder="Please enter the authorization scope"
            />
          </el-form-item>

          <el-form-item
            label="Resource collection"
            placeholder="Resource collection"
            prop="resourceIds"
          >
            <el-input
              v-model="info.data.resourceIds"
              :disabled="type != '1'"
              placeholder="Please enter a resource set"
            />
          </el-form-item>

          <!-- <el-form-item label="Extended configuration" placeholder="Extended configuration" prop="config">
            <el-input
              v-model="info.data.config"
              :disabled="type != '1'"
              placeholder="Please enter the extended configuration"
            />
          </el-form-item> -->

          <el-form-item label="Description information" prop="desc">
            <el-input
              v-model="info.data.desc"
              :disabled="type != '1'"
              placeholder="Please enter a description"
              type="textarea"
            />
          </el-form-item>
        </el-form>
      </el-scrollbar>
    </el-main>
  </el-container>
</template>
<!--
<script lang="ts">
  let type = ''
  // Get which route it comes from
  export default defineComponent({
    beforeRouteEnter(to, from, next) {
      console.log('from', from)
      if (from.name) {
        type = to.name == 'ApplicationEdit' ? '1' : '0'
        console.log(type)
      }
    },
  })
</script> -->

<script setup lang="ts">
import { getCurrentInstance, reactive, ref } from 'vue'
import { Back } from '@element-plus/icons-vue'
import { sysApi, bizApi } from 'agilebpm'
const formRef = ref()
const info: any = reactive({
  type: '',
  isEdit: false,
  validatorMap: '',
  grantType: [] as any[],
  data: {
    name: '',
    code: '',
    secret: '',
    scope: '', // Scope of authorization
    resourceIds: '', // Resource collection
    grantTypes: '', // Authorization Type
    autoapprove: '', // Automatic authorization
    authorities: '', // Permissions
    refreshTokenValidity: '',
    accessTokenValidity: '',
    url: '',
    redirectUri: '',
    openType: '',
    enabled: 1,
    desc: '',
    config: '',
    isDefault: 0,
    appType: 0,
  },
})

// Get details or edit flags by parameters (read-only/modify)
const type = getCurrentInstance()?.proxy?.$route.query.type

bizApi.bizPattern
  .getAllBizValidator()
  .then((data: any) => (info.validatorMap = data.varirule.rule))

watch(
  () => info.grantType,
  () => (info.data.grantTypes = info.grantType.join(','))
)

watch(
  () => info.data.grantTypes,
  () => (info.grantType = info.data.grantTypes.split(','))
)
</script>
