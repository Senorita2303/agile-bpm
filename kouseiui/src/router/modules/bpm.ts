import Layout from '@/layout/index.vue'
import home from '@/views/home/index.vue'

export default {
  path: '/bpm',
  name: 'BPM',
  component: Layout,
  meta: {
    title: 'Process Management',
    icon: 'home-2-line',
    breadcrumbHidden: true,
  },
  children: [
    {
      path: '/bpm/bpm/definitionList',
      name: 'BpmDefinitionList',
      component: () => import('@/views/bpm/definition/bpmDefinitionList.vue'),
      meta: {
        title: 'Process List',
        icon: 'Menu',
        noKeepAlive: false,
      },
    },
    {
      path: '/bpm/bpm/definitionEdit',
      name: 'BpmDefinitionEdit',
      component: () => import('@/views/bpm/definition/bpmDefinitionEdit.vue'),
      meta: {
        title: 'Process Editing',
      },
    },
    {
      path: '/bpm/bpm/instanceList',
      name: 'BpmInstanceList',
      component: () => import('@/views/bpm/instance/bpmInstanceList.vue'),
      meta: {
        title: 'Instance List',
        icon: 'Menu',
        noKeepAlive: false,
      },
    },
    {
      path: '/bpm/bpm/instanceDetail',
      name: 'BpmInstanceDetail',
      component: () => import('@/views/bpm/instance/bpmInstanceDetail.vue'),
      meta: {
        title: 'Process instance detail',
        dynamicNewTab: true,
        icon: 'Menu',
        noKeepAlive: false,
      },
    },
    {
      path: '/bpm/flowStart/:defKey',
      name: 'FlowStart',
      component: () => import('@/views/bpm/instance/flowStart.vue'),
      meta: {
        title: 'Process Startup',
        icon: 'Menu',
        noKeepAlive: false,
      },
    },
    {
      path: '/bpm/bpm/draftStart',
      name: 'DraftStart',
      component: () => import('@/views/bpm/instance/draftStart.vue'),
      meta: {
        title: 'Draft Startup',
        icon: 'Menu',
        noKeepAlive: false,
      },
    },
    {
      path: '/bpm/bpm/instanceApprovalHistory',
      name: 'BpmInstanceApprovalHistory',
      component: () =>
        import('@/views/bpm/instance/bpmInstanceApprovalHistory.vue'),
      meta: {
        title: 'Process instance detail',
        dynamicNewTab: true,
        icon: 'Menu',
        noKeepAlive: false,
      },
    },
    {
      path: '/bpm/bpm/taskList',
      name: 'BpmTaskList',
      component: () => import('@/views/bpm/task/bpmTaskList.vue'),
      meta: {
        title: 'Process Task List',
        icon: 'Menu',
      },
    },
    {
      path: '/bpm/bpm/taskComplate',
      name: 'BpmTaskComplate',
      component: () => import('@/views/bpm/task/bpmTaskComplate.vue'),
      meta: {
        title: 'Process task processing',
        icon: 'Menu',
        noKeepAlive: true,
      },
    },
    {
      path: '/bpm/my/todoList',
      name: 'BpmMyTaskTodoList',
      component: () => import('@/views/bpm/myTask/todoList.vue'),
      meta: {
        title: 'My To-Do List',
        icon: 'Menu',
        noKeepAlive: false,
      },
    },
    {
      path: '/bpm/my/approveList',
      name: 'BpmMyApproveList',
      component: () => import('@/views/bpm/myTask/approveList.vue'),
      meta: {
        title: 'My Approval History',
        icon: 'Menu',
        noKeepAlive: false,
      },
    },
    {
      path: '/bpm/my/definitionList',
      name: 'BpmMyDefinitionList',
      component: () => import('@/views/bpm/myTask/definitionList.vue'),
      meta: {
        title: 'List of available application processes',
        icon: 'Menu',
        noKeepAlive: false,
      },
    },
    {
      path: '/bpm/my/applyList',
      name: 'BpmMyApplyList',
      component: () => import('@/views/bpm/myTask/applyList.vue'),
      meta: {
        title: 'Application History',
        icon: 'Menu',
        noKeepAlive: false,
      },
    },
    {
      path: '/bpm/my/draftList',
      name: 'BpmMyDraftList',
      component: () => import('@/views/bpm/myTask/draftList.vue'),
      meta: {
        title: 'Draft',
        icon: 'Menu',
        noKeepAlive: false,
      },
    },
    {
      path: '/bpm/my/receiveList',
      name: 'BpmMyReceiveList',
      component: () => import('@/views/bpm/myTask/receiveList.vue'),
      meta: {
        title: 'Copy/Circulate',
        icon: 'Menu',
        noKeepAlive: false,
      },
    },
    {
      path: '/bpm/bpm/definitionVersionList',
      name: 'BpmDefinitionVersionList',
      component: () =>
        import('@/views/bpm/definition/overView/bpmDefinitionVersionList.vue'),
      meta: {
        title: 'Process Version List',
        icon: 'Menu',
        noKeepAlive: true,
      },
    },
    {
      name: "dashboard",
      path: "/dashboard",
      meta: {
        title: "Dashboard",
        icon: 'Menu',
        affix: true,
        type: 'menu',
        parentPath: '/bpm'
      },
      component: home
    },
    {
      path: 'bpm/bpmInstanceListDetail',
      name: 'BpmInstanceListDetail',
      component: () => import('@/views/bpm/instance/bpmInstanceListDetail.vue'),
      meta: {
        title: 'Process instance detail',
        dynamicNewTab: true,
        icon: 'grid-fill',
        noKeepAlive: false,
      },
    },
  ],
}
