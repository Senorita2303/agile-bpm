import { IdefaultModel } from '@/models/IdefaultModel'

export const useDefaultConfig = (): IdefaultModel => {
    const defaultConfig: IdefaultModel = {
        APP_NAME: "Agile Development",
        DASHBOARD_URL: "/dashboard", // Initial page
        APP_VER: "2.5.0",
        CORE_VER: "2.5.0",
        API_URL: 's',
        TIMEOUT: 10000,
        TOKEN_NAME: "Authorization",
        TOKEN_PREFIX: "Bearer ",
        TOKEN_TIME: 50,
        HEADERS: {},
        REQUEST_CACHE: false,
        LAYOUT: 'default',
        MENU_IS_COLLAPSE: false,
        MENU_UNIQUE_OPENED: false,
        LAYOUT_TAGS: false,
        LANG: 'en',
        COLOR: '#009688',
        LS_ENCRYPTION: 'AES',
        LS_ENCRYPTION_key: '2XNN4K8LC0ELVWN4',
        DEFAULT_GRID: {
            layout: [12, 6, 6],
            copmsList: [
                ['welcome'],
                ['about', 'ver'],
                ['time', 'progress']
            ]
        },
        THEME: '',
        MY_SHOW_LOGIN_OAUTH: false
    }
    return defaultConfig
}