import { IRouteMenu } from "@/models/IRouterMenu";
export const useMenuApi = (): Array<IRouteMenu> => {
  let serverData: Array<IRouteMenu> = [
    {
      name: "home",
      path: "/home",
      meta: {
        title: "Home Page",
        icon: "HomeFilled",
        type: "menu"
      },
      children: [
        {
          name: "dashboard",
          path: "/dashboard",
          meta: {
            title: "Dashboard",
            icon: 'Menu',
            affix: true,
            type: 'menu',
            parentPath: '/home'
          },
          component: "../views/home/index.vue"
        },
        {
          name: "news",
          path: "/news",
          meta: {
            title: "News",
            icon: 'Menu',
            type: 'menu',
            parentPath: '/home'
          },
          component: "../views/cms/news/newsList.vue"
        }
      ]
    },
    {
      name: "setting",
      path: "/setting",
      meta: {
        title: "Configuration",
        icon: "Setting",
        type: "menu"
      }
    },
    {
      name: "intertest",
      path: "/intertest",
      meta: {
        title: "Internal testing",
        icon: "Setting",
        type: "menu"
      },
      children: [
        {
          path: "/intertest/test",
          name: "test",
          meta: {
            title: "My test",
            icon: 'Tools',
            type: "menu",
            parentPath: '/intertest'
          },
          component: "../views/testform/index.vue"
        },
        {
          path: "/intertest/test2",
          name: "test2",
          meta: {
            title: "My test 2",
            icon: 'Tools',
            type: "menu",
            parentPath: '/intertest'
          },
          component: "../views/testform/test2.vue"
        }
      ]
    }
  ];
  return serverData
}