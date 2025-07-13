package com.dstz.base.common.constats;

/**
 * String constant pool
 * 
 */
public interface StrPool extends cn.hutool.core.text.StrPool {
	/**
	 * Character constants：$
	 */
	String DOLLAR = "$";

	/**
	 * Remainder, character constant：%
	 */
	String MOD = "%";

	/**
	 * Left bracket, character constant: (
	 */
	String LPAREN = "(";

	/**
	 * Right bracket, character constant:)
	 */
	String RPAREN = ")";
	/**
	 * The value in String that is considered true
	 */
	String BOOLEAN_FALSE = "0";

	/**
	 * The value in String that is considered false
	 */
	String BOOLEAN_TRUE = "1";

	/**
	 * Semicolon, character constant: ;
	 */
	String SEMICOLON = ";";

	/**
	 * Empty string, character constant:
	 */
	String EMPTY = "";

	/**
	 * Number 0
	 */
	String NUMBER_ZERO = "0";

	/**
	 * #Number
	 */
	String HASH = "#";

	/**
	 * *Number
	 */
	String BIT = "*";

	String FALSE = "false";

	String TRUE = "true";

	String FORMATSTR = "%s_%s";
	/**
	 * The default grid symbol for multiple data,
	 */
	String SPLIT = C_COMMA + "";

	String FROM = "system";

	String REGEX = "\\.";

	String STRING_PWD_ONZ = "1";
	// English colon
	String COLON = ":";

	// Chinese Colon
	String COLON_ZN = "：";

	/**
	 * unknown
	 */
	String UNKNOWN = "unknown";

	/**
	 * Default data source alias
	 */
	String DEFAULT_DATASOURCE_ALIAS = "datasourceDefault";
	String EQUAL = "=";

	String AND = "&";

	String SINGLE_QUOTES = "'";

	String WX = "workWeChat";

	String QYWX = "qywx";

	String DD = "dingTalk";

	String LOGINTYPE = "quickLoginType";
}
