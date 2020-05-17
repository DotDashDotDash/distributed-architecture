package org.codwh.redis.monitor.service.util;

public abstract class DistributedLock {

    /**
     * 尝试获取锁
     *
     * @return
     * @throws Exception
     */
    public boolean tryLock() throws Exception {
        try {
            return lock();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 指定时间内尝试获得锁
     *
     * @param timeout
     * @return
     * @throws Exception
     */
    public boolean tryLock(int timeout) throws Exception {
        do {
            if (tryLock()) {
                return true;
            }
            timeout -= 100;

            try {
                Thread.sleep(Math.min(100, 100 + timeout));
            } catch (InterruptedException e) {
            }
        } while (timeout > 0);
        return false;
    }

    /**
     * 上锁
     *
     * @return
     * @throws Exception
     */
    protected abstract boolean lock() throws Exception;

    /**
     * 检查锁是否还有效
     *
     * @return
     * @throws Exception
     */
    public abstract boolean checkLockValidity() throws Exception;

    /**
     * 维持心跳，仅在heartbeat < timeout有效
     *
     * @return
     * @throws Exception
     */
    public abstract boolean heartbeat() throws Exception;

    /**
     * 解锁
     *
     * @return
     * @throws Exception
     */
    public abstract boolean unlock() throws Exception;
}
