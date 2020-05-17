package org.codwh.redis.monitor.service.service;

import org.codwh.redis.custom.model.system.SystemComponent;

import java.util.List;

public interface ISystemComponentService extends IBaseService<SystemComponent>, IPaginationService<SystemComponent> {

    List<SystemComponent> getListByApplicationId(Integer applicationId);
}
