<template>
  <div v-loading="info.loading">
    <ab-save
      v-if="!info.readonly"
      :after-save-fn="saveAfter"
      after-save-stay
      :back-btn="false"
      back-name="groupDetail"
      :before-save-fn="saveBefore"
      :form-ref="formRef"
      :save-data="info.group"
      :url="orgApi.group.saveOrgGroupUrl"
    />
    <el-button
      v-show="info.readonly"
      v-ab-btn-rights:groupManager_edit
      style="margin-left: 10px"
      @click="editOrg"
    >
      Edit
    </el-button>
    <el-button
      v-if="props.operationType !== 'add'"
      v-show="!info.readonly"
      style="margin-left: 10px"
      @click="cancel"
    >
      Cancel
    </el-button>
    <el-form
      ref="formRef"
      :label-width="200"
      :model="info.group"
      style="margin-top: 20px"
    >
      <el-form-item label="Parent organization" prop="parentName">
        <el-input v-model="info.group.parentName" :disabled="true" />
      </el-form-item>
      <el-form-item
        label="Organization name"
        prop="name"
        :rules="[
          { required: true, message: 'Required', trigger: 'blur' },
          { max: 50, message: 'You can enter up to 50 characters' },
        ]"
      >
        <ab-pinyin
          v-model="info.group.name"
          v-model:to="info.group.code"
          clearable
          :disabled="info.readonly"
        />
      </el-form-item>
      <el-form-item
        label="Organization code"
        prop="code"
        :rules="[
          { required: true, message: 'Required', trigger: 'blur' },
          { max: 50, message: 'You can enter up to 50 characters' },
        ]"
      >
        <el-input
          v-model="info.group.code"
          :disabled="info.readonly || !!info.group.id"
        />
      </el-form-item>
      <el-form-item label="Sort Order">
        <el-input-number
          v-model="info.group.sn"
          clearable
          controls-position="right"
          :disabled="info.readonly"
          :max="100"
          :min="1"
          size="default"
        />
        <el-tooltip
          class="box-item"
          content="The default value is 1. The larger the number, the higher the ranking of organizations at the same level."
          effect="light"
          placement="right-start"
        >
          <el-icon color="#409EFF" size="20" style="margin-left: 12px">
            <WarningFilled />
          </el-icon>
        </el-tooltip>
      </el-form-item>
      <el-form-item label="Organization type">
        <abSelectTree
          v-model="info.group.type"
          desc="Organization type"
          :disabled="info.readonly"
          :has-root="false"
          :multiple="false"
          type-code="groupGrade"
        />
        <el-tooltip
          class="box-item"
          content="When the organization is divided into different types, it is very convenient to directly find the corresponding type of superior position personnel, such as obtaining the person in charge of the branch"
          effect="light"
          placement="right-start"
        >
          <el-icon color="#409EFF" size="20" style="margin-left: 12px">
            <WarningFilled />
          </el-icon>
        </el-tooltip>
      </el-form-item>
      <el-form-item label="Organizational position">
        <el-tag
          v-for="(relation, index) in info.group.orgRelationList"
          v-show="relation.type === 'groupRole'"
          :key="index"
          :closable="!info.readonly"
          :index="index"
          @close="removeRelation(index)"
        >
          {{ relation.roleName }}
        </el-tag>
        <ab-cust-dialog
          v-if="!info.readonly"
          dialog-key="roleSelector"
          :param="{}"
          size="small"
          style="margin-left: 3px"
          @ok="pushOrgRelationList"
        >
          Select
        </ab-cust-dialog>
        <el-tooltip class="box-item" effect="light" placement="right-start">
          <template #content>
            Define the positions under this organization, position = organization + role
            <br />
            When you need to assign work to a position in a department, the position will be very useful.
            <br />
            For example: the department head handles the leave applications of the department's employees.
          </template>
          <el-icon color="#409EFF" size="20" style="margin-left: 12px">
            <WarningFilled />
          </el-icon>
        </el-tooltip>
      </el-form-item>
      <el-form-item
        label="Description"
        prop="desc"
        :rules="[{ max: 300, message: 'You can enter up to 300 characters' }]"
      >
        <el-input
          v-model="info.group.desc"
          autosize
          clearable
          :disabled="info.readonly"
          type="textarea"
        />
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup lang="ts">
  import {
    ChatDotRound,
    ChatSquare,
    WarningFilled,
  } from '@element-plus/icons-vue'
  import { ref } from 'vue'
  import { FormInstance } from 'element-plus'
  import {
    orgApi,
    sysApi,
    getData,
    postData,
    abUtil,
    abSelectTree,
  } from 'agilebpm'

  const { proxy } = getCurrentInstance() as any
  const name = 'groupDetail'
  const formRef = ref<FormInstance>()
  const props = defineProps({
    orgId: {
      type: String,
      required: true,
    },
    parentOrg: {
      type: Object,
      required: true,
    },
    isEdit: {
      type: Boolean,
      required: false,
    },
    operationType: {
      type: String,
      required: true,
    },
    readonlyTmp: {
      type: Boolean,
      required: false,
    },
  })

  const info: any = reactive({
    group: {},
    readonly: true,
    loading: false,
    isAdd: true,
  })

  const emit = defineEmits([
    'appendChildMsgEvent',
    'reloadTree',
    'editOrg',
    'cancel',
  ])

  const loadOrg = (orgId: string) => {
    info.loading = true
    getData(orgApi.group.getOrgGroupUrl + orgId).then((result) => {
      info.loading = false
      info.group = result.data
    })
  }

  const editOrg = () => {
    info.readonly = false
    emit('editOrg')
  }

  const cancel = () => {
    info.readonly = true
    emit('cancel')
  }

  /*
  Delete the role on the organization
  Check relationship information before deleting
  * The front end uses AbUtil.Arrays.del to delete
  * The backend calls the interface for deleting organizational relationships
  * */
  const removeRelation = (index: number) => {
    postData(orgApi.post.removeCheckGroupUrl, {
      roleId: info.group.orgRelationList[index].roleId,
      groupId: info.group.orgRelationList[index].groupId,
    }).then((result) => {
      if (result.isOk) {
        abUtil.Arrays.del(index, info.group.orgRelationList)
      } else {
        info.$Notice.error({
          title: 'Delete failed',
          desc: result.msg,
          duration: 5,
        })
      }
    })
  }

  const saveBefore = () => {}
  const saveAfter = (data: string) => {
    info.loading = false
    info.readonly = true
    info.group.id = info.group.id ? info.group.id : data
    emit('reloadTree')
    // proxy.$emit('getDataTree')
    // If it is a new addition, continue to add
    emit(
      'appendChildMsgEvent',
      abUtil.clone(info.group),
      proxy.operationType === 'add'
    )
  }
  const pushOrgRelationList = (dataArr: []) => {
    if (!dataArr) return
    if (!info.group.orgRelationList) {
      info.group.orgRelationList = []
    }

    dataArr.forEach((item: any) => {
      // Check if duplicates exist
      let i = 0,
        relation
      for (; (relation = info.group.orgRelationList[i++]); ) {
        if (relation.roleId === item.id) {
          return
        }
      }
      info.group.orgRelationList.push({
        type: 'groupRole',
        roleId: item.id,
        roleName: item.name,
        groupId: info.group.id || 'asdf',
      })
    })
  }

  watch(
    () => props.orgId,
    (orgId) => {
      if (props.operationType === 'add') {
        info.readonly = false
        if (orgId == '0') {
          info.group = {
            parentName: '',
            parentId: '0',
            sn: 1,
          }
        } else {
          info.group = {
            parentName: props.parentOrg.name,
            parentId: props.parentOrg.id,
            sn: 1,
          }
        }
        return
      }
      if (orgId) {
        loadOrg(orgId)
      }
    }
  )

  watch(
    () => props.operationType,
    (operationType, oldValue) => {
      // edit situation
      if (operationType === 'edit') {
        if (oldValue === 'add') {
          loadOrg(proxy.orgId)
        }
        info.readonly = false
      }
      // add situation
      else if (operationType === 'add') {
        info.readonly = false
        info.group = {
          parentName: proxy.parentOrg.name,
          parentId: proxy.parentOrg.id,
          sn: 1,
        }
      }
      // detail situation, that is, when clicking on the tree
      else {
        loadOrg(proxy.orgId)
        info.readonly = true
      }
    }
  )

  watch(
    () => props.readonlyTmp,
    () => {
      info.readonly = !info.readonly
    }
  )
  onMounted(() => {
    if (!info.id) {
      info.id = 1
    }
    if (info.isEdit) {
      info.readonly = false
    }
  })
</script>
<style scoped>
  /deep/.el-input-number .el-input__inner {
    text-align: left;
  }
</style>
