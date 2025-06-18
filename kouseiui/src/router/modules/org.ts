import Layout from '@/layout/index.vue'
export default {
  path: '/org',
  name: 'ORG',
  component: Layout,
  meta: {
    title: 'Organization',
    icon: 'home-2-line',
    breadcrumbHidden: true,
  },
  children: [
    {
      path: '/org/user/userList',
      name: 'UserList',
      component: () => import('@/views/org/user/userList.vue'),
      meta: {
        title: 'User Management',
        icon: 'home-2-line',
      },
    },
    {
      path: '/org/user/userEdit',
      name: 'userEdit',
      component: () => import('@/views/org/user/userEdit.vue'),
      meta: {
        title: 'Edit User',
        icon: 'dashboard-line',
        noKeepAlive: true,
      },
    },
    {
      path: '/org/group/groupList',
      name: 'GroupList',
      component: () => import('@/views/org/group/groupList.vue'),
      meta: {
        title: 'Organization and Management',
      },
    },
    {
      path: '/org/group/groupUserList',
      name: 'GroupUserList',
      component: () => import('@/views/org/group/groupUserList.vue'),
      meta: {
        title: 'Organization and personnel management',
        icon: 'grid-fill',
      },
    },
    {
      path: '/org/role/list',
      name: 'RoleList',
      component: () => import('@/views/org/role/roleList.vue'),
      meta: {
        title: 'Role Management',
        icon: 'grid-fill',
      },
    },
    {
      path: '/org/role/edit',
      name: 'RoleEdit',
      component: () => import('@/views/org/role/roleEdit.vue'),
      meta: {
        title: 'Role Edit',
        icon: 'grid-fill',
        noKeepAlive: true,
      },
    },
    {
      path: '/org/role/users',
      name: 'RoleUserList',
      component: () => import('@/views/org/role/roleUserList.vue'),
      meta: {
        title: 'Role User Management',
        icon: 'grid-fill',
      },
    },
  ],
}
