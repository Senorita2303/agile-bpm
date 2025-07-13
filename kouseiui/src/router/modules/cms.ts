import Layout from '@/layout/index.vue'
export default {
  path: '/cms',
  name: 'CMS',
  component: Layout,
  meta: {
    title: 'CMS',
    icon: 'home-2-line',
    breadcrumbHidden: true,
  },
  children: [
    {
      path: '/cms/notice/noticeList',
      name: 'NoticeList',
      component: () => import('@/views/cms/notice/noticeList.vue'),
      meta: {
        title: 'Announcement List',
        icon: 'Menu',
        affix: true,
        type: 'menu',
        parentPath: '/cms'
      },
    },
    {
      path: '/cms/notice/noticeEdit',
      name: 'NoticeEdit',
      component: () => import('@/views/cms/notice/noticeEdit.vue'),
      meta: {
        title: 'Edit Announcement List',
        icon: 'Menu',
        affix: true,
        type: 'menu',
        parentPath: '/cms'
      },
    },
    {
      path: '/cms/notice/noticeDetails',
      name: 'NoticeDetails',
      component: () => import('@/views/cms/notice/noticeDetails.vue'),
      meta: {
        title: 'Notice list details',
        icon: 'Menu',
        affix: true,
        type: 'menu',
        parentPath: '/cms'
      },
    },
    {
      path: '/cms/notice/noticeIndexList',
      name: 'NoticeIndexList',
      component: () => import('@/views/cms/notice/noticeIndexList.vue'),
      meta: {
        title: 'Notice details list preview',
        icon: 'Menu',
        affix: true,
        type: 'menu',
        parentPath: '/cms'
      },
    },
    {
      path: '/cms/news/newsList',
      name: 'NewsList',
      component: () => import('@/views/cms/news/newsList.vue'),
      meta: {
        title: 'News List',
        icon: 'Menu',
        affix: true,
        type: 'menu',
        parentPath: '/cms'
      },
    },
    {
      path: '/cms/news/newsIndexList',
      name: 'NewsIndexList',
      component: () => import('@/views/cms/news/newsIndexList.vue'),
      meta: {
        title: 'News details list preview',
        icon: 'Menu',
        affix: true,
        type: 'menu',
        parentPath: '/cms'
      },
    },
    {
      path: '/cms/news/newsEdit',
      name: 'NewsEdit',
      component: () => import('@/views/cms/news/newsEdit.vue'),
      meta: {
        title: 'News Editor',
        icon: 'Menu',
        affix: true,
        type: 'menu',
        parentPath: '/cms'
      },
    },
    {
      path: '/cms/news/newsDetails',
      name: 'NewsDetails',
      component: () => import('@/views/cms/news/newsDetails.vue'),
      meta: {
        title: 'News Details',
        icon: 'Menu',
        affix: true,
        type: 'menu',
        parentPath: '/cms'
      },
    }
  ],
}
