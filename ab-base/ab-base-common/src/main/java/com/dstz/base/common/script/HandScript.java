package com.dstz.base.common.script;

/**
 * <pre>
 * Handwritten Script
 * </pre>
 * 
 */
public class HandScript implements java.io.Serializable{

	private static final long serialVersionUID = -6112747578142353989L;

	/**
	 * Script content
	 */
	private String script;
	/**
	 * Description of the script
	 */
	private String desc;

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
