import { _GettersTree } from "pinia";
export interface ITokenModel {
    access_token: string,
    create_time: number
}

export interface IMainState {
    access_token: ITokenModel | null
}
export interface IMainGetter extends _GettersTree<IMainState> {
    GetToken(state: IMainState): ITokenModel

}
export interface IMainActions {
    setToken(token: ITokenModel): void;
    clearToken(): void;
}