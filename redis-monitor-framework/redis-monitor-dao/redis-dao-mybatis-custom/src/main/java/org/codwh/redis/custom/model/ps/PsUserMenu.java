package org.codwh.redis.custom.model.ps;

import java.io.Serializable;

public class PsUserMenu implements Serializable {

    private static final long serialVersionUID = 4587277367290063186L;

    /**
     * 主键Id
     */
    private Integer id;

    /**
     * userId
     */
    private Integer userId;

    /**
     * menuId
     */
    private Integer menuId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }
}
