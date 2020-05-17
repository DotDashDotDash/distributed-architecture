package org.codwh.redis.monitor.service.service;

import org.codwh.common.exception.DataPersistentException;
import org.codwh.redis.custom.dao.IMyBatisRepository;

import java.util.List;

public abstract class AbstractBaseService<T> implements IBaseService<T> {

    /**
     * 子类需要注入特定的DAO实现
     */
    protected abstract IMyBatisRepository<T> getMyBatisRepository();

    /**
     * 对参数进行预操作，不能涉及事务，涉及事务请用createXXX
     *
     * @param entity
     * @throws Exception
     */
    protected void preCreate(T entity) throws DataPersistentException {
    }

    /**
     * 对参数进行预操作，不能涉及事务，涉及事务请用modifyXXX
     *
     * @param entity
     * @throws DataPersistentException
     */
    protected void preModify(T entity) throws DataPersistentException {
    }

    /**
     * 对参数进行预操作，不能涉及事务，涉及事务请用deleteXXX
     *
     * @param id
     * @throws DataPersistentException
     */
    protected void preDelete(Integer id) throws DataPersistentException {
    }

    /**
     * 对参数进行预操作，不能涉及事务，涉及事务请用getXXX
     *
     * @param id
     * @throws DataPersistentException
     */
    protected void preGet(Integer id) throws DataPersistentException {
    }

    /**
     * 对参数进行预操作，不能涉及事务，涉及事务请用queryXXX
     *
     * @param entity
     * @throws DataPersistentException
     */
    protected void preQuery(T entity) throws DataPersistentException {
    }

    /**
     * 数据库新增对象
     *
     * @param entity
     * @throws DataPersistentException
     */
    @Override
    public void create(T entity) throws DataPersistentException {
        preCreate(entity);

        int status = getMyBatisRepository().insert(entity);
        if (status != 1) {
            throw new DataPersistentException("向数据库中插入数据失败!");
        }
    }

    /**
     * 更新数据库对象
     *
     * @param entity
     * @throws DataPersistentException
     */
    @Override
    public void modifyById(T entity) throws DataPersistentException {
        preModify(entity);

        int status = getMyBatisRepository().updateById(entity);
        if (status != 1) {
            throw new DataPersistentException("更新数据库对象失败!");
        }
    }

    /**
     * 删除数据库对象
     *
     * @param id
     * @throws DataPersistentException
     */
    @Override
    public void deleteById(Integer id) throws DataPersistentException {
        preDelete(id);

        int status = getMyBatisRepository().deleteById(id);
        if (status != 1) {
            throw new DataPersistentException("删除数据库对象失败!");
        }
    }

    /**
     * 根据id获取
     *
     * @param id
     * @return
     * @throws DataPersistentException
     */
    @Override
    public T getById(Integer id) throws DataPersistentException {
        preGet(id);

        return getMyBatisRepository().getById(id);
    }

    /**
     * 根据对象获取
     *
     * @param queryObject
     * @return
     * @throws DataPersistentException
     */
    @Override
    public List<T> queryList(T queryObject) throws DataPersistentException {
        preQuery(queryObject);

        return getMyBatisRepository().getListByCriteria(queryObject);
    }
}
