
import { nextTick } from 'vue'
export function beforeEach(to:any, from:any){
	var adminMain = document.querySelector('#adminui-main')
	if(!adminMain){return false}
}

export function afterEach(to:any){
	var adminMain = document.querySelector('#adminui-main') as HTMLElement
	if(!adminMain){return false}

}