import Layout from '@/layout/index.vue'
export default {
  path: '/biz',
  name: 'BIZ',
  component: Layout,
  meta: {
    title: 'Form Management',
    icon: 'home-2-line',
    breadcrumbHidden: true,
  },
  children: [
    {
      path: 'custom/customDialogList',
      name: 'CustomDialogList',
      component: () => import('@/views/biz/custom/customDialogList.vue'),
      meta: {
        title: 'Customize Dialog Box',
        icon: 'dashboard-line',
        keepAlive: true,
      },
    },
    {
      path: 'custom/customDemo',
      name: 'CustomDemo',
      component: () => import('@/views/biz/custom/customDemo.vue'),
      meta: {
        title: 'Dialog box demo',
        icon: 'dashboard-line',
        keepAlive: true,
      },
    },
    {
      path: 'custom/customDialogEdit',
      name: 'CustomDialogEdit',
      component: () => import('@/views/biz/custom/customDialogEdit.vue'),
      meta: {
        title: 'Edit Custom Dialog Box',
        icon: 'dashboard-line',
        keepAlive: true,
      },
    },
    {
      path: 'formCustSql/formCustSqlView/:code',
      name: 'FormCustSqlView',
      component: () => import('@/views/biz/formCustSql/formCustSqlView.vue'),
      meta: {
        title: 'Custom List',
        icon: 'dashboard-line',
        dynamicNewTab: true,
        noKeepAlive: false,
      },
    },
    {
      path: 'formCustSql/formCustSqlPreView/:code',
      name: 'FormCustSqlPreView',
      component: () => import('@/views/biz/formCustSql/formCustSqlPreView.vue'),
      meta: {
        title: 'List Preview',
        icon: 'dashboard-line',
        dynamicNewTab: true,
        noKeepAlive: false,
      },
    },
    {
      path: '/biz/bizForm/formViewAdd/:code',
      name: 'FormViewAdd',
      component: () => import('@/views/biz/bizForm/formPreview.vue'),
      meta: {
        title: 'Add a new form',
        icon: 'dashboard-line',
        dynamicNewTab: true,
      },
    },
    {
      path: '/biz/bizForm/formViewEdit/:code',
      name: 'FormViewEdit',
      component: () => import('@/views/biz/bizForm/formPreview.vue'),
      meta: {
        title: 'Form Editing',
        icon: 'dashboard-line',
        dynamicNewTab: true,
      },
    },
    {
      path: '/biz/bizForm/formViewDetail/:code',
      name: 'FormViewDetail',
      component: () => import('@/views/biz/bizForm/formPreview.vue'),
      meta: {
        title: 'Form Detail',
        icon: 'dashboard-line',
        dynamicNewTab: true,
      },
    },
    {
      path: '/biz/bizForm/bizFormDesignList',
      name: 'BizFormDesignList',
      component: () => import('@/views/biz/bizForm/bizFormDesignList.vue'),
      meta: {
        title: 'Business form design list page',
        icon: 'dashboard-line',
      },
    },
    {
      path: 'formCustSql/formCustSqlView/:code',
      name: 'FormCustSqlView',
      component: () => import('@/views/biz/formCustSql/formCustSqlView.vue'),
      meta: {
        title: 'Custom List',
        icon: 'dashboard-line',
        dynamicNewTab: true,
        noKeepAlive: false,
      },
    },
    {
      path: 'bizFormCombinationView/:code',
      name: 'BizFormCombinationView',
      component: () =>
        import('@/views/biz/bizFormCombination/bizFormCombinationView.vue'),
      meta: {
        title: 'Combination Form',
        dynamicNewTab: true,
        icon: 'dashboard-line',
      },
    },
  ],
}
