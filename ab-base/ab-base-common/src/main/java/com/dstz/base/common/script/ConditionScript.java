package com.dstz.base.common.script;

import cn.hutool.core.util.StrUtil;

/**
 * <pre>
 * Front-end general condition script configuration object
 * </pre>
 * 
 */
public class ConditionScript implements java.io.Serializable{

	private static final long serialVersionUID = 8232653926713818605L;

	/**
	 * ScriptType
	 */
	private String type;
	/**
	 * Handwritten script content
	 */
	private HandScript handScript;
	/**
	 * Configuration script content
	 */
	private ConfigScript configScript;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public HandScript getHandScript() {
		return handScript;
	}

	public void setHandScript(HandScript handScript) {
		this.handScript = handScript;
	}

	public ConfigScript getConfigScript() {
		return configScript;
	}

	public void setConfigScript(ConfigScript configScript) {
		this.configScript = configScript;
	}
	
	/**
	 * <pre>
	 * Get result script by type
	 * </pre>	
	 * @return
	 */
	public String getResultScript() {
		if (ScriptType.HAND.equalsWithKey(this.type)) {
			return this.handScript.getScript();
		}
		if (ScriptType.CONFIG.equalsWithKey(this.type)) {
			return this.configScript.getScript();
		}
		return "";
	}
	
	/**
	 * <pre>
	 * Get result description by type
	 * </pre>	
	 * @return
	 */
	public String getResultDesc() {
		String desc = null;
		if (ScriptType.HAND.equalsWithKey(this.type)) {
			desc = this.handScript.getDesc();
		}
		if (ScriptType.CONFIG.equalsWithKey(this.type)) {
			desc = this.configScript.getDesc();
		}
		if (StrUtil.isEmpty(desc)) {
			return this.getResultScript();
		}
		return desc;
	}
}
