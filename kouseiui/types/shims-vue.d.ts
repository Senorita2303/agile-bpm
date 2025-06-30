/* 
declare module "@agilebpm/build/ab-core/ab-core.umd.min.js" {
  export function setupAbCore(app: vue<Element>);
  export const abTableMix: any;
  export const svgIcon: any;
  export const abDictTree: any;
  export const abAuthorization: any;
} 

declare module "@agilebpm/build/avue-form-design/avue-form-design.umd.min.js" {
  export function setupFormDesign(app: vue<Element>);
}  */
import * as axios from 'axios'

declare module 'axios' {
  interface AxiosInstance {
    (config: AxiosRequestConfig): Promise<any>
  }
}
 

 declare module "agilebpm" {
  export function setupFormDesign(app: any): void;
  export const abForm: any;
  export function setupAbCore(app: any): void;
  export const abTableMix: any;
  export const svgIcon: any;
  export const abDictTree: any;
  export const abAuthorization: any;

  export const registerApi: any;
  export const request: any;
  export const sysApi: any;
  export const bizApi: any;
  export const bpmApi: any;
  export const abTools: any;
  export const abUtil: any;
  export const cmsApi: any;
  export const getData: any;
  export const orgApi: any;

  export const abDesigner: any;
  export const postData: any;
  export const cptApi: any;
  export const BpmImageDialog: any;
  export const ApproveHistoryTable: any;
  export const BpmImageBlock: any;
  export const bpmButtons: any;
  export const bpmTools: any;
}
 