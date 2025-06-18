package com.dstz.sys.rest.model.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * @Name TestHolidayConfDTO
 */
public class TestHolidayConfDTO implements Serializable {
	private static final long serialVersionUID = -8791184722892342808L;
	/**
	 * Start time
	 */
	private Date startDay;

	/**
	 * n days
	 */
	private Integer day;

	public Date getStartDay() {
		return startDay;
	}

	public void setStartDay(Date startDay) {
		this.startDay = startDay;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}
}
