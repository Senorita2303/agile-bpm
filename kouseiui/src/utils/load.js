/**
 * loadJS Load remote JS asynchronously
 * @constructor
 * @param {string} src - Required, the URL path to be loaded
 * @param {string} keyName - Required, unique key and global object name returned by JS
 * @param {string} callbackName - Not required. If the remote JS has a callback, it can be more effective to determine whether the loading is complete
 */
export function loadJS(src, keyName, callbackName) {
	return new Promise((resolve, reject) => {
		let has = document.head.querySelector("script[loadKey=" + keyName + "]")
		if (has) {
			return resolve(window[keyName])
		}
		let script = document.createElement("script")
		script.type = "text/javascript"
		script.src = src
		script.setAttribute("loadKey", keyName)
		document.head.appendChild(script)
		script.onload = () => {
			if (callbackName) {
				window[callbackName] = () => {
					return resolve(window[keyName])
				}
			} else {
				setTimeout(() => {
					return resolve(window[keyName])
				}, 50)
			}
		}
		script.onerror = (err) => {
			return reject(err)
		}
	})
}

/**
 * loadCSS Asynchronous loading of remote css
 * @constructor
 * @param {string} src - Required, the URL path to be loaded
 * @param {string} keyName - Required, unique key
 */
export function loadCSS(src, keyName) {
	return new Promise((resolve, reject) => {
		let has = document.head.querySelector("link[loadKey=" + keyName + "]")
		if (has) {
			return resolve()
		}
		let link = document.createElement('link')
		link.rel = "stylesheet"
		link.href = src
		link.setAttribute("loadKey", keyName)
		document.head.appendChild(link)
		link.onload = () => {
			return resolve()
		}
		link.onerror = (err) => {
			return reject(err)
		}
	})
}
