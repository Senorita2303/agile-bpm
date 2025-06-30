/**
 * Global code error capture
 * For example, null.length will be caught
 */

export default (error, vm) => {
	// Filter HTTP request errors
	if (error.status || error.status == 0) {
		return false
	}

	var errorMap = {
		InternalError: "Javascript engine internal error",
		ReferenceError: "Object not found",
		TypeError: "Wrong type or object used",
		RangeError: "When using a built-in object, the parameter is out of range",
		SyntaxError: "Syntax error",
		EvalError: "Incorrect use of Eval",
		URIError: "URI error"
	}
	var errorName = errorMap[error.name] || "Unknown error"

	console.warn(`[SCUI error]: ${error}`);
	console.error(error);
	//throw error;

	vm.$nextTick(() => {
		vm.$notify.error({
			title: errorName,
			message: error
		});
	})
}
