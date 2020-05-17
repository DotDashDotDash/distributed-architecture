package org.codwh.redis.custom.model.system;

import java.io.Serializable;
import java.util.Date;

public class SystemApplication implements Serializable {

    private static final long serialVersionUID = 6856558115348060284L;

    /**
     * 主键Id
     */
    private Integer id;

    /**
     * application名称
     */
    private String applicationName;

    /**
     * application主机
     */
    private String applicationHost;

    /**
     * application端口
     */
    private Integer applicationPort;

    /**
     * JMX主机
     */
    private String jmxHost;

    /**
     * JMX端口号
     */
    private Integer jmxPort;

    /**
     * JMX用户名
     */
    private String jmxUserName;

    /**
     * JMX密码
     */
    private String jmxPassword;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * application状态
     */
    private Integer applicationStatus;

    /**
     * JMX状态
     */
    private Integer jmxStatus;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getApplicationHost() {
        return applicationHost;
    }

    public void setApplicationHost(String applicationHost) {
        this.applicationHost = applicationHost;
    }

    public Integer getApplicationPort() {
        return applicationPort;
    }

    public void setApplicationPort(Integer applicationPort) {
        this.applicationPort = applicationPort;
    }

    public String getJmxHost() {
        return jmxHost;
    }

    public void setJmxHost(String jmxHost) {
        this.jmxHost = jmxHost;
    }

    public String getJmxUserName() {
        return jmxUserName;
    }

    public void setJmxUserName(String jmxUserName) {
        this.jmxUserName = jmxUserName;
    }

    public String getJmxPassword() {
        return jmxPassword;
    }

    public void setJmxPassword(String jmxPassword) {
        this.jmxPassword = jmxPassword;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(Integer applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    public Integer getJmxStatus() {
        return jmxStatus;
    }

    public void setJmxStatus(Integer jmxStatus) {
        this.jmxStatus = jmxStatus;
    }

    public Integer getJmxPort() {
        return jmxPort;
    }

    public void setJmxPort(Integer jmxPort) {
        this.jmxPort = jmxPort;
    }
}
