package org.codwh.redis.monitor.service.service;

import org.codwh.redis.custom.model.ps.PsMenu;

import java.util.List;

public interface IPsMenuService extends IBaseService<PsMenu>, IPaginationService<PsMenu> {

    List<PsMenu> getMenusByUserId(PsMenu psMenu);
}
