/**
 * Related information about the user who is operating
 */
import { defineStore } from 'pinia'
import {
  IUserInfo,
  IUserInfoActions,
  IUserInfoState,
  IUserInfoGetter,
} from '../models/userInfoModel'
import { useKeys } from '@/utils/useKeys'
import { useLocalStorage } from '@/utils'
const key = useKeys()
export const useUserStore = defineStore<
  string,
  IUserInfoState,
  IUserInfoGetter,
  IUserInfoActions
>('userStore', {
  state: (): IUserInfoState => {
    return {
      userInfo: null,
      abUser: null,
      currentOrg: null,
      buttonPermission: null,
    }
  },
  getters: {
    GetUsers(state) {
      if (state.userInfo) {
        return state.userInfo
      } else {
        return useLocalStorage.getSecretObject<IUserInfo>(key.USER_INFO)
      }
    },
    getAbUsers(state) {
      if (state.abUser) {
        return state.abUser
      } else {
        return useLocalStorage.getSecretObject('abUser')
      }
    },
    getCurrentOrg(state) {
      if (state.currentOrg) {
        return state.currentOrg
      } else {
        return useLocalStorage.getSecretObject('currentOrg')
      }
    },
    getButtonPermission(state) {
      if (state.buttonPermission) {
        return state.buttonPermission
      } else {
        return useLocalStorage.getSecretObject('buttonPermission')
      }
    },
  },
  actions: {
    setUsers(user) {
      this.userInfo = user
      useLocalStorage.setSecretObject<IUserInfo>(key.USER_INFO, user)
    },
    setAbUsers(user: any) {
      this.abUser = user
      useLocalStorage.setSecretObject('abUser', user)
    },
    setCurrentOrg(org: any) {
      this.currentOrg = org
      useLocalStorage.setSecretObject('currentOrg', org)
    },
    setButtonPermission(btnPermission: any) {
      this.buttonPermission = btnPermission
      useLocalStorage.setSecretObject('buttonPermission', btnPermission)
    },
    clearUsers() {
      useLocalStorage.remove(key.USER_INFO)
    },
  },
})
