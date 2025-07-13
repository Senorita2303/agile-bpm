export interface ICachValue<T> {
    content:T;
    datetime:number

}
export interface ILocalStorage<T extends {}> {
    set(key:string,data:string):void;
    set<T>(key:string,data:T):void;
    get(key:string):string;
    get<T>(key:string):T;
    setSecret(key:string,data:string):void;
    setSecret<T>(key:string,data:T):void;
    getSecret(key:string):string;
    getSecret<T>(key:string):T;
	remove(key:string):void;
}
export interface IToolSession {
    set(table:string, settings:object):void;
    get(table:string):object;
    remove(table:string):void;
    clear():void;
}
export interface ITool {
    data:any;
    session:IToolSession;
    objCopy:IObjCopy,
    dateFormat:Function,
    groupSeparator:Function,
    crypto:ICrypto
}
export interface IObjCopy {
    copy(obj:object):object;
}
export interface ICrypto{
    MD5(data:any):string;
    BASE64:IBase64;
    AES:IAES
}
export interface IBase64{
    encrypt(data:any):any;
    decrypt(cipher:any):any;
}
export interface IAES {
    encrypt(data:any, secretKey:string, config:IAESConfig):string;
    decrypt(cipher:any, secretKey:string, config:IAESConfig):any;
}
export interface IAESConfig {
    iv?:any;
    mode?:any;
    padding?:any
}