package com.dstz.base.common.property.vo;

import java.io.Serializable;

/**
 * Process Configuration
 *
 */
public class FlowConfVO implements Serializable {
    private int bpmScheduleStartTaskInterval;

    private int reminder_max_retries;

    private Boolean doTaskSuccessional;

    private Boolean task_remove_message_push;

    private String bpmNotDefaultButtons;

    private int taskDefaultDurationHour;

    private Boolean flowLeftTreeTypeSwitch;



    public String getBpmNotDefaultButtons() {
        return bpmNotDefaultButtons;
    }

    public void setBpmNotDefaultButtons(String bpmNotDefaultButtons) {
        this.bpmNotDefaultButtons = bpmNotDefaultButtons;
    }

    public Boolean getDoTaskSuccessional() {
        return doTaskSuccessional;
    }

    public void setDoTaskSuccessional(Boolean doTaskSuccessional) {
        this.doTaskSuccessional = doTaskSuccessional;
    }

    public Boolean getTask_remove_message_push() {
        return task_remove_message_push;
    }

    public void setTask_remove_message_push(Boolean task_remove_message_push) {
        this.task_remove_message_push = task_remove_message_push;
    }

    public Boolean getFlowLeftTreeTypeSwitch() {
        return flowLeftTreeTypeSwitch;
    }

    public void setFlowLeftTreeTypeSwitch(Boolean flowLeftTreeTypeSwitch) {
        this.flowLeftTreeTypeSwitch = flowLeftTreeTypeSwitch;
    }

    public int getBpmScheduleStartTaskInterval() {
        return bpmScheduleStartTaskInterval;
    }

    public void setBpmScheduleStartTaskInterval(int bpmScheduleStartTaskInterval) {
        this.bpmScheduleStartTaskInterval = bpmScheduleStartTaskInterval;
    }

    public int getReminder_max_retries() {
        return reminder_max_retries;
    }

    public void setReminder_max_retries(int reminder_max_retries) {
        this.reminder_max_retries = reminder_max_retries;
    }

    public int getTaskDefaultDurationHour() {
        return taskDefaultDurationHour;
    }

    public void setTaskDefaultDurationHour(int taskDefaultDurationHour) {
        this.taskDefaultDurationHour = taskDefaultDurationHour;
    }
}
