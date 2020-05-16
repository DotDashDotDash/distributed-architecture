package org.codwh.redis.custom.model.system;

import java.io.Serializable;
import java.util.Date;

public class SystemTrackingInfo implements Serializable {

    private static final long serialVersionUID = -6823093035497000901L;

    /**
     * 主键id
     */
    private Integer id;

    /**
     * 任务组id
     */
    private Integer jobGroup;

    /**
     * 任务组名称
     */
    private String jobGroupName;

    /**
     * 任务名称
     */
    private String jobName;

    /**
     * 任务调度cron表达式
     */
    private String jobCron;

    /**
     * 任务描述
     */
    private String jobDesc;

    /**
     * 任务种类
     */
    private Integer jobClass;

    /**
     * 创建时间
     */
    private Date addTime;

    /**
     * 任务作者
     */
    private String author;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 报警邮件
     */
    private String alarmEmail;

    /**
     * 报警阈值
     */
    private Integer alarmThreshold;

    /**
     * 任务种类名称
     */
    private String jobClassName;

    /**
     * 任务状态
     */
    private String jobStatus;

    /**
     * 任务创建人
     */
    private String createUserName;

    /**
     * 类名
     */
    private String className;

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

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getAlarmEmail() {
        return alarmEmail;
    }

    public void setAlarmEmail(String alarmEmail) {
        this.alarmEmail = alarmEmail;
    }

    public Integer getAlarmThreshold() {
        return alarmThreshold;
    }

    public void setAlarmThreshold(Integer alarmThreshold) {
        this.alarmThreshold = alarmThreshold;
    }

    public String getJobClassName() {
        return jobClassName;
    }

    public void setJobClassName(String jobClassName) {
        this.jobClassName = jobClassName;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
