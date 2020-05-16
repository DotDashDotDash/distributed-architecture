package org.codwh.redis.custom.model.system;

import java.io.Serializable;
import java.util.Date;

public class SystemTrackingLog implements Serializable {

    private static final long serialVersionUID = -137940326096567491L;

    /**
     * 主键
     */
    private Integer id;
    /**
     * 任务组
     */
    private Integer jobGroup;

    private String jobGroupName;
    /**
     * 任务名
     */
    private String jobName;
    /**
     * cron表达式
     */
    private String jobCron;
    /**
     * 任务描述
     */
    private String jobDesc;
    /**
     * 执行方式
     */
    private Integer jobClass;

    private String jobClassName;
    /**
     * 任务数据
     */
    private String jobData;
    /**
     * 调度时间
     */
    private Date triggerTime;

    /**
     * 调度结果
     */
    private String triggerStatus;
    /**
     * 调度日志
     */
    private String triggerMsg;
    /**
     * 执行时间
     */
    private Date handleTime;

    /**
     * 执行状态
     */
    private String handleStatus;
    /**
     * 执行日志
     */
    private String handleMsg;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(Integer jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getJobGroupName() {
        return jobGroupName;
    }

    public void setJobGroupName(String jobGroupName) {
        this.jobGroupName = jobGroupName;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobCron() {
        return jobCron;
    }

    public void setJobCron(String jobCron) {
        this.jobCron = jobCron;
    }

    public String getJobDesc() {
        return jobDesc;
    }

    public void setJobDesc(String jobDesc) {
        this.jobDesc = jobDesc;
    }

    public Integer getJobClass() {
        return jobClass;
    }

    public void setJobClass(Integer jobClass) {
        this.jobClass = jobClass;
    }

    public String getJobClassName() {
        return jobClassName;
    }

    public void setJobClassName(String jobClassName) {
        this.jobClassName = jobClassName;
    }

    public String getJobData() {
        return jobData;
    }

    public void setJobData(String jobData) {
        this.jobData = jobData;
    }

    public Date getTriggerTime() {
        return triggerTime;
    }

    public void setTriggerTime(Date triggerTime) {
        this.triggerTime = triggerTime;
    }

    public String getTriggerStatus() {
        return triggerStatus;
    }

    public void setTriggerStatus(String triggerStatus) {
        this.triggerStatus = triggerStatus;
    }

    public String getTriggerMsg() {
        return triggerMsg;
    }

    public void setTriggerMsg(String triggerMsg) {
        this.triggerMsg = triggerMsg;
    }

    public Date getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(Date handleTime) {
        this.handleTime = handleTime;
    }

    public String getHandleStatus() {
        return handleStatus;
    }

    public void setHandleStatus(String handleStatus) {
        this.handleStatus = handleStatus;
    }

    public String getHandleMsg() {
        return handleMsg;
    }

    public void setHandleMsg(String handleMsg) {
        this.handleMsg = handleMsg;
    }
}
