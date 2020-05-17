package org.codwh.redis.monitor.service.service;

import org.codwh.common.exception.DataPersistentException;

import java.util.List;

public interface IBaseService<T> {

    void create(T entity) throws Exception;

    void modifyById(T entity) throws DataPersistentException;

    void deleteById(Integer id) throws DataPersistentException;

    T getById(Integer id) throws DataPersistentException;

    List<T> queryList(T queryObject) throws DataPersistentException;
}
