package org.codwh.redis.custom.dao;

import org.codwh.redis.custom.model.redis.RedisMonitorLog;

public interface IRedisClusterMonitorLogDao extends IMyBatisRepository<RedisMonitorLog>, IPaginationDao<RedisMonitorLog> {
}
