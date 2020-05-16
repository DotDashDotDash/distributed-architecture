package org.codwh.redis.custom.model.system;

import java.io.Serializable;
import java.util.Date;

public class SystemApplicationMonitor implements Serializable {

    private static final long serialVersionUID = -5023455976111372652L;

    /**
     * monitor主键id
     */
    private Integer id;

    /**
     * monitor所属模块
     */
    private Integer monitorModule;

    /**
     * monitor名称
     */
    private String monitorName;

    /**
     * monitor状态
     */
    private Integer monitorStatus;

    /**
     * monitor主机
     */
    private String monitorHost;

    /**
     * monitor端口号
     */
    private Integer monitorPort;

    /**
     * monitor创建时间
     */
    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMonitorModule() {
        return monitorModule;
    }

    public void setMonitorModule(Integer monitorModule) {
        this.monitorModule = monitorModule;
    }

    public String getMonitorName() {
        return monitorName;
    }

    public void setMonitorName(String monitorName) {
        this.monitorName = monitorName;
    }

    public Integer getMonitorStatus() {
        return monitorStatus;
    }

    public void setMonitorStatus(Integer monitorStatus) {
        this.monitorStatus = monitorStatus;
    }

    public String getMonitorHost() {
        return monitorHost;
    }

    public void setMonitorHost(String monitorHost) {
        this.monitorHost = monitorHost;
    }

    public Integer getMonitorPort() {
        return monitorPort;
    }

    public void setMonitorPort(Integer monitorPort) {
        this.monitorPort = monitorPort;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
