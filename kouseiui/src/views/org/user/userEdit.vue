<template>
  <el-container class="layout-container-demo">
    <el-header>
      <ab-save
        back-name="UserList"
        :form-ref="formRef"
        :save-data="info.data"
        :url="orgApi.user.OrgUserSaveUrl"
      />
      <ab-load
        v-model="info.data"
        :get-query="id"
        :url="orgApi.user.OrgUserGetUrl"
      />
    </el-header>

    <el-divider class="dividermar" />

    <el-main>
      <el-form
        ref="formRef"
        label-suffix="："
        label-width="120px"
        :model="info.data"
        :rules="rules"
        :status-icon="false"
      >
        <el-form-item label="Full Name" prop="fullname">
          <ab-pinyin
            v-model="info.data.fullname"
            v-model:to="info.data.account"
          />
        </el-form-item>

        <el-form-item label="Account" prop="account">
          <el-input
            v-model="info.data.account"
            :disabled="Boolean(info.data.id)"
          />
        </el-form-item>

        <el-form-item
          v-if="!info.data.id"
          label="Password"
          prop="password"
          show-password
        >
          <el-input v-model="info.data.password" password type="password" />
        </el-form-item>

        <el-form-item label="Email" prop="email">
          <el-input v-model="info.data.email" />
        </el-form-item>
        <el-form-item label="Mobile" prop="mobile">
          <el-input v-model="info.data.mobile" />
        </el-form-item>
        <el-form-item label="Weixin" prop="weixin">
          <el-input v-model="info.data.weixin" />
        </el-form-item>
        <el-form-item label="Gender" prop="sex">
          <el-radio-group v-model="info.data.sex" class="ml-4">
            <el-radio label="Male" size="large">Male</el-radio>
            <el-radio label="Female" size="large">Female</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="Status" prop="status">
          <el-switch
            v-model="info.data.status"
            active-text="Enable"
            :active-value="1"
            inactive-text="Disable"
            :inactive-value="0"
            inline-prompt
            size="large"
          />
        </el-form-item>
        <el-form-item label="Role" prop="orgRelationList">
          <el-tag
            v-for="(relation, index) in info.data.orgRelationList"
            v-show="relation.type === 'userRole'"
            :key="index"
            :closable="true"
            @close="deleteRel(index)"
          >
            {{ relation.roleName }}
          </el-tag>

          <el-tag
            v-for="(relation, index) in info.data.orgRelationList"
            v-show="relation.type === 'groupUserRole' && relation.roleName"
            :key="index"
            :index="index"
          >
            {{ relation.roleName }}
          </el-tag>

          <ab-cust-dialog
            v-model="info.orgRoleList"
            dialog-key="roleSelector"
            :param="{}"
            size="small"
            style="margin-left: 3px"
            :value-mapping="{ id: 'id', name: 'name' }"
          >
            Select
          </ab-cust-dialog>
        </el-form-item>
        <el-form-item label="Organization" prop="orgRelationList">
          <el-tag
            v-for="(relation, index) in info.data.orgRelationList"
            v-show="relation.type === 'groupUser'"
            :key="index"
            :closable="true"
            :index="index"
            @close="deleteRel(index)"
          >
            {{ relation.groupName }}
          </el-tag>

          <el-tag
            v-for="(relation, index) in info.data.orgRelationList"
            v-show="relation.type === 'groupUserRole' && relation.groupName"
            :key="index"
            :index="index"
          >
            {{ relation.groupName }}
          </el-tag>

          <ab-cust-dialog
            v-model="info.orgGroupList"
            dialog-key="orgSelector"
            size="small"
            style="margin-left: 3px"
            :value-mapping="{ id: 'id', name: 'name' }"
          >
            Select
          </ab-cust-dialog>
        </el-form-item>
        <el-form-item label="Position" prop="orgRelationList">
          <el-tag
            v-for="(relation, index) in info.data.orgRelationList"
            v-show="relation.type === 'groupUserRole'"
            :key="index"
            :closable="true"
            :index="index"
            @close="deleteRel(index)"
          >
            {{ relation.postName }}
          </el-tag>
          <ab-cust-dialog
            v-model="info.orgRelList"
            dialog-key="postSelector"
            size="small"
            style="margin-left: 3px"
            :value-mapping="{
              groupId: 'groupId',
              roleId: 'roleId',
              name: 'postName',
              groupName: 'groupName',
              roleName: 'roleName',
            }"
          >
            Select
          </ab-cust-dialog>
        </el-form-item>
        <el-form-item label="Address" prop="address">
          <el-input
            v-model="info.data.address"
            :autosize="{ minRows: 2, maxRows: 4 }"
            placeholder="Please enter your address"
            type="textarea"
          />
        </el-form-item>
      </el-form>
    </el-main>
  </el-container>
</template>

<script lang="ts" setup>
  import { reactive, ref } from 'vue'
  import { orgApi, abTools, abUtil } from 'agilebpm'
  import type { FormRules } from 'element-plus'

  // Import current instance
  const { proxy } = abTools.useCurrentInstance()
  const formRef = ref()
  const id = proxy.$route.params.id
  const info: any = reactive({
    data: {
      id: '',
      fullname: '',
      account: '',
      password: '',
      email: '',
      weixin: '',
      mobile: '',
      sex: 'Male',
      status: 1,
      orgRelationList: [],
      address: '',
    },
    orgRoleList: [],
    orgGroupList: [],
    orgRelList: [],
    patt: '^(([^<>()[\\]\\\\.,;:\\s@\\"]+(\\.[^<>()[\\]\\\\.,;:\\s@\\"]+)*)|(\\".+\\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$',
  })

  const checkRegExp = (rule: any, value: string, callback: any) => {
    const myRegex = new RegExp(info.patt)
    if (value) {
      if (!myRegex.test(value)) {
        callback(new Error('Please enter a valid email address'))
      }
    }
    callback()
  }

  const rules = reactive<FormRules>({
    fullname: [
      { required: true, message: 'Required', trigger: 'blur' },
      { max: 50, message: 'You can enter up to 50 characters' },
    ],
    account: [
      { required: true, message: 'Required', trigger: 'blur' },
      { max: 50, message: 'You can enter up to 50 characters' },
    ],
    password: [
      { required: true, message: 'Required', trigger: 'blur' },
      { max: 50, message: 'You can enter up to 50 characters' },
    ],
    email: [{ validator: checkRegExp, trigger: 'change' }],
  })

  /**
   * Add job information to the role
   *
   * @param dataArr  Job information
   * @param type   Type Organization Role Position
   */

  const pushRelationList = (dataArr: any[], type: string, infoArr: any[]) => {
    if (dataArr.length === 0) return
    dataArr.forEach((item: any) => {
      if (infoArr.length > 0) {
        // Check if duplicates exist
        for (let i = 0, relation; (relation = infoArr[i++]); ) {
          if (type === 'userRole') {
            if (relation.roleId === item.id) {
              return
            }
          }
          if (type === 'groupUser') {
            if (relation.groupId === item.id) {
              return
            }
          }
          if (type === 'groupUserRole') {
            if (
              relation.roleId === item.roleId &&
              relation.groupId === item.groupId
            ) {
              return
            }
          }
        }
      }
      if (infoArr.length === 0) {
        infoArr.splice(0)
      }

      if (type === 'userRole') {
        infoArr.push({
          type: type,
          roleId: item.id,
          roleName: item.name,
        })
      }
      if (type === 'groupUser') {
        infoArr.push({
          type: type,
          groupId: item.id,
          groupName: item.name,
        })
      }
      if (type === 'groupUserRole') {
        infoArr.push({
          type: type,
          groupId: item.groupId,
          roleId: item.roleId,
          postName: item.postName,
          groupName: item.groupName,
          roleName: item.roleName,
        })
      }
    })
  }

  const deleteRel = (index: number) =>
    abUtil.Arrays.del(index, info.data.orgRelationList)

  watch(
    () => info.orgRoleList,
    (val: any) => {
      if (val) {
        pushRelationList(val, 'userRole', info.data.orgRelationList)
      }
      info.orgRoleList.splice(0)
    }
  )

  watch(
    () => info.orgGroupList,
    (val: any) => {
      if (val) {
        pushRelationList(val, 'groupUser', info.data.orgRelationList)
      }
      info.orgGroupList.splice(0)
    }
  )

  watch(
    () => info.orgRelList,
    (val: any) => {
      if (val) {
        pushRelationList(val, 'groupUserRole', info.data.orgRelationList)
      }
      info.orgRelList.splice(0)
    }
  )
</script>

