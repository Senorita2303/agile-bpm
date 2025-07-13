package com.dstz.base.query;

/**
 * <pre>
 * Description: Enumerate the relationships between query fields.
 * </pre>
 */
public enum FieldRelation {
    AND("AND"),
    OR("OR"),
    NOT("NOT");
	
    private String key;

    FieldRelation(String key) {
    	this.key = key;
    }

    public String key() {
        return key;
    }
}
