package org.codwh.redis.custom.model.ps;

import java.io.Serializable;

public class PsDict implements Serializable {

    private static final long serialVersionUID = 8802437236322663753L;

    /**
     * 主键
     */
    private Integer id;

    /**
     * 字段名称
     */
    private String psKey;

    /**
     * 字段值
     */
    private String psValue;

    /**
     * 模块
     */
    private String psModule;

    /**
     * 状态
     */
    private Integer psStatus;

    /**
     * 状态名称
     */
    private String psStatusName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPsKey() {
        return psKey;
    }

    public void setPsKey(String psKey) {
        this.psKey = psKey;
    }

    public String getPsValue() {
        return psValue;
    }

    public void setPsValue(String psValue) {
        this.psValue = psValue;
    }

    public String getPsModule() {
        return psModule;
    }

    public void setPsModule(String psModule) {
        this.psModule = psModule;
    }

    public Integer getPsStatus() {
        return psStatus;
    }

    public void setPsStatus(Integer psStatus) {
        this.psStatus = psStatus;
    }

    public String getPsStatusName() {
        return psStatusName;
    }

    public void setPsStatusName(String psStatusName) {
        this.psStatusName = psStatusName;
    }
}
