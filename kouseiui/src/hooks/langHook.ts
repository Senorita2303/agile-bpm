import i18n from "@/locales";
import { useLocalStorage } from "@/utils";
import { useKeys } from "@/utils/useKeys";
const key = useKeys()
export const useChangeLang = (val: string): void => {
       i18n.global.locale = (val === 'vi' ? 'vi' : 'en')
       useLocalStorage.setSecretObject<string>(key.APP_LANG, val)
}