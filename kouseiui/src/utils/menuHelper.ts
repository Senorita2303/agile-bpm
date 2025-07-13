export const useMenu = () => {
  let serverData = [
    {
      name: "home",
      path: "/home",
      meta: {
        title: "Home Page",
        icon: "el-icon-eleme-filled",
        type: "menu"
      },
      children: [
        {
          name: "dashboard",
          path: "/dashboard",
          meta: {
            title: "Dashboard",
            icon: "el-icon-menu",
            affix: true
          },
          component: "home"
        }
      ]
    },
    {
      name: "setting",
      path: "/setting",
      meta: {
        title: "Configuration",
        icon: "el-icon-setting",
        type: "menu"
      },
      children: [
        {
          path: "/setting/system",
          name: "system",
          meta: {
            title: "System settings",
            icon: "el-icon-tools",
            type: "menu"
          },
          component: "setting/system"
        },
        {
          path: "/setting/user",
          name: "user",
          meta: {
            title: "User management",
            icon: "el-icon-user-filled",
            type: "menu"
          },
          component: "setting/user"
        }
      ]
    }
  ];
  return serverData
}