package org.codwh.redis.custom.dao;

import org.codwh.redis.custom.model.ps.PsUser;

public interface IPsUserDao extends IMyBatisRepository<PsUser>, IPaginationDao<PsUser> {
}
