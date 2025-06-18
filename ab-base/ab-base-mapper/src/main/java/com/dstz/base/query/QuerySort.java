package com.dstz.base.query;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class QuerySort {
	public static final String ASC = "ASC";
    /**
     * The fields to be sorted
     */
    private String column;
    /**
     * Whether to sort in positive order, default true
     */
    private Boolean asc = true;
    
	public String getColumn() {
		return column;
	}
	public void setColumn(String column) {
		this.column = column;
	}
	
	public Boolean getAsc() {
		return asc;
	}
	public void setAsc(Boolean asc) {
		this.asc = asc;
	}
	 

    public QuerySort(String column, Boolean asc) {
		this.column = column;
		this.asc = asc;
	}
    
	public static QuerySort asc(String column) {
        return build(column, true);
    }

    public static QuerySort desc(String column) {
        return build(column, false);
    }
    

    public static List<QuerySort> ascs(String... columns) {
        return Arrays.stream(columns).map(QuerySort::asc).collect(Collectors.toList());
    }

    public static List<QuerySort> descs(String... columns) {
        return Arrays.stream(columns).map(QuerySort::desc).collect(Collectors.toList());
    }

    private static QuerySort build(String column, boolean asc) {
        return new QuerySort(column, asc);
    }

}
