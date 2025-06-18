
// Verify mobile number
export function verifyPhone(rule, value, callback) {
	let reg = /^[1][3, 4, 5, 6, 7, 8, 9][0-9]{9}$/
	if (!reg.test(value)) {
		return callback(new Error('Please enter a valid mobile phone number'))
	}
	callback()
}

// License plate number
export function verifyCars(rule, value, callback) {
	let reg = /^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-HJ-NP-Z][A-HJ-NP-Z0-9]{4,5}[A-HJ-NP-Z0-9挂学警港澳]$/
	if (!reg.test(value)) {
		return callback(new Error('Please enter the correct license plate number'))
	}
	callback()
}
