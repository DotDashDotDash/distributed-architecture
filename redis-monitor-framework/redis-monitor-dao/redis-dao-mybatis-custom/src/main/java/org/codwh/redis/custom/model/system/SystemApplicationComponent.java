package org.codwh.redis.custom.model.system;

import java.io.Serializable;

/**
 * 连表查询中间表
 */
public class SystemApplicationComponent implements Serializable {

    private static final long serialVersionUID = 4201120696477955650L;

    /**
     * 主键Id
     */
    private Integer id;

    /**
     * 应用id
     */
    private Integer applicationId;

    /**
     * 配置id
     */
    private Integer componentId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Integer applicationId) {
        this.applicationId = applicationId;
    }

    public Integer getComponentId() {
        return componentId;
    }

    public void setComponentId(Integer componentId) {
        this.componentId = componentId;
    }
}
