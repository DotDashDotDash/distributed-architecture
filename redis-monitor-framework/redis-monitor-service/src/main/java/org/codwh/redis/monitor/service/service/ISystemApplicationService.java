package org.codwh.redis.monitor.service.service;

import org.codwh.redis.custom.model.system.SystemApplication;

public interface ISystemApplicationService extends IBaseService<SystemApplication>, IPaginationService<SystemApplication> {

    int updateVersionByIdAndVersion(SystemApplication systemApplication);

    SystemApplication getSystemApplicationByHostAndPort(String host, Integer port) throws Exception;
}
