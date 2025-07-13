<template>
  <div class="common-layout">
    <el-container>
      <el-aside class="left-tree">
        <ab-dict-tree
          v-model="query.typeCode$VIN"
          type-code="script"
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
                  label-width="62px"
                  :model="query"
                  @submit.prevent
                >
                  <el-form-item label="Name" prop="name$VLK">
                    <el-input
                      v-model="query.name$VLK"
                      placeholder="Please enter a name"
                    />
                  </el-form-item>
                  <el-form-item label="Type" prop="typeCode$VEQ">
                    <ab-select-tree
                      v-model="query.typeCode$VEQ"
                      :operation="false"
                      type-code="script"
                    />
                  </el-form-item>
                  <el-form-item>
                    <el-button :icon="Search" type="primary" @click="search()">
                      Search
                    </el-button>
                    <el-button :icon="RefreshRight" @click="reset()">
                      Reset
                    </el-button>
                  </el-form-item>
                </el-form>
              </el-col>
              <el-col class="left-panel">
                <el-space>
                  <router-link
                    :to="{
                      name: 'ScriptEdit',
                      query: { typeCode: query.typeCode$VEQ },
                    }"
                  >
                    <el-button icon="Plus" type="primary">Add</el-button>
                  </router-link>
                  <el-button
                    v-ab-btn-rights:script_del
                    :disabled="!selectedData || selectedData.length == 0"
                    :icon="Delete"
                    type="danger"
                    @click="delBySeletedIds(sysApi.script.scriptRemove)"
                  >
                    Batch Delete
                  </el-button>
                </el-space>
              </el-col>
            </el-row>
          </div>

          <ab-table
            ref="abTable"
            v-model="selectedData"
            :height="tableHeight"
            :query-param="query"
            row-key="id"
            :url="sysApi.script.scriptList"
          >
            <ab-column label="Name" min-width="80" prop="name" />
            <ab-column label="Note" min-width="280" prop="desc" />
            <ab-column
              ab-date-formatter="yyyy-MM-dd HH:mm"
              label="Update Time"
              width="160"
              prop="updateTime"
            />
            <ab-column label="Updater" width="160" prop="updater" />
            <ab-column
              ab-template="edit"
              fixed="right"
              label="Action"
              prop="key"
              width="140"
            />
            <template #edit="{ scope }">
              <router-link
                :to="{ name: 'ScriptEdit', query: { id: scope.row.id } }"
              >
                <el-button text type="primary">Edit</el-button>
              </router-link>
              <el-button
                text
                type="primary"
                @click="
                  sendAction(
                    sysApi.script.scriptRemove + scope.row.id,
                    `Confirm delete ${scope.row.name} ?`
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
  import { reactive, defineComponent } from 'vue'
  import { abTableMix, abDictTree, sysApi } from 'agilebpm'
  import {
    Search,
    Plus,
    RefreshRight,
    InfoFilled,
    Delete,
  } from '@element-plus/icons-vue'

  export default defineComponent({
    name: 'ScriptList',
    components: { abDictTree },
    mixins: [abTableMix],
    setup() {
      // Query condition definition, if ts needs to define all parameters, here override the parent class, if not, you don't need to set it, the parent class has already defined the query object
      const query: any = reactive({
        name$VLK: '',
        typeCode$VEQ: '',
      })
      return {
        abDictTree,
        query,
        sysApi,
        Search,
        Plus,
        RefreshRight,
        InfoFilled,
        Delete,
      }
    },
  })
</script>
