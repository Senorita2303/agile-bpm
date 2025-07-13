import { _GettersTree } from "pinia";
export interface IUserInfo {
    userName: string,
    userNameF: string,
}
export interface IUserInfoState {
    userInfo?: IUserInfo | null
    currentOrg: any
    buttonPermission: any
    abUser: any
}
export interface IUserInfoGetter extends _GettersTree<IUserInfoState> {
    GetUsers(user: IUserInfoState): IUserInfo | null;
}
export interface IUserInfoActions {
    // Current user JSON
    setUsers(user: IUserInfo): void;
    clearUsers(): void;
    // Current organization
    setCurrentOrg(org: any): void;
    setAbUsers(user: any): void;
    // Current user button operation permissions
    setButtonPermission(btnPermission: any): void;
}