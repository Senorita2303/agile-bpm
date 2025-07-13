package com.dstz.auth.authentication.api.constant;

public enum ResouceTypeConstant {
    /**
     * Menu
     */
    MENU("menu", "Menu"),
    /**
     * Link
     */
    LINK("link", "Link"),
    /**
     * Button
     */
    BUTTON("button", "Button");

    private final String key;
    private final String label;


    ResouceTypeConstant(String key, String label) {
        this.key = key;
        this.label = label;
    }

    public String getLabel() {
  		return label;
  	}

	public String getKey() {
		return key;
	}
}
