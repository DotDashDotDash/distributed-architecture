package org.codwh.redis.custom.dao;

import org.codwh.redis.custom.model.redis.RedisClusterMaster;

import java.util.List;

public interface IRedisClusterMasterDao extends IMyBatisRepository<RedisClusterMaster>, IPaginationDao<RedisClusterMaster> {

    /**
     * 根据主键来更新版本号，乐观锁
     *
     * @param clusterMaster
     * @return
     */
    int updateVersionByIdAndVersion(RedisClusterMaster clusterMaster);

    /**
     * 根据master状态来查询master，查询前50条数据
     *
     * @return
     */
    List<RedisClusterMaster> getListByStatus();
}
