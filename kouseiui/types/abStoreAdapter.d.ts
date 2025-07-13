declare interface AbStoreAdapter {
  buttonPermission: any
  userMenuList: any
  getAbUser: AbUser
  currentOrg: any
}

declare interface AbUser {
  fullName: string
  userId: string
  username: string
}

declare interface TabEditInfo {
  fullPath: string
  // name?: string
  meta: TabMeta
}

declare interface TabMeta {
  // Title to be modified
  title: string
  /*  badge?: string
   // Whether to display small dots
   dot?: boolean
   // Icon
   icon?: string
   // Can the current route close multiple tabs?
   noClosable?: boolean */
}
