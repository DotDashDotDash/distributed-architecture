package org.codwh.common.util.JVM;


public class QuartzRuntimeBean {

    private String schedulerInstanceId;

    private String schedulerName;

    private Integer status;

    private String ThreadPoolClassName;

    private Integer ThreadPoolSize;

    private String version;//版本号

    private String statusName;//

    public String getSchedulerInstanceId() {
        return schedulerInstanceId;
    }

    public void setSchedulerInstanceId(String schedulerInstanceId) {
        this.schedulerInstanceId = schedulerInstanceId;
    }

    public String getSchedulerName() {
        return schedulerName;
    }

    public void setSchedulerName(String schedulerName) {
        this.schedulerName = schedulerName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getThreadPoolClassName() {
        return ThreadPoolClassName;
    }

    public void setThreadPoolClassName(String threadPoolClassName) {
        ThreadPoolClassName = threadPoolClassName;
    }

    public Integer getThreadPoolSize() {
        return ThreadPoolSize;
    }

    public void setThreadPoolSize(Integer threadPoolSize) {
        ThreadPoolSize = threadPoolSize;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}
