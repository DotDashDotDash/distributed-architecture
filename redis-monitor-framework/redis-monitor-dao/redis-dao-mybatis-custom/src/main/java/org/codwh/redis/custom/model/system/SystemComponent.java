package org.codwh.redis.custom.model.system;

import java.io.Serializable;
import java.util.Date;

public class SystemComponent implements Serializable {

    private static final long serialVersionUID = -5026646750819363203L;

    /**
     * 主键Id
     */
    private Integer id;

    /**
     * 组件名称
     */
    private String componentName;

    /**
     * 组件值
     */
    private String componentValue;

    /**
     * 组件关键配置
     */
    private String keyConfig;

    /**
     * 组件创建人
     */
    private String createUserName;

    /**
     * 组件创建时间
     */
    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getComponentValue() {
        return componentValue;
    }

    public void setComponentValue(String componentValue) {
        this.componentValue = componentValue;
    }

    public String getKeyConfig() {
        return keyConfig;
    }

    public void setKeyConfig(String keyConfig) {
        this.keyConfig = keyConfig;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
