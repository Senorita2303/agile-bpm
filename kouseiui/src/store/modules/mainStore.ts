/**
 * Operation token configuration
 * 
 */
import { useLocalStorage } from "@/utils";
import { useKeys } from "@/utils/useKeys";
import { defineStore } from "pinia";
import { IMainActions, IMainGetter, IMainState, ITokenModel } from "../models/webModel";
const key = useKeys()
export const useMainStore = defineStore<string, IMainState, IMainGetter, IMainActions>('mainStore', {
   state: (): IMainState => {
      return {
         access_token: null
      }
   },
   getters: {
      GetToken(state) {
         let value = useLocalStorage.getSecretObject<ITokenModel>(key.ACCESS_TOKEN)
         if (value) {
            return value
         } else {
            return state.access_token!
         }
      }
   },
   actions: {
      setToken(token) {
         this.$state.access_token = token
         useLocalStorage.setSecretObject<ITokenModel>(key.ACCESS_TOKEN, token)
      },
      clearToken() {
         useLocalStorage.remove(key.ACCESS_TOKEN)
      }
   }
})