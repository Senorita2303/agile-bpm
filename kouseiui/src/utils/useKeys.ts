import { IKey } from "@/models/IKeyModel";

export const useKeys = (): IKey => {
  return {
    APP_DARK: 'APP_DARK',
    MENU_KEY: 'MENU_KEY',
    APP_LANG: 'APP_LANG',
    APP_TAG: 'APP_TAG',
    USER_INFO: 'USER_INFO',
    ACCESS_TOKEN: 'token',
    ACTIVATE_MENU: 'ACTIVATE_MENU',
    FLAT_MENUS: 'FLAT_MENUS',
    APP_COLL: 'APP_COLL',
    APP_LAYOUT: 'APP_LAYOUT',
    APP_COLOR: 'APP_COLOR'
  }
}