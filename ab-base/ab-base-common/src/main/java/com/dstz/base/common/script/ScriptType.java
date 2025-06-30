package com.dstz.base.common.script;

/**
 * <pre>
 * Script type enumeration
 * </pre>
 */
public enum ScriptType {
	CONFIG("config", "Configuration Mode"),
	HAND("hand", "Handwriting mode");
	/**
	 * key
	 */
	private String key;
	/**
	 * describe
	 */
	private String desc;

	private ScriptType(String key, String desc) {
		this.key = key;
		this.desc = desc;
	}

	public String getKey() {
		return key;
	}

	public String getDesc() {
		return desc;
	}

	/**
	 * <pre>
	 * Determine whether it is consistent with the current one based on the key
	 * </pre>
	 * 
	 * @param key
	 * @return
	 */
	public boolean equalsWithKey(String key) {
		return this.key.equals(key);
	}

	/**
	 * <pre>
	 * Get by key
	 * </pre>
	 * 
	 * @param key
	 * @return
	 */
	public static ScriptType getByKey(String key) {
		for (ScriptType type : ScriptType.values()) {
			if (key.equals(type.getKey())) {
				return type;
			}
		}
		return null;
	}
}
