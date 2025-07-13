import { IRouterModel } from "../../router/models/routeModel";

export interface IFrameState {
    iframeList:Array<IRouterModel>
}
export interface IFrameActions{
    setIframeList(route:IRouterModel):void;
    pushIframeList( route:IRouterModel):void;
    removeIframeList(route:IRouterModel):void;
    refreshIframe(route:IRouterModel):void;
    clearIframeList():void;
}