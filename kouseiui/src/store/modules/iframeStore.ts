/**
 * Operation iframe configuration
 */
import { defineStore } from "pinia";
import { IFrameActions, IFrameState } from "../models/iframeModel";

const useIframeStore = defineStore<string, IFrameState, {}, IFrameActions>('iframe', {
	state: (): IFrameState => {
		return {
			iframeList: []
		}
	},
	actions: {
		setIframeList(route) {
			this.$state.iframeList = []
			this.$state.iframeList.push(route)
		},
		pushIframeList(route) {
			let target = this.$state.iframeList.find((item) => item.path === route.path)
			if (!target) {
				this.$state.iframeList.push(route)
			}
		},
		removeIframeList(route) {
			this.$state.iframeList.forEach((item, index) => {
				if (item.path === route.path) {
					this.$state.iframeList.splice(index, 1)
				}
			})
		},
		refreshIframe(route) {
			this.$state.iframeList.forEach((item) => {
				if (item.path == route.path) {
					var url = route.meta.url;
					item.meta.url = '';
					setTimeout(function () {
						item.meta.url = url
					}, 200);
				}
			})
		},
		clearIframeList() {
			this.$state.iframeList = []
		}
	}
})
export {
	useIframeStore
}