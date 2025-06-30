import CryptoJS, { mode, pad } from 'crypto-js'
import { useDefaultConfig } from "@/config/defaultConfig";
import { ITokenModel } from "@/store/models/webModel";
import dayjs from 'dayjs';
import { useKeys } from './useKeys';
import { IRouteMenu } from '@/models/IRouterMenu';
const key = useKeys()
const config = useDefaultConfig()
const cfg = {
    mode: CryptoJS.mode.ECB,
    padding: CryptoJS.pad.Pkcs7
}
export const useLocalStorage = {
    get(key: string): string {
        return localStorage.getItem(key) as string
    },
    getObject<T>(key: string): T | null {
        let json = localStorage.getItem(key) as string
        return json === null ? json : JSON.parse(json) as T;
    },
    getSecret(key: string): string {
        let data = localStorage.getItem(key) as string;
        let value = CryptoJS.AES.decrypt(data, config.LS_ENCRYPTION_key, cfg).toString()
        return value
    },
    getSecretObject<T>(key: string): T | null {
        let data = localStorage.getItem(key) as string;
        if (data === null)
            return data
        let value = CryptoJS.AES.decrypt(data, config.LS_ENCRYPTION_key, cfg).toString(CryptoJS.enc.Utf8)
        if(value ==="")return value as T;
        return JSON.parse(value) as T;
    },
    set(key: string, data: string): void {
        localStorage.setItem(key, data)
    },
    setObject<T>(key: string, data: T): void {
        let json = JSON.stringify(data)
        localStorage.setItem(key, json)
    },
    setSecret(key: string, data: string): void {
        let json = CryptoJS.AES.encrypt(data, config.LS_ENCRYPTION_key, cfg).toString()
        localStorage.setItem(key, data)
    },
    setSecretObject<T>(key: string, data: T): void {
        let json = JSON.stringify(data)
        let seData = CryptoJS.AES.encrypt(json, config.LS_ENCRYPTION_key, cfg).toString()
        localStorage.setItem(key, seData)
    },
    remove(key: string): void {
        localStorage.removeItem(key)
    }
}
export const checkTokenExp = (): boolean => {
    const token = useLocalStorage.getSecretObject<ITokenModel>(key.ACCESS_TOKEN)
    if (token == null)
        return true
    const creatime = new Date(token.create_time)
    const nowDate = dayjs()
    const diff = nowDate.diff(creatime, 'minute')
    return diff > config.TOKEN_TIME
}
export const resolveTag = (items: Array<IRouteMenu>, menu: IRouteMenu): Array<IRouteMenu> => {
    const routes: Array<IRouteMenu> = []
    routes.push(menu)
    let item = items.find(x => x.path === menu.meta.parentPath) as IRouteMenu
    if(item.meta.parentPath) {
        const parents = resolveTag(items, item);
        routes.push(...parents)
    }else{
        routes.push(item)
    }
    return routes
}