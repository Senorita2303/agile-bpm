export interface IdefaultModel {
	APP_NAME: string;
	// Home page address
	DASHBOARD_URL: string;
	// Version number
	APP_VER: string

	// Kernel version number
	CORE_VER: string,

	// Interface address
	API_URL: string;

	// Request timed out 
	TIMEOUT: number,

	// TokenName
	TOKEN_NAME: string;

	// Token prefix, note that there is a space at the end, if not needed, set an empty string
	TOKEN_PREFIX: string;

	TOKEN_TIME: number, // Token expiration time (minutes)
	// Append other headers
	HEADERS: object;

	// Request whether to enable cache
	REQUEST_CACHE: boolean;

	// Layout Default：default | Full column：header | Classic：menu | Function dock：dock
	// The dock will close the tabs and breadcrumbs bar
	LAYOUT: string;

	// Is the menu collapsed?
	MENU_IS_COLLAPSE: boolean;

	// Whether the menu enables the accordion effect
	MENU_UNIQUE_OPENED: boolean;

	// Whether to enable multiple tabs
	LAYOUT_TAGS: boolean;

	// Language
	LANG: string;
	// Theme color
	COLOR: string;

	// Whether to encrypt localStorage, if it is empty, it will not be encrypted, you can fill in AES (mode ECB, shift Pkcs7) encryption
	LS_ENCRYPTION: string;

	// localStorage AES encryption key, it is recommended to fill in a multiple of 8 digits
	LS_ENCRYPTION_key: string

	// Default layout of the console home page
	DEFAULT_GRID: GridModel;
	THEME: string,
	MY_SHOW_LOGIN_OAUTH: boolean
}
export interface GridModel {
	layout: Array<number>;
	copmsList: Array<Array<string>>
}
