import { IRouterModel } from "../../router/models/routeModel";
import {  RouterOptions } from 'vue-router'
export interface IViewTagState {
    viewTags:Array<IRouterModel>
}
export interface IUpdateTags {
    fullPath:string
    scrollTop:number
}
export interface IViewTagActions {
    pushViewTags(route:IRouterModel):void;
    removeViewTags(route:IRouterModel):void;
    updateViewTags(route:IUpdateTags):void;
    updateViewTagsTitle(state:IViewTagState,title:string):void;
    clearViewTags():void;
}