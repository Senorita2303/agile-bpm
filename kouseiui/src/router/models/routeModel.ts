export interface IRouterModel {
    name:string,
    path:string,
    component?:string,
    type?:string,
    fullPath?:string,
    redirect?:string,
    children?:Array<IRouterModel>,
    meta:IMetaModel,
    icon?:string,
    title?:string
   
    
}
export interface IMetaModel{
    title?:string;
    hidden?:boolean;
    affix?:boolean;
    icon?:string;
    type?:string;
    hiddenBreadcrumb?:boolean;
    active?:string;
    color?:string;
    fullpage?:boolean,
    breadcrumb?:Array<any>,
    role?:Array<string>,
    url?:string
}
