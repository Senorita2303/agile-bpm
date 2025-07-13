<template>
  <el-container class="layout-container-demo">
    <el-header>
      <router-link
        v-if="type != '1'"
        :to="{
          name: 'DailyPhrasesList',
        }"
      >
        <el-button :icon="Back">Back</el-button>
      </router-link>
      <ab-save
        v-else
        back-name="DailyPhrasesList"
        :form-ref="formRef"
        :save-data="info.data"
        :url="sysApi.sysDailyPhrases.dailyPhrasesSaveOrUpdate"
      />
      <ab-load
        v-model="info.data"
        :url="sysApi.sysDailyPhrases.dailyPhrasesGet"
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
          @submit.prevent
        >
          <el-form-item
            label="Commonly used words"
            prop="locution"
            :rules="[{ required: true, message: 'Required' }]"
          >
            <el-input
              v-model="info.data.locution"
              :disabled="info.data.isDefault === 1 || type != '1'"
              placeholder="Please enter a common phrase"
            />
          </el-form-item>
          <el-form-item label="Status" prop="enable">
            <el-switch
              v-model="info.data.enable"
              :active-value="1"
              :disabled="type != '1'"
              :inactive-value="0"
              :rules="[{ required: true, message: 'Required' }]"
            />
          </el-form-item>
        </el-form>
      </el-scrollbar>
    </el-main>
  </el-container>
</template>

<script setup lang="ts">
  import { Back } from '@element-plus/icons-vue'
  import { sysApi } from 'agilebpm'
  const formRef = ref()
  const type = getCurrentInstance()?.proxy?.$route.query.type
  const info: any = reactive({
    data: {
      locution: '',
      enable: true,
    },
  })
</script>
