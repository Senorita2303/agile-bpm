import { IRouteMenu } from "@/models/IRouterMenu";
import { _GettersTree } from "pinia";

export interface IRouterMenuState {
    menus:Array<IRouteMenu>,
    activateMenu:IRouteMenu | null,
    tabList:Array<IRouteMenu>,
    platMenus:Array<IRouteMenu>,
    topMenus:Array<IRouteMenu>,
    activePath:string,
    pMenu:IRouteMenu | null,
    reload:boolean
}
export interface IRouterMenuGetter extends _GettersTree<IRouterMenuState> {
    getMenus():Array<IRouteMenu> |null,
    getActivate():IRouteMenu | null,
    getTabList():Array<IRouteMenu>,
    getPlatMenus():Array<IRouteMenu> |null,
    getTopMenus():Array<IRouteMenu>
}
export interface IRouterActions{
    setActivePath(path:string):void;
    setMenus(menus:Array<IRouteMenu>):void;
    clearMenus():void;
    setActivate(menu:IRouteMenu):void;
    clearActivate():void;
    setTabList(menu:IRouteMenu):void;
    setPlatMenus(menus:Array<IRouteMenu>):void;
    clearPlatMenus():void;
    setTopMenus(menus:IRouteMenu):void,
    clearTopMenus():void;
    removeTab(path:string):void;
    setPMenu(menu:IRouteMenu):void;
    setReload(load:boolean):void;
    setOpenNamesByMenu(menus:Array<IRouteMenu>):void;
    updateTabTitle(path:string,title:string):void;
}