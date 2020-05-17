package org.codwh.redis.monitor.service.util.quartz;

public interface IQuartzFramework {

    /**
     * 执行
     *
     * @param times 失败次数
     * @return
     */
    String invoke(int times);
}
