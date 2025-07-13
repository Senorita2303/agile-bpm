
export function useColor(): any {

	// Convert hex color to rgb color
	const HexToRgb = (str: any): any => {
		str = str.replace("#", "")
		var hxs = str.match(/../g)
		for (var i = 0; i < 3; i++) hxs[i] = parseInt(hxs[i], 16)
		return hxs
	}
	const RgbToHex = (a: any, b: any, c: any): any => {
		var hexs: any[] = [a.toString(16), b.toString(16), c.toString(16)]
		for (var i = 0; i < 3; i++) {
			if (hexs[i].length == 1) hexs[i] = "0" + hexs[i]
		}
		return "#" + hexs.join("");
	}
	const darken = (color: any, level: any): any => {
		var rgbc = HexToRgb(color)
		for (var i = 0; i < 3; i++) rgbc[i] = Math.floor(rgbc[i] * (1 - level))
		return RgbToHex(rgbc[0], rgbc[1], rgbc[2])
	}
	const lighten = (color: any, level: any): any => {
		var rgbc = HexToRgb(color)
		for (var i = 0; i < 3; i++) rgbc[i] = Math.floor((255 - rgbc[i]) * level + rgbc[i])
		return RgbToHex(rgbc[0], rgbc[1], rgbc[2])
	}
	return {
		HexToRgb,
		RgbToHex,
		darken,
		lighten

	}
}
