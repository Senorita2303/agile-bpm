<template>
  <div class="resourceTree">
    <p class="selectvalue">{{state.selectvalue}}</p>
    <el-card
      class="box-card treeBoxHeight"
      shadow="hover"
      style="margin: 0 12px"
      :style="{ height: state.height }"
    >
      <el-tree
        :allow-drop="allowDrop"
        :current-node-key="state.nodekey"
        :data="state.data"
        :default-expanded-keys="state.defaultExpanded"
        draggable
        empty-text="Loading..."
        :expand-on-click-node="false"
        :highlight-current="true"
        node-key="id"
        @node-click="handleNodeClick"
        @node-drop="nodeDrop"
      >
        <template #default="{ data }">
          <div class="custom-tree-node">
            <el-popover placement="right-end" trigger="hover" :width="100">
              <template #reference>
                <p>
                  <vab-icon :icon="data.icon" />
                  {{ data.name }}
                </p>
              </template>
              <span>
                <a style="cursor: pointer" @click.stop="append(data, true)">
                  Add
                </a>
                <a
                  style="margin-left: 10px; cursor: pointer"
                  @click.stop="removeFn(data)"
                >
                  Delete
                </a>
              </span>
            </el-popover>
          </div>
        </template>
      </el-tree>
    </el-card>
  </div>
</template>

<script setup>
  import { onMounted, reactive, toRefs } from 'vue'
  import { ElMessage, ElMessageBox } from 'element-plus'
  import {
    sysApi,
    abTools,
    abUtil,
    postData,
    registerApi,
    authApi,
  } from 'agilebpm'

  const emit = defineEmits([
    'updateFetchdata',
    'updateLoading',
    'addFn',
    'resetFields',
    'enableAppFn',
    'updateApiButtonData',
  ])

  const state = reactive({
    defaultExpanded: [],
    nodekey: '',
    height: `${window.innerHeight - (110 + 32 + 20 + 55 + 50)}px`,
    selectvalue: null,
    selectoption: [],
    data: [],
    datato: {
      systemId: null,
    },
    isshow: false,
    id: null,
    enableApp: 0,
    allTreeData: [],
  })

  // Select process platform / backend
  const changeselectvalue = (value) => {
    state.datato.systemId = value
    state.data = []
    fetchTreeData()
    state.selectoption.forEach((item, index) => {
      if (item.id === value) {
        state.enableApp = item.enableApp
      }
    })
    emit('enableAppFn', state.enableApp)
  }

  // Add child/Edit opens the window
  const append = (row) => {
    emit('addFn', row)
  }
  // Click to delete
  const removeFn = (row) => {
    ElMessageBox.confirm(`Confirm to delete【${row.name}】is this the menu?`, 'Message', {
      confirmButtonText: 'Confirm',
      cancelButtonText: 'Cancel',
      type: 'warning',
    }).then(() => {
      authApi.removeResource({ id: row.id }).then(({ msg }) => {
        ElMessage({
          type: 'success',
          message: 'Deleted',
          onClose: () => {},
        })
        fetchTreeData()
        emit('resetFields')
        registerApi.getUserInfo().then((dataInfo) => {
           
        })
        // removereset(row.parentId)
      })
    })
  }

  // Request details
  const fetchData = async (id) => {
    emit('updateLoading', true)
    const data = await authApi.getJson({ id: id })
    emit('updateLoading', false)
    emit('updateFetchdata', data.data)
    const apiButtonData = getTreeName(state.allTreeData, id)
    let arr = []
    if (apiButtonData.children && apiButtonData.children.length > 0) {
      arr = apiButtonData.children.filter((item) => {
        return item.type !== 'menu'
      })
    }
    emit('updateApiButtonData', arr)
  }

  // Click on the single tree properties menu
  const handleNodeClick = ({ id }) => {
    if (id !== '0') {
      state.id = id
      fetchData(id)
    } else {
      return
    }
  }

  // Get node data from tree structure data according to tree structure node id
  const getTreeName = (treeList, id) => {
    for (let i = 0; i < treeList.length; i++) {
      const treeItem = treeList[i]
      if (treeItem.id === id) {
        return treeItem
      } else {
        if (treeItem.children && treeItem.children.length > 0) {
          const res = getTreeName(treeItem.children, id)
          if (res) {
            return res
          }
        }
      }
    }
  }

  const getapplicationListJson = () => {
    postData(sysApi.authApplication.applicationListJson, {
      limit: 99,
    }).then(
      (res) => {
        state.selectoption = res.data.rows
        if (state.selectoption && state.selectoption.length > 0) {
          state.selectoption.forEach((item, index) => {
            // isDefault default enabled enabled
            if (item.isDefault === 1 && item.enabled === 1) {
              state.selectvalue = item.name
              state.datato.systemId = item.id
              state.enableApp = item.enableApp
              emit('enableAppFn', state.enableApp)
            }
          })
          fetchTreeData()
        } else {
          ElMessage({
            type: 'error',
            message: 'The application list is empty and the resource tree cannot be queried',
            onClose: () => {},
          })
        }
      },
      () => {}
    )
  }

  // Define a recursive method that receives an array
  const deepCopy = (params) => {
    // If it is not an object, exit (can stop recursion)
    if (typeof params !== 'object') return
    // Deep copy initial value: object/array
    const newObj = params instanceof Array ? [] : {}
    // Use for-in to loop over object properties (including properties on the prototype chain)
    for (const i in params) {
      // Only access the object's own properties
      if (params.hasOwnProperty(i)) {
        // When the current property does not yet exist in the new object
        if (!(i in newObj)) {
          if (params[i] instanceof Date) {
            // Determine the date type
            newObj[i] = new Date(params[i].getTime())
          } else if (params[i] instanceof RegExp) {
            // Determine the regular type
            newObj[i] = new RegExp(params[i])
          } else if (
            typeof params[i] === 'object' &&
            params[i].nodeType === 1
          ) {
            // Determine DOM element node
            const domEle = document.getElementsByTagName(params[i].nodeName)[0]
            newObj[i] = domEle.cloneNode(true)
          } else {
            // Recursively copy when the element is of type object (excluding Date, RegExp, DOM)
            newObj[i] =
              typeof params[i] === 'object' ? deepCopy(params[i]) : params[i]
          }
        }
      }
    }
    return newObj
  }

  // Recursively filter to get data where hidden is false for each item
  function filterData3(arr) {
    return arr
      .filter((item) => {
        return !item.type || item.type == 'menu'
      })
      .map((i) => {
        i = deepCopy(i)
        if (i.children) {
          i.children = filterData3(i.children)
        }
        return i
      })
  }

  // Request tree menu list
  const fetchTreeData = async () => {
    const data = await authApi.getTreeData(state.datato)

    state.data = filterData3(data.data)
    state.allTreeData = data.data
    // Expand two levels by default
    state.defaultExpanded = abUtil.expandTwoLevels(state.data, 'id')
  }

  const fetchTreeDataAndData = (id) => {
    authApi.getTreeData(state.datato).then(
      (data) => {
        state.data = filterData3(data.data)
        state.allTreeData = data.data
        // Expand two levels by default
        state.defaultExpanded = abUtil.expandTwoLevels(state.data, 'id')
        fetchData(id)
      },
      () => {}
    )
  }
  // Selected by default after deletion
  const removereset = (id) => {
    fetchData(id)
  }

  /**
   * Find all parent node IDs based on the tree child node ID
   * @param {array} dataSource Tree structure data source
   * @param {number} nodeId Subnode ID
   * @returns {array} An array containing all parent node IDs, sorted from the root node to the immediate parent node
   */
  function findParentIds(dataSource, nodeId) {
    const parentIds = [] // An array used to store all parent node IDs

    // Define a recursive function to traverse the entire tree and find all parent nodes of a child node
    function traverse(node, nodeId) {
      if (node.id === nodeId) {
        // If the ID of the current node is equal to the ID of the child node, it means that the child node has been found and you can start looking for the parent node upwards
        return true // Return true if the child node has been found
      }

      if (node.children) {
        // If the current node has child nodes, continue to traverse the child nodes
        for (const childNode of node.children) {
          if (traverse(childNode, nodeId)) {
            // If the parent node of the child node is found in the child node, the ID of the current node is added to the parent node ID array, and true is returned to indicate that the child node has been found
            parentIds.push(node)
            return true
          }
        }
      }

      return false // If the current node is not the parent of the child node, it returns false
    }

    // Traverse the entire tree from the root node and call a recursive function to find all parent nodes of child nodes
    for (const node of dataSource) {
      if (traverse(node, nodeId)) {
        // If the parent node of the child node is found in the subtree of the current node, the loop is exited directly. 
        break
      }
    }

    return parentIds // Returns an array of all parent node IDs
  }

  // Drag the tree
  const nodeDrop = (draggingNode, dropNode, dropType, ev) => {
    let obj = {}
    if (dropNode.data.parentId !== '0') {
      // Pull in child nodes
      if (dropType === 'inner') {
        draggingNode.data.parentId = dropNode.data.id
      }
      obj = findParentIds(state.data, dropNode.data.id)[0]
    } else {
      obj = state.data[0]
    }
    ElMessageBox.confirm(
      `Confirm the need to【${draggingNode.data.name}】move to【${
        dropNode.data.name
      }】when ${
        dropType == 'after'
          ? 'The next node?'
          : dropType == 'before'
          ? 'Previous node?'
          : dropType == 'inner'
          ? 'In the child node?'
          : ''
      }`,
      'Message',
      {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        type: 'warning',
      }
    )
      .then(() => {
        authApi.saveTreeUrl(obj).then(
          (res) => {
            ElNotification({
              title: 'Message',
              message: 'Changed position successfully',
              type: 'success',
            })
            fetchTreeData()
          },
          () => {
            fetchTreeData()
          }
        )
        // postData(
        //   // dropNode.data.parentId !== '0'
        //   //   ? authApi.saveUrl
        //   //   : authApi.saveTreeUrl,
        //   authApi.saveTreeUrl,
        //   obj
        // ).then(
        //   (res) => {
        //     ElNotification({
        //       title: '提示',
        //       message: 'Changed position successfully',
        //       type: 'success',
        //     })
        //     fetchTreeData()
        //   },
        //   () => {
        //     fetchTreeData()
        //   }
        // )
      })
      .catch(() => {
        fetchTreeData()
      })
  }

  const allowDrop = (draggingNode, dropNode, type) => {
    return true
  }
  // tree menu list initialization
  onMounted(() => {
    getapplicationListJson()
  })
  defineExpose({
    ...toRefs(state),
    handleNodeClick,
    fetchTreeData,
    removereset,
    fetchData,
    fetchTreeDataAndData,
  })
</script>

<style lang="scss">
  .selectstyle {
    width: 100%;
    margin: 20px 0;
    margin-bottom: 10px;
  }
  .custom-tree-node {
    display: flex;
    flex: 1;
    align-items: center;
    justify-content: space-between;
    padding-right: 8px;
    font-size: 14px;
  }
  .treeBoxHeight {
    overflow: auto;
    .el-card__body {
      padding: 8px !important;
    }
  }
  .resourceTree{
    .selectvalue{
      padding:16px;
      font-size:15px;
    }
    .box-card{
      width:auto;
    }
  }
</style>
