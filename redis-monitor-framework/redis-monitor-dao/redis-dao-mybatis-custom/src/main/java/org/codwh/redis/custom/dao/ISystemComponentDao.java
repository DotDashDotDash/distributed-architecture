package org.codwh.redis.custom.dao;

import org.codwh.redis.custom.model.system.SystemComponent;

import java.util.List;

public interface ISystemComponentDao extends IMyBatisRepository<SystemComponent>, IPaginationDao<SystemComponent> {

    List<SystemComponent> getListByApplicationId(Integer applicationId);
}
