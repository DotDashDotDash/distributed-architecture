package org.codwh.common.util;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

public class JvmUtils {

    /**
     * 获取调用栈的信息
     *
     * @return
     */
    public static String getStackMessage() {
        StringBuilder stackInfo = new StringBuilder();
        Throwable ex = new Throwable();
        for (StackTraceElement elem : getStackElements()) {
            stackInfo.append(elem.toString());
            stackInfo.append("\r\n");
        }
        return stackInfo.toString();
    }

    /**
     * 获取调用栈元素
     *
     * @return
     */
    public static StackTraceElement[] getStackElements() {
        Throwable ex = new Throwable();
        return ex.getStackTrace();
    }

    /**
     * 获取JVM中所有的线程
     *
     * @return
     */
    public static Thread[] getAllJvmThreads() {
        ThreadGroup group = Thread.currentThread().getThreadGroup().getParent();
        int estimatedSize = group.activeCount() * 2;
        Thread[] temp = new Thread[estimatedSize];
        int actualSize = group.enumerate(temp);
        Thread[] res = new Thread[actualSize];
        System.arraycopy(temp, 0, res, 0, actualSize);
        return res;
    }

    /**
     * 获取JVM已经启动的线程的信息
     *
     * @return
     */
    public static ThreadInfo[] getJvmThreadInfo() {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        ThreadInfo[] res = threadMXBean.dumpAllThreads(false, false);
        return res;
    }
}
