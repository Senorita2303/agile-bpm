import { IRouterModel } from "../../router/models/routeModel";

export interface IKeepLiveState {
    keepLiveRoute: Array<string>;
    routeKey: string;
    routeShow: boolean;
}
export interface IKeepLiveActions{
    pushKeepLive( name:string):void;
    removeKeepLive(component:string):void;
    clearKeepLive():void;
    setRouteKey(key:string):void;
    setRouteShow(key:boolean):void;
}