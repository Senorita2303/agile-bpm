<template>
  <div class="common-layout">
    <el-container>
      <el-aside class="left-tree">
        <ab-dict-tree
          v-model="query['a.typeCode$VIN']"
          type-code="biz"
          @node-click="searchResetPage()"
        />
      </el-aside>
      <el-main>
        <div class="comprehensive-table-container">
          <div ref="titleForm">
            <!--Query area-->
            <el-row>
              <el-col class="top-panel" :span="24">
                <el-form ref="queryForm" :inline="true" :model="query">
                  <el-form-item
                    label="Name"
                    label-width="62px"
                    prop="a.name$VLK"
                  >
                    <el-input
                      v-model="query['a.name$VLK']"
                      placeholder="Please enter a name"
                    />
                  </el-form-item>
                  <el-form-item
                    label="Code"
                    label-width="62px"
                    prop="a.code$VLK"
                  >
                    <el-input
                      v-model="query['a.code$VLK']"
                      placeholder="Please enter the code"
                    />
                  </el-form-item>
                  <el-form-item
                    label="Business Object Code"
                    label-width="150px"
                    prop="a.boCode$VLK"
                  >
                    <el-input
                      v-model="query['a.boCode$VLK']"
                      placeholder="Business Object Code"
                    />
                  </el-form-item>

                  <el-form-item>
                    <el-button :icon="Search" type="primary" @click="search()">
                      Search
                    </el-button>
                    <el-button :icon="Refresh" @click="reset()">Reset</el-button>
                  </el-form-item>
                </el-form>
              </el-col>
            </el-row>
            <!--Button area-->
            <el-button
              :icon="Plus"
              type="primary"
              @click="diy"
            >
              Form Design
            </el-button>
            <!-- Import -->
            <span style="margin-left: 5px">
              <span >
                <ab-upload-dialog
                  accept=".form"
                  :after-save="afterImp"
                  :data="uploadData"
                  :search="search"
                  :timeout="2 * 60 * 1000"
                />
              </span>
            </span>
            <el-button
              :disabled="!selectedData || selectedData.length == 0"
              style="margin-left: 5px"
              type="primary"
              @click="exportXml()"
            >
              Export
            </el-button>
            <el-button
              :disabled="!selectedData || selectedData.length == 0"
              :icon="Delete"
              type="danger"
              @click="delBySeletedIds(bizApi.bizForm.BizFormDesignRemove)"
            >
              Batch Delete
            </el-button>
          </div>
          <!--List area-->
          <ab-table
            ref="abTable"
            v-model="selectedData"
            :checkable="true"
            :height="tableHeight"
            highlight-current-row
            is-have-tree
            :query-param="query"
            row-key="id"
            :url="bizApi.bizForm.BizFormDesignListVOJson"
          >
            <ab-column label="Name" min-width="30" prop="name" />
            <ab-column label="Code" min-width="30" prop="code" />
            <ab-column label="Business Object Code" min-width="30" prop="boCode" />
            <ab-column label="Business Object" min-width="30" prop="boName" />
            <ab-column
              ab-text-formatter="byBo-Pro-success-dark|diyBo-Simple-warning-dark"
              label="Mode"
              prop="mode"
              width="100"
            />
            <ab-column
              ab-date-formatter="yyyy-MM-dd HH:mm"
              label="Update Time"
              prop="updateTime"
              width="150"
            />
            <ab-column label="Operator" min-width="20" prop="operator" />
            <ab-column
              ab-template="edit"
              fixed="right"
              label="Action"
              width="270"
            />
            <template #edit="{ scope }">
              <el-button
                text
                type="primary"
                @click="open(scope.row)"
              >
                Design
              </el-button>
              <el-button
                text
                type="primary"
                @click="previewPc(scope.row.pcFormCode)"
                >
                Preview
              </el-button>
              <el-dropdown>
                <el-button :icon="ArrowDown" text type="primary">
                  More
                  <!-- <el-icon class="el-icon--right"><ArrowDown /></el-icon> -->
                </el-button>
                <template #dropdown>
                  <el-dropdown-menu>
                    <span v-if="scope.row.pcFormCode">
                      <el-dropdown-item>
                        <biz-permission
                          :bo-codes="scope.row.boCode"
                          type="form"
                          :value="scope.row.pcFormCode"
                        >
                          Form Permissions
                        </biz-permission>
                      </el-dropdown-item>
                    </span>
                    <span v-if="scope.row.mbFormCode">
                      <!-- <el-dropdown-item
                        divided
                        @click="previewMb(scope.row.mbFormCode)"
                      >
                        Preview mobile version
                      </el-dropdown-item> -->
                    </span>
                    <span >
                      <el-dropdown-item
                        divided
                        @click="exportXml(scope.row.code)"
                      >
                        Export
                      </el-dropdown-item>
                    </span>
                    <span >
                      <el-dropdown-item @click="copy(scope.row)">
                        <el-popover
                          placement="right"
                          title="Message"
                          trigger="hover"
                          :width="300"
                        >
                          <template #reference>Copy</template>
                          <span v-if="scope.row.mode == 'diyBo'">
                            Will be copied along with the business object
                          </span>
                          <span v-else>Share the current form business object</span>
                        </el-popover>
                      </el-dropdown-item>
                    </span>

                    <span >
                      <el-dropdown-item
                        @click="
                          sendAction(
                            bizApi.bizForm.BizFormDesignRemove + scope.row.id,
                            'Confirm to delete' + scope.row.name + ' ?'
                          )
                        "
                      >
                        Delete
                      </el-dropdown-item>
                    </span>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </template>
          </ab-table>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script>
  import { ElMessage } from 'element-plus'
  import {
    abTableMix,
    abDictTree,
    bizApi,
    sysApi,
    abTools,
    abUploadDialog,
  } from 'agilebpm'
  import { Search, Refresh, Plus, ArrowDown } from '@element-plus/icons-vue'
  import BizPermission from '@/views/biz/bizObject/bizPermission.vue'

  export default {
    components: { BizPermission, abDictTree, abUploadDialog },
    mixins: [abTableMix],
    setup() {
      return {
        bizApi,
        abDictTree,
        Search,
        Refresh,
        Plus,
        ArrowDown,
        sysApi,
        abTools,
        abUploadDialog,
      }
    },
    data() {
      return {
        mobileUrl: '',
        uploadData: {
          btnText: 'Import',
          tip: 'Please select.form Files',
          url: bizApi.bizForm.BizFormDesignImportXml,
        },
        proxy: null,
      }
    },
    created() {
      const { proxy } = abTools.useCurrentInstance()
      this.proxy = proxy

      this.getMobileUrl()
    },
    methods: {
      add(data) {
        if (data.length == 0) {
          ElMessage({
            message: 'Please select a business object',
            type: 'warning',
          })
          return
        }
        window.open(
          abTools.getResolveUrl(
            `/formDesigner?boCode=${data[0].code}&typeCode=${
              this.query['a.typeCode$VEQ'] || ''
            }`,
            this.proxy
          )
        )
      },
      diy() {
        window.open(
          abTools.getResolveUrl(
            `/formDesigner?mode=diyBo&typeCode=${
              this.query['a.typeCode$VEQ'] || ''
            }`,
            this.proxy
          )
        )
      },
      open(row) {
        const url = abTools.getResolveUrl(
          `/formDesigner?id=${row.id}&mode=${row.mode}&typeCode=${
            this.query['a.typeCode$VEQ'] || ''
          }`,
          this.proxy
        )

        window.open(url)
      },
      codePc(code) {
        window.open(
          abTools.getResolveUrl(`/bizFormCode?code=${code}`, this.proxy)
        )
      },
      codeMb(code) {
        window.open(
          abTools.getResolveUrl(`/bizFormCode?type=mb&code=${code}`, this.proxy)
        )
      },
      previewPc(code) {
        window.open(
          abTools.getResolveUrl(`/bizForm/preview/${code}?del=1`, this.proxy)
        )
      },
      copy(row) {
        window.open(
          abTools.getResolveUrl(
            `/formDesigner?copyId=${row.id}&mode=${row.mode}&typeCode=${
              this.query['a.typeCode$VEQ'] || ''
            }`,
            this.proxy
          )
        )
      },
      exportXml(code) {
        if (!code) {
          code = this.selectedData.map((d) => d.code).join(',')
        }
        bizApi.bizForm.exportXml(code).then(({ data }) => {
          let name
          if (!code.includes(',')) {
            name = data[code].design.name
          } else {
            name = `${this.selectedData[0].name} and ${this.selectedData.length} forms`
          }
          abTools.downImgFile(`${name}.form`, JSON.stringify(data))
        })
      },
      async getMobileUrl() {
        const data = await sysApi.property.getByCode('mobileUrl')
        this.mobileUrl = data.data
        console.info(this.mobileUrl)
      },
      afterImp(result) {
        if (!result.isOk) {
          ElMessage({
            message: 'Import failed',
            type: 'error',
          })
          return
        }
        ElMessage({
          message: result.data,
          type: 'success',
          dangerouslyUseHTMLString: true,
        })
      },
    },
  }
</script>
