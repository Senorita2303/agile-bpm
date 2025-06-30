<template>
  <div class="common-layout">
    <el-container>
      <el-aside class="left-tree">
        <ab-dict-tree
          v-model="query.typeCode$VIN"
          type-code="property"
          @node-click="searchResetPage()"
        />
      </el-aside>
      <el-main>
        <div class="comprehensive-table-container">
          <div ref="titleForm">
            <el-row class="vab-query-form">
              <el-col class="top-panel">
                <el-form
                  ref="queryForm"
                  :inline="true"
                  :model="query"
                  @submit.prevent
                >
                  <el-form-item
                    label="Parameter name"
                    label-width="120px"
                    prop="name$VLK"
                  >
                    <el-input
                      v-model="query.name$VLK"
                      placeholder="Please enter the parameter name"
                    />
                  </el-form-item>
                  <el-form-item label="Code" label-width="62px" prop="code$VLK">
                    <el-input
                      v-model="query.code$VLK"
                      placeholder="Please enter the code"
                    />
                  </el-form-item>
                  <el-form-item
                    v-if="collapse"
                    label="Environment"
                    label-width="70px"
                    prop="environment$VLK"
                  >
                    <ab-select-tree
                      v-model="query.environment$VLK"
                      :has-root="false"
                      :operation="false"
                      style="width: 160px"
                      type-code="environment"
                    />
                  </el-form-item>
                  <el-form-item>
                    <el-button :icon="Search" type="primary" @click="search()">
                      Search
                    </el-button>
                    <el-button :icon="RefreshRight" @click="reset()">
                      Reset
                    </el-button>
                    <el-button type="text" @click="handleCollapse()">
                      <span v-if="!collapse" type="text">Show Details</span>
                      <span v-else type="text">Collapse</span>
                      <el-icon class="el-icon--right">
                        <ArrowUp v-if="collapse" />
                        <ArrowDown v-else />
                      </el-icon>
                    </el-button>
                  </el-form-item>
                </el-form>
              </el-col>
              <el-col class="left-panel">
                <el-space>
                  <router-link
                    :to="{
                      name: 'PropertyEdit',
                      query: { typeCode: query.typeCode$VEQ },
                    }"
                  >
                    <el-button :icon="Plus" type="primary">Add</el-button>
                  </router-link>
                  <el-button
                    :disabled="!selectedData || selectedData.length == 0"
                    :icon="Delete"
                    type="danger"
                    @click="delBySeletedIds(sysApi.property.propertyRemove)"
                  >
                    Batch Delete
                  </el-button>
                </el-space>
              </el-col>
            </el-row>
          </div>
          <!-- Key fields are set to minwith, and specific enumeration fields are set to with (management columns, dates, status, dictionaries, etc.).
         Put unimportant fields at the end and manage column settings fixed="right"
    -->
          <ab-table
            ref="abTable"
            v-model="selectedData"
            :height="tableHeight"
            :query-param="query"
            row-key="id"
            :url="sysApi.property.propertyList"
          >
            <ab-column label="Parameter name" min-width="80" prop="name" />
            <ab-column label="Code" min-width="80" prop="code" />
            <ab-column label="Description" min-width="100" prop="desc" />
            <ab-column
              ab-date-formatter="yyyy-MM-dd HH:mm"
              label="Update Time"
              prop="updateTime"
              width="160"
            />
            <ab-column label="Updater" width="120" prop="updater" />
            <ab-column
              ab-template="environment"
              label="Environment"
              prop="environmentName"
              width="120"
            />
            <template #environment="{ scope }">
              <el-tag class="mx-1" :type="envCss(scope.row.environment)">
                {{ scope.row.environmentName }}
              </el-tag>
            </template>
            <ab-column
              ab-text-formatter="1-Yes-success-dark|0-No-danger-dark"
              label="Encrypt"
              prop="encrypt"
              width="75"
            />
            <ab-column
              ab-template="edit"
              fixed="right"
              label="Action"
              width="140"
            />
            <template #edit="{ scope }">
              <router-link
                :to="{ name: 'PropertyEdit', query: { id: scope.row.id } }"
              >
                <el-button text type="primary">Edit</el-button>
              </router-link>
              <el-button
                text
                type="primary"
                @click="
                  sendAction(
                    sysApi.property.propertyRemove + scope.row.id,
                    `Confirm delete ${scope.row.name}?`
                  )
                "
              >
                Delete
              </el-button>
            </template>
          </ab-table>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script lang="ts">
  import { reactive, defineComponent, getCurrentInstance } from 'vue'
  import { abTableMix, abDictTree, sysApi } from 'agilebpm'
  import {
    Delete,
    Edit,
    Search,
    Plus,
    RefreshRight,
    InfoFilled,
    ArrowUp,
    ArrowDown,
  } from '@element-plus/icons-vue'

  export default defineComponent({
    name: 'PropertyList',
    components: { ArrowUp, ArrowDown, abDictTree },
    mixins: [abTableMix],
    setup() {
      // Query condition definition, if ts needs to define all parameters, here override the parent class, if not, you don't need to set it, the parent class has already defined the query object
      const query: any = reactive({
        name$VLK: '',
        code$VLK: '',
        environment$VLK: '',
        typeCode$VIN: '',
      })

      const envCss = (env: string) => {
        if (env === 'DEV') return 'primary'
        if (env === 'SIT') return 'success'
        if (env === 'GRAY') return 'warning'
        if (env === ' PROD') return 'danger'
        return 'info'
      }

      return {
        query,
        sysApi,
        abDictTree,
        Delete,
        Edit,
        Search,
        Plus,
        RefreshRight,
        InfoFilled,
        ArrowUp,
        ArrowDown,
        envCss,
      }
    },
  })
</script>
