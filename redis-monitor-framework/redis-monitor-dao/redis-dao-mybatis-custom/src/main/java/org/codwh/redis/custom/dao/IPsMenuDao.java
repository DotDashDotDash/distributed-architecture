package org.codwh.redis.custom.dao;

import org.codwh.redis.custom.model.ps.PsMenu;

import java.util.List;

public interface IPsMenuDao extends IMyBatisRepository<PsMenu>, IPaginationDao<PsMenu> {

    /**
     * 根据UserId查询菜单
     *
     * @param psMenu
     * @return
     */
    List<PsMenu> getMenusByUserId(PsMenu psMenu);
}
