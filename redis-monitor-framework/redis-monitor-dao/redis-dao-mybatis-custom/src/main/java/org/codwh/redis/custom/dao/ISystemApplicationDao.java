package org.codwh.redis.custom.dao;

import org.codwh.redis.custom.model.system.SystemApplication;

import java.util.List;

public interface ISystemApplicationDao extends IMyBatisRepository<SystemApplication>, IPaginationDao<SystemApplication> {

    /**
     * 根据主键来更新版本号，乐观锁
     *
     * @param systemApplication
     * @return
     */
    int updateVersionByIdAndVersion(SystemApplication systemApplication);

    /**
     * 查询前20条数据
     *
     * @param status
     * @return
     */
    List<SystemApplication> getListByStatus(Integer status);
}
