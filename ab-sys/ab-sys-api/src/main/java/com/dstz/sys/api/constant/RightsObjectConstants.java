package com.dstz.sys.api.constant;


public enum RightsObjectConstants {
	FLOW("FLOW","Process definition"),
	CHART("CHART","Custom charts"),
	WORKBENCH("WORKBENCH","Workbench"),
	HOME("HOME","Home page components"),
	DOCUMENT("DOCUMENT","Knowledge base");
	private final String key;
	private final String label;

	RightsObjectConstants(String key,String label){
		this.key = key;
		this.label = label;
	}
	public String key(){
		return key;
	}
	public String label(){
		return label;
	}

	public static RightsObjectConstants getByKey(String key){
		for (RightsObjectConstants rights : RightsObjectConstants.values()) {
			if(rights.key.equals(key)){
				return rights;
			}
		}
		throw new RuntimeException(String.format(" key [%s] The corresponding RightsObjectConstants does not exist. The permission definition is always on. Please check!",key));
	}
}
