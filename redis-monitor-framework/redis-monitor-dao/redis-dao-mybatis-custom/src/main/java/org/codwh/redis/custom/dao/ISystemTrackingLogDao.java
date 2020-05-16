package org.codwh.redis.custom.dao;

import org.codwh.redis.custom.model.system.SystemTrackingLog;

public interface ISystemTrackingLogDao extends IMyBatisRepository<SystemTrackingLog>, IPaginationDao<SystemTrackingLog> {
}
