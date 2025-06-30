package com.dstz.sys.api.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * @Name WorkCalenDarVO
 * @description: Work calendar VO
 */
public class WorkCalendarVO implements Serializable {
	private static final long serialVersionUID = 7796607053626856841L;

	private String id;

	private Date day;

	private String isWorkDay;

	private String type;

	private String system;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getDay() {
		return day;
	}

	public void setDay(Date day) {
		this.day = day;
	}

	public String getIsWorkDay() {
		return isWorkDay;
	}

	public void setIsWorkDay(String isWorkDay) {
		this.isWorkDay = isWorkDay;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}
}
