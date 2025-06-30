import axios from 'axios';
import { ElNotification, ElMessageBox } from 'element-plus';
import sysConfig from "@/config";
import tool from '@/utils/tool';
import router from '@/router';

axios.defaults.baseURL = ''

axios.defaults.timeout = sysConfig.TIMEOUT

// HTTP request interceptor
axios.interceptors.request.use(
	(config) => {
		let token = tool.cookie.get("TOKEN");
		if (token) {
			config.headers[sysConfig.TOKEN_NAME] = sysConfig.TOKEN_PREFIX + token
		}
		if (!sysConfig.REQUEST_CACHE && config.method == 'get') {
			config.params = config.params || {};
			config.params['_'] = new Date().getTime();
		}
		Object.assign(config.headers, sysConfig.HEADERS)
		return config;
	},
	(error) => {
		return Promise.reject(error);
	}
);

// HTTP response interceptor
axios.interceptors.response.use(
	(response) => {
		return response;
	},
	(error) => {
		if (error.response) {
			if (error.response.status == 404) {
				ElNotification.error({
					title: 'Request error',
					message: "Status:404, requesting a server record that does not exist!"
				});
			} else if (error.response.status == 500) {
				ElNotification.error({
					title: 'Request error',
					message: error.response.data.message || "Status: 500, server error!"
				});
			} else if (error.response.status == 401) {
				ElMessageBox.confirm('The current user has been logged out or has no permission to access the current resource. Please try to log in again before operating.', '无权限访问', {
					type: 'error',
					closeOnClickModal: false,
					center: true,
					confirmButtonText: 'Re-login'
				}).then(() => {
					router.replace({ path: '/login' });
				}).catch(() => { })
			} else {
				ElNotification.error({
					title: 'Request error',
					message: error.message || `Status:${error.response.status}, Unknown error!`
				});
			}
		} else {
			ElNotification.error({
				title: 'Request error',
				message: "The server did not respond!"
			});
		}

		return Promise.reject(error.response);
	}
);

var http = {

	/** get Request
	 * @param  {Interface address} url
	 * @param  {Request parameters} params
	 * @param  {Parameters} config
	 */
	get: function (url, params = {}, config = {}) {
		return new Promise((resolve, reject) => {
			axios({
				method: 'get',
				url: url,
				params: params,
				...config
			}).then((response) => {
				resolve(response.data);
			}).catch((error) => {
				reject(error);
			})
		})
	},

	/** post Request
	 * @param  {Interface address} url
	 * @param  {Request parameters} data
	 * @param  {Parameters} config
	 */
	post: function (url, data = {}, config = {}) {
		return new Promise((resolve, reject) => {
			axios({
				method: 'post',
				url: url,
				data: data,
				...config
			}).then((response) => {
				resolve(response.data);
			}).catch((error) => {
				reject(error);
			})
		})
	},

	/** put request
	 * @param  {Interface address} url
	 * @param  {Request parameters} data
	 * @param  {Parameters} config
	 */
	put: function (url, data = {}, config = {}) {
		return new Promise((resolve, reject) => {
			axios({
				method: 'put',
				url: url,
				data: data,
				...config
			}).then((response) => {
				resolve(response.data);
			}).catch((error) => {
				reject(error);
			})
		})
	},

	/** patch request
	 * @param  {Interface address} url
	 * @param  {Request parameters} data
	 * @param  {Parameters} config
	 */
	patch: function (url, data = {}, config = {}) {
		return new Promise((resolve, reject) => {
			axios({
				method: 'patch',
				url: url,
				data: data,
				...config
			}).then((response) => {
				resolve(response.data);
			}).catch((error) => {
				reject(error);
			})
		})
	},

	/** delete request
	 * @param  {Interface address} url
	 * @param  {Request parameters} data
	 * @param  {Parameters} config
	 */
	delete: function (url, data = {}, config = {}) {
		return new Promise((resolve, reject) => {
			axios({
				method: 'delete',
				url: url,
				data: data,
				...config
			}).then((response) => {
				resolve(response.data);
			}).catch((error) => {
				reject(error);
			})
		})
	},

	/** jsonp Request
	 * @param  {Interface address} url
	 * @param  {JSONP callback function name} name
	 */
	jsonp: function (url, name = 'jsonp') {
		return new Promise((resolve) => {
			var script = document.createElement('script')
			var _id = `jsonp${Math.ceil(Math.random() * 1000000)}`
			script.id = _id
			script.type = 'text/javascript'
			script.src = url
			window[name] = (response) => {
				resolve(response)
				document.getElementsByTagName('head')[0].removeChild(script)
				try {
					delete window[name];
				} catch (e) {
					window[name] = undefined;
				}
			}
			document.getElementsByTagName('head')[0].appendChild(script)
		})
	}
}

export default http;
