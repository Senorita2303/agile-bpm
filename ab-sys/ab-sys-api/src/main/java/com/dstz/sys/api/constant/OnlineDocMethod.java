package com.dstz.sys.api.constant;

/**
 * @Name OnlineDocMethod
 * @description: Online document operation method
 */
public enum OnlineDocMethod {
	OPEN(1, "/api.do", "Open document"), SAVE(2, "/api.do", "Save document"), CLOSE(3, "/api.do", "Close document"),
	IS_OPEN(4, "/api.do", "Determine whether the document is open"),

	;

	OnlineDocMethod(Integer key, String path, String desc) {
		this.desc = desc;
		this.key = key;
		this.path = path;
	}

	private String desc;
	private String path;
	private Integer key;

	public Integer getKey() {
		return key;
	}

	public String getPath() {
		return path;
	}
}
