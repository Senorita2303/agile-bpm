package com.dstz.sys.api;

import com.dstz.sys.api.vo.WorkCalendarVO;

import java.util.Date;
import java.util.List;

/**
 * @Name WorkCalendarApi
 * @description: Work calendar interface
 */
public interface WorkCalendarApi {

    /**
     * Get calendar information for a certain day
     * Can determine whether it is a working day, calendar details
     *
     * @param day
     * @return
     */
    WorkCalendarVO getWorkCalendarByDay(Date day);

    /**
     * Get calendar information for a certain day
     *
     * @param day
     * @param system
     * @return
     */
    WorkCalendarVO getWorkCalendarByDay(Date day, String system);


    /**
     * Return by time interval
     *
     * @param startDay
     * @param endDay
     * @return
     */
    List<WorkCalendarVO> getWorkCalendars(Date startDay, Date endDay);

    /**
     * Return by time interval
     *
     * @param startDay
     * @param endDay
     * @param system
     * @return
     */
    List<WorkCalendarVO> getWorkCalendars(Date startDay, Date endDay, String system);

    /**
     * Get the specified working day, the working date after N days
     *
     * @param startDay
     * @param days
     * @return
     */
    Date getEndWorkDay(Date startDay, int days);

    /**
     * Get the specified working day, the working date after N days
     *
     * @param startDay
     * @param days
     * @param system
     * @return
     */
    Date getEndWorkDay(Date startDay, int days, String system);

    /**
     * Get the number of days after a certain number of minutes
     *
     * @param startDay
     * @param minute
     * @return
     */
    Date getEndWorkDayByMinute(Date startDay, int minute);
}
