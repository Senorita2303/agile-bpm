import Layout from '@/layout/index.vue'
export default {
  path: '/sys',
  name: 'System',
  component: Layout,
  meta: {
    title: 'System Management',
    icon: 'home-2-line',
    breadcrumbHidden: true,
  },
  children: [
    {
      path: '/sys/dataDic/datadicList',
      name: 'DatadicList',
      component: () => import('@/views/sys/dataDic/datadicList.vue'),
      meta: {
        title: 'Data dictionary',
        icon: 'dashboard-line',
      },
    },
    {
      path: '/sys/sysResourceList',
      name: 'SysResource',
      component: () => import('@/views/sys/sysResourceList/index.vue'),
      meta: {
        title: 'Resource Management',
        icon: 'dashboard-line',
      },
    },
    {
      path: '/sys/dailyPhrases/dailyPhrasesList',
      name: 'DailyPhrasesList',
      component: () => import('@/views/sys/dailyPhrases/dailyPhrasesList.vue'),
      meta: {
        title: 'Daily phrase management',
        icon: 'dashboard-line',
      },
    },
    {
      path: '/sys/dailyPhrases/dailyPhrasesEdit',
      name: 'DailyPhrasesEdit',
      component: () => import('@/views/sys/dailyPhrases/dailyPhrasesEdit.vue'),
      meta: {
        title: 'Edit Daily Phrases',
        icon: 'dashboard-line',
      },
    },

    /*  {
       path: '/sys/dataSource/dataSourceList',
       name: 'DataSourceList',
       component: () => import('@/views/sys/dataSource/dataSourceList.vue'),
       meta: {
         title: 'Data source list',
         icon: 'dashboard-line',
       },
     },
     {
       path: '/sys/dataSource/dataSourceEdit',
       name: 'DataSourceEdit',
       component: () => import('@/views/sys/dataSource/dataSourceEdit.vue'),
       meta: {
         title: 'Data source editing',
         icon: 'dashboard-line',
       },
     },
     {
       path: '/sys/dataSource/dataSourceDefList',
       name: 'DataSourceDefList',
       component: () => import('@/views/sys/dataSource/dataSourceDefList.vue'),
       meta: {
         title: 'Data source template list',
         icon: 'dashboard-line',
       },
     },
     {
       path: '/sys/dataSource/dataSourceDefEdit',
       name: 'DataSourceDefEdit',
       component: () => import('@/views/sys/dataSource/dataSourceDefEdit.vue'),
       meta: {
         title: 'Data source template editing',
         icon: 'dashboard-line',
       },
     }, */
    {
      path: '/sys/auditLog/logErrorList',
      name: 'LogErrorList',
      component: () => import('@/views/sys/auditLog/logErrorList.vue'),
      meta: {
        title: 'Log Error List',
        icon: 'dashboard-line',
      },
    },
    {
      path: '/sys/auditLog/logErrorEdit',
      name: 'LogErrorEdit',
      component: () => import('@/views/sys/auditLog/logErrorEdit.vue'),
      meta: {
        title: 'Log Error Editing',
        icon: 'dashboard-line',
      },
    },
    {
      path: '/sys/properties/propertyList',
      name: 'PropertyList',
      component: () => import('@/views/sys/properties/propertyList.vue'),
      meta: {
        title: 'System Properties List',
        icon: 'dashboard-line',
      },
    },
    {
      path: '/sys/properties/propertyEdit',
      name: 'PropertyEdit',
      component: () => import('@/views/sys/properties/propertyEdit.vue'),
      meta: {
        title: 'System Properties Editing',
        icon: 'dashboard-line',
      },
    },
    {
      path: '/sys/scripts/scriptList',
      name: 'ScriptList',
      component: () => import('@/views/sys/scripts/scriptList.vue'),
      meta: {
        title: 'List of commonly used scripts',
        icon: 'dashboard-line',
      },
    },
    {
      path: 'scripts/scriptEdit',
      name: 'ScriptEdit',
      component: () => import('@/views/sys/scripts/scriptEdit.vue'),
      meta: {
        title: 'Common script editing',
        icon: 'dashboard-line',
      },
    }
  ],
}
