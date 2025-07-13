package com.dstz.sys.api;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.dstz.sys.api.vo.WorkCalendarVO;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;

/**
 * @Name WorkCalendarApiImpl
 * @description: Work calendar service interface implementation
 */
@Service
public class WorkCalendarApiImpl implements WorkCalendarApi {

	@Override
	public WorkCalendarVO getWorkCalendarByDay(Date day) {
		return  null;
	}

	@Override
	public WorkCalendarVO getWorkCalendarByDay(Date day, String system) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<WorkCalendarVO> getWorkCalendars(Date startDay, Date endDay) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<WorkCalendarVO> getWorkCalendars(Date startDay, Date endDay, String system) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getEndWorkDay(Date startDay, int days) {
		return DateUtil.offset(startDay, DateField.HOUR, days*24);
	}

	@Override
	public Date getEndWorkDay(Date startDay, int days, String system) {
		return DateUtil.offset(startDay, DateField.HOUR, days*24);
	}

	@Override
	public Date getEndWorkDayByMinute(Date startDay, int minute) {
		return DateUtil.offset(startDay, DateField.MINUTE, minute);
	}

     
}
