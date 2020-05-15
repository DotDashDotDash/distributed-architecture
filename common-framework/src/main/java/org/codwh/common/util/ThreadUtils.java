package org.codwh.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadUtils {

    private static Logger logger = LoggerFactory.getLogger(ThreadUtils.class);

    /**
     * 休眠当前线程
     *
     * @param time
     */
    public static void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            logger.error(e.getLocalizedMessage());
        }
    }
}
