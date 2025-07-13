package com.dstz.auth.model.dto;

/**
 * <pre>
 * Description: Enterprise WeChat Department
 * </pre>
 *
 */
public class QyWxDepartmentDTO {


    private int id; // The created department id
    private String name; // Department Name
    private String name_en; // English name
    private int parentid; // Parent department id
    private int order; // The order value in the parent department. The larger the order value, the higher the order. The value range is [0, 2^32)

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName_en() {
        return name_en;
    }

    public void setName_en(String name_en) {
        this.name_en = name_en;
    }

    public int getParentid() {
        return parentid;
    }

    public void setParentid(int parentid) {
        this.parentid = parentid;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
