package org.codwh.common.util;

import org.codwh.common.constants.TomcatStatus;
import org.quartz.core.jmx.QuartzSchedulerMBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.*;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.lang.management.*;
import java.util.*;

public class JvmUtils {

    static Logger logger = LoggerFactory.getLogger(JvmUtils.class);

    /**
     * 工具连接类
     */
    private static MBeanServerConnection mBeanServerConnection;

    /**
     * JMX监控url
     */
    private static final String MONITOR_URL = "service:jmx:rmi:///jndi/rmi://%s:%s/jmxrmi";

    /**
     * GC
     */
    private static final String GC = "java.lang:type=GarbageCollector";

    /**
     * MemoryPool
     */
    private static final String MEMORY = "java.lang:type=MemoryPool";

    /**
     * 操作系统
     */
    private static final String OPERATING_SYSTEM = "java.lang:type=OperatingSystem,*";

    /**
     * Eden区
     */
    private static final String MEMORY_EDEN = "Eden Space";

    /**
     * Survivor区
     */
    private static final String MEMORY_SURVIVOR = "Survivor Space";

    /**
     * Old区
     */
    private static final String MEMORY_OLD = "Old Space";

    /**
     * 永久代
     */
    private static final String MEMORY_PERM = "Perm Gen";

    /**
     * Tomcat线程池
     */
    public static final String TOMCAT_THREAD_POOL = "Catalina:type=ThreadPool,*";

    /**
     * Tomcat会话池
     */
    public static final String TOMCAT_SESSION_POOL = "Catalina:type=Manager,*";

    /**
     * Quartz调度器
     */
    public static final String QUARTZ_SCHEDULER = "quartz:type=QuartzScheduler,*";

    /**
     * JMX连接器
     */
    private static JMXConnector jmxConnector;

    /**
     * 创建JMX连接初始化
     *
     * @param host
     * @param port
     * @param username
     * @param password
     */
    public static void initJmxConnection(String host, int port, String username, String password) {
        try {
            Object[] args = new Object[]{host, String.valueOf(port)};
            String monitorUrl = String.format(MONITOR_URL, args);
            JMXServiceURL serviceURL = new JMXServiceURL(monitorUrl);
            Map<String, String[]> params = new HashMap<>();
            if (StringUtils.isNotEmpty(username) && StringUtils.isNotEmpty(password)) {
                String[] credentials = new String[]{username, password};
                params.put("jmx.remote.credentials", credentials);
            }
            jmxConnector = JMXConnectorFactory.connect(serviceURL);
            mBeanServerConnection = jmxConnector.getMBeanServerConnection();
        } catch (Exception e) {
            logger.error("JMX连接初始化失败: host: {}, port: {}, username: {}, password: {}",
                    host, port, username, password);
        }
    }

    /**
     * 获取JVM内存
     *
     * @return
     */
    public static MemoryMXBean getMemoryMXBean() {
        try {
            return ManagementFactory.newPlatformMXBeanProxy(mBeanServerConnection,
                    ManagementFactory.MEMORY_MXBEAN_NAME, MemoryMXBean.class);
        } catch (Exception e) {
            logger.error("获取MemoryMXBean失败");
            return null;
        }
    }

    /**
     * 获取操作系统
     *
     * @return
     */
    public static OperatingSystemMXBean getOperatingSystemMXBean() {
        try {
            return ManagementFactory.newPlatformMXBeanProxy(mBeanServerConnection,
                    ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME, OperatingSystemMXBean.class);
        } catch (Exception e) {
            logger.error("获取OperatingSystemMXBean失败");
            return null;
        }
    }

    /**
     * 获取JVM线程栈信息
     *
     * @return
     */
    public static ThreadMXBean getThreadMXBean() {
        try {
            return ManagementFactory.newPlatformMXBeanProxy(mBeanServerConnection,
                    ManagementFactory.THREAD_MXBEAN_NAME, ThreadMXBean.class);
        } catch (Exception e) {
            logger.error("获取ThreadMXBean失败");
            return null;
        }
    }

    /**
     * 获取CPU信息
     *
     * @return
     */
    public static CPUBean getCPUBean() {
        try {
            CPUBean cpuBean = new CPUBean();
            ObjectName cpuObjectName = new ObjectName(OPERATING_SYSTEM);
            Set<ObjectName> objs = mBeanServerConnection.queryNames(cpuObjectName, null);
            for (ObjectName obj : objs) {
                ObjectName objName = new ObjectName(obj.getCanonicalName());
                cpuBean.cpuProcessingTime = Long.parseLong(String.valueOf(
                        mBeanServerConnection.getAttribute(objName, "ProcessingCpuTime")));
                cpuBean.availableProcessors = Integer.parseInt(String.valueOf(
                        mBeanServerConnection.getAttribute(objName, "AvailableProcessors")));
                break;
            }
            return cpuBean;
        } catch (Exception e) {
            logger.error("获取CPUBean失败!");
            return null;
        }
    }

    /**
     * 获取内存池信息
     *
     * @return
     */
    public static MemoryPoolMXBean getMemoryPoolMXBean() {
        try {
            return ManagementFactory.newPlatformMXBeanProxy(mBeanServerConnection,
                    ManagementFactory.MEMORY_POOL_MXBEAN_DOMAIN_TYPE, MemoryPoolMXBean.class);
        } catch (Exception e) {
            logger.error("获取MemoryPoolMXBean失败");
            return null;
        }
    }

    /**
     * 获取类加载信息
     *
     * @return
     */
    public static ClassLoadingMXBean getClassLoadingMXBean() {
        try {
            return ManagementFactory.newPlatformMXBeanProxy(mBeanServerConnection,
                    ManagementFactory.CLASS_LOADING_MXBEAN_NAME, ClassLoadingMXBean.class);
        } catch (Exception e) {
            logger.error("获取ClassLoaderMXBean失败");
            return null;
        }
    }

    /**
     * 获取编译信息
     *
     * @return
     */
    public static CompilationMXBean getCompilationMXBean() {
        try {
            return ManagementFactory.newPlatformMXBeanProxy(mBeanServerConnection,
                    ManagementFactory.CLASS_LOADING_MXBEAN_NAME, CompilationMXBean.class);
        } catch (Exception e) {
            logger.error("获取CompilationMXBean失败");
            return null;
        }
    }

    /**
     * 获取RuntimeMXBean失败
     *
     * @return
     */
    public static RuntimeMXBean getRuntimeMXBean() {
        try {
            return ManagementFactory.newPlatformMXBeanProxy(mBeanServerConnection,
                    ManagementFactory.RUNTIME_MXBEAN_NAME, RuntimeMXBean.class);
        } catch (Exception e) {
            logger.error("获取RuntimeMXBean失败");
            return null;
        }
    }

    /**
     * 获取垃圾回收信息
     *
     * @return
     */
    public static HeapMemoryGcBean getHeapMemoryGarbage() {
        try {
            ObjectName gc = new ObjectName(GC);
            Set<ObjectName> objs = mBeanServerConnection.queryNames(gc, null);
            HeapMemoryGcBean heapMemoryGcBean = new HeapMemoryGcBean();
            for (ObjectName obj : objs) {
                if (obj.getKeyProperty("name").contains("MarkSweep")) {
                    ObjectName oldObjName = new ObjectName(obj.getCanonicalName());
                    heapMemoryGcBean.oldGcCount = Long.parseLong(String.valueOf(
                            mBeanServerConnection.getAttribute(oldObjName, "CollectionCount")));
                    heapMemoryGcBean.oldGcTime = Long.parseLong(String.valueOf(
                            mBeanServerConnection.getAttribute(oldObjName, "CollectionTime")));
                } else {
                    ObjectName youngName = new ObjectName(obj.getCanonicalName());
                    heapMemoryGcBean.youngGcCount = Long.parseLong(String.valueOf(
                            mBeanServerConnection.getAttribute(youngName, "CollectionCount")));
                    heapMemoryGcBean.youngGcTime = Long.parseLong(String.valueOf(
                            mBeanServerConnection.getAttribute(youngName, "CollectionTime")));
                }
            }
            return heapMemoryGcBean;
        } catch (Exception e) {
            logger.error("获取HeapMemoryGcInfo失败");
            return null;
        }
    }

    /**
     * 获取JVM各个分区的空间情况
     *
     * @return
     */
    public static MemorySpaceBean getMemorySpaceBean() {
        try {
            MemorySpaceBean memorySpaceBean = new MemorySpaceBean();
            ObjectName mem = new ObjectName(MEMORY);
            Set<ObjectName> objs = mBeanServerConnection.queryNames(mem, null);
            for (ObjectName obj : objs) {
                if (obj.getKeyProperty("name").contains(MEMORY_EDEN)) {
                    ObjectName edenObjectName = new ObjectName(obj.getCanonicalName());
                    MemoryUsage edenUsage = MemoryUsage.from(
                            (CompositeDataSupport) mBeanServerConnection.getAttribute(edenObjectName, "Usage"));
                    memorySpaceBean.edenAllocated = edenUsage.getInit();
                    memorySpaceBean.edenUsed = edenUsage.getUsed();
                    memorySpaceBean.edenCommitted = edenUsage.getCommitted();
                    memorySpaceBean.edenMax = edenUsage.getMax();
                } else if (obj.getKeyProperty("name").contains(MEMORY_SURVIVOR)) {
                    ObjectName survivorObjectName = new ObjectName(obj.getCanonicalName());
                    MemoryUsage survivorUsage = MemoryUsage.from(
                            (CompositeDataSupport) mBeanServerConnection.getAttribute(survivorObjectName, "Usage"));
                    memorySpaceBean.survivorAllocated = survivorUsage.getInit();
                    memorySpaceBean.survivorUsed = survivorUsage.getUsed();
                    memorySpaceBean.survivorCommitted = survivorUsage.getCommitted();
                    memorySpaceBean.survivorMax = survivorUsage.getMax();
                } else if (obj.getKeyProperty("name").contains(MEMORY_OLD)) {
                    ObjectName oldObjectName = new ObjectName(obj.getCanonicalName());
                    MemoryUsage oldUsage = MemoryUsage.from(
                            (CompositeDataSupport) mBeanServerConnection.getAttribute(oldObjectName, "Usage"));
                    memorySpaceBean.oldAllocated = oldUsage.getInit();
                    memorySpaceBean.oldUsed = oldUsage.getUsed();
                    memorySpaceBean.oldCommitted = oldUsage.getCommitted();
                    memorySpaceBean.oldMax = oldUsage.getMax();
                } else if (obj.getKeyProperty("name").contains(MEMORY_PERM)) {
                    ObjectName permObjectName = new ObjectName(obj.getCanonicalName());
                    MemoryUsage permUsage = MemoryUsage.from(
                            (CompositeDataSupport) mBeanServerConnection.getAttribute(permObjectName, "Usage"));
                    memorySpaceBean.permAllocated = permUsage.getInit();
                    memorySpaceBean.permUsed = permUsage.getUsed();
                    memorySpaceBean.permCommitted = permUsage.getCommitted();
                    memorySpaceBean.permMax = permUsage.getMax();
                }
            }
            return memorySpaceBean;
        } catch (Exception e) {
            logger.error("获取MemorySpaceBean失败");
            return null;
        }
    }

    /**
     * 获取tomcat 运行线程
     *
     * @return
     * @throws Exception
     */
    public List<TomcatThreadBean> getTomcatRuntimeBeans(String keyContent, String validateKey) throws Exception {
        List<TomcatThreadBean> tomcatRuntimeBeans = new ArrayList<TomcatThreadBean>();
        ObjectName tomcatObjName = new ObjectName(keyContent);
        Set<ObjectName> s = mBeanServerConnection.queryNames(tomcatObjName, null);
        for (ObjectName obj : s) {
            TomcatThreadBean tomcatRuntimeBean = new TomcatThreadBean();
            if (StringUtils.isNotEmpty(validateKey) && obj.getKeyProperty("name").contains(validateKey)) {
                ObjectName objName = new ObjectName(obj.getCanonicalName());
                tomcatRuntimeBean.name = obj.getKeyProperty("name");
                tomcatRuntimeBean.currentThreadCount = Long.parseLong(String.valueOf(mBeanServerConnection.getAttribute(objName, "currentThreadCount")));
                tomcatRuntimeBean.currentThreadsBusy = Long.parseLong(String.valueOf(mBeanServerConnection.getAttribute(objName, "currentThreadsBusy")));
                tomcatRuntimeBean.maxThreads = Long.parseLong(String.valueOf(mBeanServerConnection.getAttribute(objName, "maxThreads")));
                Boolean runFlag = Boolean.parseBoolean(String.valueOf(mBeanServerConnection.getAttribute(objName, "running")));
                if (runFlag != null && runFlag) {
                    tomcatRuntimeBean.status = TomcatStatus.RUNNING.getCode();//运行
                } else {
                    tomcatRuntimeBean.status = TomcatStatus.STOP.getCode();//停止
                }
                tomcatRuntimeBeans.add(tomcatRuntimeBean);
            }
        }
        return tomcatRuntimeBeans;
    }

    /**
     * 获取session 回话信息
     *
     * @param keyContent
     * @return
     */
    public List<TomcatSessionBean> getTomcatSessionBeans(String keyContent, String validateKey) throws Exception {
        List<TomcatSessionBean> quartzSessionBeans = new ArrayList<TomcatSessionBean>();
        ObjectName tomcatObjName = new ObjectName(keyContent);
        Set<ObjectName> s = mBeanServerConnection.queryNames(tomcatObjName, null);
        for (ObjectName obj : s) {
            TomcatSessionBean quartzSessionBean = new TomcatSessionBean();
            if (StringUtils.isNotEmpty(validateKey) && obj.getKeyProperty("path").contains(validateKey)) {
                quartzSessionBean.frameworkName = obj.getKeyProperty("path");
                ObjectName objname = new ObjectName(obj.getCanonicalName());
                quartzSessionBean.activeSessions = Long.parseLong(String.valueOf(mBeanServerConnection.getAttribute(objname, "activeSessions")));
                quartzSessionBean.maxActiveSessions = Long.parseLong(String.valueOf(mBeanServerConnection.getAttribute(objname, "maxActiveSessions")));
                quartzSessionBean.sessionCounter = Long.parseLong(String.valueOf(mBeanServerConnection.getAttribute(objname, "sessionCounter")));
                quartzSessionBeans.add(quartzSessionBean);
            }
        }
        return quartzSessionBeans;
    }

    /**
     * 获取定时器
     *
     * @return
     * @throws Exception
     */
    public List<QuartzRuntimeBean> getQuartzRuntimeBeans(String keyContent) throws Exception {
        List<QuartzRuntimeBean> quartzRuntimeBeans = new ArrayList<QuartzRuntimeBean>();
        ObjectName tomcatObjName = new ObjectName(keyContent);
        Set<ObjectName> s = mBeanServerConnection.queryNames(tomcatObjName, null);
        for (ObjectName obj : s) {
            QuartzRuntimeBean quartzRuntimeBean = new QuartzRuntimeBean();
            ObjectName objName = new ObjectName(obj.getCanonicalName());
            quartzRuntimeBean.schedulerInstanceId = String.valueOf(mBeanServerConnection.getAttribute(objName, "SchedulerInstanceId"));
            quartzRuntimeBean.schedulerName = String.valueOf(mBeanServerConnection.getAttribute(objName, "SchedulerName"));
            quartzRuntimeBean.ThreadPoolClassName = String.valueOf(mBeanServerConnection.getAttribute(objName, "ThreadPoolClassName"));
            quartzRuntimeBean.ThreadPoolSize = Integer.parseInt(String.valueOf(mBeanServerConnection.getAttribute(objName, "ThreadPoolSize")));
            quartzRuntimeBean.version = String.valueOf(mBeanServerConnection.getAttribute(objName, "Version"));
            Boolean runFlag = Boolean.parseBoolean(String.valueOf(mBeanServerConnection.getAttribute(objName, "Started")));
            if (runFlag != null && runFlag) {
                quartzRuntimeBean.status = TomcatStatus.RUNNING.getCode();
            } else {
                quartzRuntimeBean.status = TomcatStatus.STOP.getCode();//停止
            }
            quartzRuntimeBeans.add(quartzRuntimeBean);
        }
        return quartzRuntimeBeans;
    }

    /**
     * quartz远程重启
     *
     * @param schedulerName
     * @param schedulerInstanceId
     * @throws Exception
     */
    public void restart(String keyContent, String schedulerName, String schedulerInstanceId) throws Exception {
        ObjectName tomcatObjName = new ObjectName(keyContent);
        Set<ObjectName> s = mBeanServerConnection.queryNames(tomcatObjName, null);
        for (ObjectName obj : s) {
            ObjectName objName = new ObjectName(obj.getCanonicalName());
            String name = String.valueOf(mBeanServerConnection.getAttribute(objName, "SchedulerName"));
            String scheduleId = String.valueOf(mBeanServerConnection.getAttribute(objName, "SchedulerInstanceId"));
            if (schedulerName.equals(name) && scheduleId.equals(schedulerInstanceId)) {
                QuartzSchedulerMBean quartzSchedulerMBean = MBeanServerInvocationHandler.newProxyInstance(mBeanServerConnection, objName, QuartzSchedulerMBean.class, false);
                quartzSchedulerMBean.shutdown();
                quartzSchedulerMBean.start();
                break;
            }
        }
    }

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

    public static class MemorySpaceBean {

        /**
         * eden区分配空间大小
         */
        private Long edenAllocated;

        /**
         * eden区已经使用的空间大小
         */
        private Long edenUsed;

        /**
         * eden区最大空间
         */
        private Long edenMax;

        /**
         * eden区提交的空间大小
         */
        private Long edenCommitted;

        /**
         * survivor区分配的空间大小
         */
        private Long survivorAllocated;

        /**
         * survivor区使用的空间大小
         */
        private Long survivorUsed;

        /**
         * survivor区最大空间
         */
        private Long survivorMax;

        /**
         * survivor区已经提交的空间大小
         */
        private Long survivorCommitted;

        /**
         * old区分配的空间大小
         */
        private Long oldAllocated;

        /**
         * old区使用的空间大小
         */
        private Long oldUsed;

        /**
         * old区最大空间
         */
        private Long oldMax;

        /**
         * old区已经提交的空间大小
         */
        private Long oldCommitted;

        /**
         * perm区分配的空间大小
         */
        private Long permAllocated;

        /**
         * perm区使用的空间大小
         */
        private Long permUsed;

        /**
         * perm区最大空间
         */
        private Long permMax;

        /**
         * perm区已经提交的空间大小
         */
        private Long permCommitted;
    }

    public static class CPUBean {

        /**
         * CPU处理时间
         */
        private Long cpuProcessingTime;

        /**
         * 可用的处理器数目
         */
        private Integer availableProcessors;
    }

    public static class HeapMemoryGcBean {

        /**
         * 年轻代GC总量
         */
        private Long youngGcCount;

        /**
         * 年轻代回收时间
         */
        private Long youngGcTime;

        /**
         * 老年代GC总量
         */
        private Long oldGcCount;

        /**
         * 老年代GC时间
         */
        private Long oldGcTime;
    }

    public static class TomcatThreadBean {

        private Long maxThreads;//最大线程

        private Long currentThreadCount;//当前线程总数

        private Long currentThreadsBusy;//繁忙线程

        private int status;//当前状态

        private String name;//名称
    }

    public static class TomcatSessionBean {

        private String frameworkName;

        private Long maxActiveSessions;

        private Long activeSessions;

        private Long sessionCounter;
    }

    public static class QuartzRuntimeBean {

        private String schedulerInstanceId;

        private String schedulerName;

        private Integer status;

        private String ThreadPoolClassName;

        private Integer ThreadPoolSize;

        private String version;//版本号

        private String statusName;//
    }
}
