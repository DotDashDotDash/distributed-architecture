package org.codwh.common.util.JVM;

import org.codwh.common.constants.TomcatStatus;
import org.codwh.common.util.StringUtils;
import org.quartz.core.jmx.QuartzSchedulerMBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.lang.management.*;
import java.util.*;

public class MonitorJVM {

    public final static String TOMCAT_THREAD_POOL = "Catalina:type=ThreadPool,*";
    public final static String TOMCAT_SESSION_POOL = "Catalina:type=Manager,*";
    public final static String QUARTZ_SCHEDULER = "quartz:type=QuartzScheduler,*";
    private final static String GARBAGE_COLLECTOR = "java.lang:type=GarbageCollector,*";
    private final static String MEM = "java.lang:type=MemoryPool,*";
    private final static String MEM_EDEN = "Eden Space";
    private final static String MEM_SURVIVOR = "Survivor Space";
    private final static String MEM_OLD = "Old Gen";
    private final static String MEM_PERM = "Perm Gen";
    private MBeanServerConnection mbsc;//工具连接类
    private String monitorURL = "service:jmx:rmi:///jndi/rmi://%s:%s/jmxrmi";
    private Logger logger = LoggerFactory.getLogger(MonitorJVM.class);
    private JMXConnector connector;

    /**
     * 构造方法
     */
    public MonitorJVM(String host, int port, String username, String password) {
        init(host, port, username, password);
    }

    private void init(String host, int port, String username, String password) {
        try {
            Object[] args = new Object[]{host, String.valueOf(port)};
            monitorURL = String.format(monitorURL, args);
            JMXServiceURL serviceURL = new JMXServiceURL(monitorURL);
            Map<String, String[]> params = new HashMap<String, String[]>();
            if (StringUtils.isNotEmpty(username) && StringUtils.isNotEmpty(password)) {
                String[] credentials = new String[]{username, password};
                params.put("jmx.remote.credentials", credentials);
            }
            connector = JMXConnectorFactory.connect(serviceURL, params);
            mbsc = connector.getMBeanServerConnection();
        } catch (Exception e) {
            logger.error("init error,host:" + host + ",port:" + port + ",username:" + username + ",password:" + password, e);
        }
    }

    public void close() throws Exception {
        connector.close();
    }

    /**
     * 获取内存
     *
     * @return
     * @throws Exception
     */
    public MemoryMXBean getMemoryMXBean() throws Exception {
        return ManagementFactory.newPlatformMXBeanProxy
                (mbsc, ManagementFactory.MEMORY_MXBEAN_NAME, MemoryMXBean.class);
    }

    /**
     * 获取操作系统
     *
     * @return
     * @throws Exception
     */
    public OperatingSystemMXBean getOperatingSystemMXBean() throws Exception {
        return ManagementFactory.newPlatformMXBeanProxy
                (mbsc, ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME, OperatingSystemMXBean.class);
    }

    /**
     * 获取栈线程信息
     *
     * @return
     * @throws Exception
     */
    public ThreadMXBean getThreadMXBean() throws Exception {
        return ManagementFactory.newPlatformMXBeanProxy
                (mbsc, ManagementFactory.THREAD_MXBEAN_NAME, ThreadMXBean.class);
    }

    /**
     * 获取cpu 时间
     *
     * @return
     * @throws Exception
     */
    public CPUBean getProcessCpuTime() throws Exception {
        CPUBean cpuBean = new CPUBean();
        ObjectName cpuObjName = new ObjectName("java.lang:type=OperatingSystem,*");
        Set<ObjectName> objs = mbsc.queryNames(cpuObjName, null);
        for (ObjectName obj : objs) {
            ObjectName objectName = new ObjectName(obj.getCanonicalName());
            cpuBean.setProcessCpuTime(Long.parseLong(String.valueOf(mbsc.getAttribute(objectName, "ProcessCpuTime"))));
            cpuBean.setAvailableProcessors(Integer.parseInt(String.valueOf(mbsc.getAttribute(objectName, "AvailableProcessors"))));
            break;
        }
        return cpuBean;
    }

    /**
     * 获取内存池
     *
     * @return
     * @throws Exception
     */
    public MemoryPoolMXBean getMemoryPoolMXBean() throws Exception {
        return ManagementFactory.newPlatformMXBeanProxy
                (mbsc, ManagementFactory.MEMORY_POOL_MXBEAN_DOMAIN_TYPE, MemoryPoolMXBean.class);
    }

    /**
     * 获取类加载的信息
     *
     * @return
     * @throws Exception
     */
    public ClassLoadingMXBean getClassLoadingMXBean() throws Exception {
        return ManagementFactory.newPlatformMXBeanProxy
                (mbsc, ManagementFactory.CLASS_LOADING_MXBEAN_NAME, ClassLoadingMXBean.class);
    }

    /**
     * 获取编译信息
     *
     * @return
     * @throws Exception
     */
    public CompilationMXBean getCompilationMXBean() throws Exception {
        return ManagementFactory.newPlatformMXBeanProxy
                (mbsc, ManagementFactory.CLASS_LOADING_MXBEAN_NAME, CompilationMXBean.class);
    }

    /**
     * 获取操作系统信息
     *
     * @return
     * @throws Exception
     */
    public RuntimeMXBean getRuntimeMXBean() throws Exception {
        return ManagementFactory.newPlatformMXBeanProxy
                (mbsc, ManagementFactory.RUNTIME_MXBEAN_NAME, RuntimeMXBean.class);
    }

    /**
     * 获取垃圾回收器信息
     *
     * @return
     * @throws Exception
     */
    public HeapMemoryGarbage getHeapMemoryGarbage() throws Exception {
        ObjectName garbageCollector = new ObjectName(GARBAGE_COLLECTOR);
        Set<ObjectName> s = mbsc.queryNames(garbageCollector, null);
        HeapMemoryGarbage heapMemoryGarbage = new HeapMemoryGarbage();
        for (ObjectName obj : s) {
            if (obj.getKeyProperty("name").contains("MarkSweep")) {
                ObjectName oldObjName = new ObjectName(obj.getCanonicalName());
                heapMemoryGarbage.setOldCollectionCount(Long.parseLong(String.valueOf(mbsc.getAttribute(oldObjName, "CollectionCount"))));
                heapMemoryGarbage.setOldCollectionTime(Long.parseLong(String.valueOf(mbsc.getAttribute(oldObjName, "CollectionTime"))));
            } else {
                ObjectName youngObjName = new ObjectName(obj.getCanonicalName());
                heapMemoryGarbage.setYoungCollectionCount(Long.parseLong(String.valueOf(mbsc.getAttribute(youngObjName, "CollectionCount"))));
                heapMemoryGarbage.setYoungCollectionTime(Long.parseLong(String.valueOf(mbsc.getAttribute(youngObjName, "CollectionTime"))));
            }
        }
        return heapMemoryGarbage;
    }

    /**
     * 获取内存每个区比例
     *
     * @return
     * @throws Exception
     */
    public MemorySpaceBean getMemorySpaceInfo() throws Exception {
        MemorySpaceBean memorySpaceInfo = new MemorySpaceBean();
        ObjectName garbageCollector = new ObjectName(MEM);
        Set<ObjectName> s = mbsc.queryNames(garbageCollector, null);
        for (ObjectName obj : s) {
            if (obj.getKeyProperty("name").contains(MEM_EDEN)) {
                ObjectName edenObjName = new ObjectName(obj.getCanonicalName());
                MemoryUsage edenGenUsage = MemoryUsage.from((CompositeDataSupport) mbsc.getAttribute(edenObjName, "Usage"));
                memorySpaceInfo.setEdenInit(edenGenUsage.getInit());
                memorySpaceInfo.setEdenCommitted(edenGenUsage.getCommitted());
                memorySpaceInfo.setEdenUsed(edenGenUsage.getUsed());
                memorySpaceInfo.setEdenMax(edenGenUsage.getMax());
            } else if (obj.getKeyProperty("name").contains(MEM_OLD)) {
                ObjectName oldObjName = new ObjectName(obj.getCanonicalName());
                MemoryUsage oldGenUsage = MemoryUsage.from((CompositeDataSupport) mbsc.getAttribute(oldObjName, "Usage"));
                memorySpaceInfo.setOldInit(oldGenUsage.getInit());
                memorySpaceInfo.setOldCommitted(oldGenUsage.getCommitted());
                memorySpaceInfo.setOldUsed(oldGenUsage.getUsed());
                memorySpaceInfo.setOldMax(oldGenUsage.getMax());

            } else if (obj.getKeyProperty("name").contains(MEM_SURVIVOR)) {
                ObjectName survivorObjName = new ObjectName(obj.getCanonicalName());
                MemoryUsage survivorGenUsage = MemoryUsage.from((CompositeDataSupport) mbsc.getAttribute(survivorObjName, "Usage"));
                memorySpaceInfo.setSurvivorInit(survivorGenUsage.getInit());
                memorySpaceInfo.setSurvivorCommitted(survivorGenUsage.getCommitted());
                memorySpaceInfo.setSurvivorUsed(survivorGenUsage.getUsed());
                memorySpaceInfo.setSurvivorMax(survivorGenUsage.getMax());

            } else if (obj.getKeyProperty("name").contains(MEM_PERM)) {
                ObjectName permObjName = new ObjectName(obj.getCanonicalName());
                MemoryUsage permGenUsage = MemoryUsage.from((CompositeDataSupport) mbsc.getAttribute(permObjName, "Usage"));
                memorySpaceInfo.setPermInit(permGenUsage.getInit());
                memorySpaceInfo.setPermCommitted(permGenUsage.getCommitted());
                memorySpaceInfo.setPermUsed(permGenUsage.getUsed());
                memorySpaceInfo.setPermMax(permGenUsage.getMax());
            }
        }
        return memorySpaceInfo;
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
        Set<ObjectName> s = mbsc.queryNames(tomcatObjName, null);
        for (ObjectName obj : s) {
            TomcatThreadBean tomcatRuntimeBean = new TomcatThreadBean();
            if (StringUtils.isNotEmpty(validateKey) && obj.getKeyProperty("name").contains(validateKey)) {
                ObjectName objname = new ObjectName(obj.getCanonicalName());
                tomcatRuntimeBean.setName(obj.getKeyProperty("name"));
                tomcatRuntimeBean.setCurrentThreadCount(Long.parseLong(String.valueOf(mbsc.getAttribute(objname, "currentThreadCount"))));
                tomcatRuntimeBean.setCurrentThreadsBusy(Long.parseLong(String.valueOf(mbsc.getAttribute(objname, "currentThreadsBusy"))));
                tomcatRuntimeBean.setMaxThreads(Long.parseLong(String.valueOf(mbsc.getAttribute(objname, "maxThreads"))));
                Boolean runFlag = Boolean.parseBoolean(String.valueOf(mbsc.getAttribute(objname, "running")));
                if (runFlag != null && runFlag) {
                    tomcatRuntimeBean.setStatus(TomcatStatus.RUNNING.getCode());//运行
                } else {
                    tomcatRuntimeBean.setStatus(TomcatStatus.STOP.getCode());//停止
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
        Set<ObjectName> s = mbsc.queryNames(tomcatObjName, null);
        for (ObjectName obj : s) {
            TomcatSessionBean quartzSessionBean = new TomcatSessionBean();
            if (StringUtils.isNotEmpty(validateKey) && obj.getKeyProperty("path").contains(validateKey)) {
                quartzSessionBean.setFrameworkName(obj.getKeyProperty("path"));
                ObjectName objname = new ObjectName(obj.getCanonicalName());
                quartzSessionBean.setActiveSessions(Long.parseLong(String.valueOf(mbsc.getAttribute(objname, "activeSessions"))));
                quartzSessionBean.setMaxActiveSessions(Long.parseLong(String.valueOf(mbsc.getAttribute(objname, "maxActiveSessions"))));
                quartzSessionBean.setSessionCounter(Long.parseLong(String.valueOf(mbsc.getAttribute(objname, "sessionCounter"))));
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
        Set<ObjectName> s = mbsc.queryNames(tomcatObjName, null);
        for (ObjectName obj : s) {
            QuartzRuntimeBean quartzRuntimeBean = new QuartzRuntimeBean();
            ObjectName objname = new ObjectName(obj.getCanonicalName());
            quartzRuntimeBean.setSchedulerInstanceId(String.valueOf(mbsc.getAttribute(objname, "SchedulerInstanceId")));
            quartzRuntimeBean.setSchedulerName(String.valueOf(mbsc.getAttribute(objname, "SchedulerName")));
            quartzRuntimeBean.setThreadPoolClassName(String.valueOf(mbsc.getAttribute(objname, "ThreadPoolClassName")));
            quartzRuntimeBean.setThreadPoolSize(Integer.parseInt(String.valueOf(mbsc.getAttribute(objname, "ThreadPoolSize"))));
            quartzRuntimeBean.setVersion(String.valueOf(mbsc.getAttribute(objname, "Version")));
            Boolean runFlag = Boolean.parseBoolean(String.valueOf(mbsc.getAttribute(objname, "Started")));
            if (runFlag != null && runFlag) {
                quartzRuntimeBean.setStatus(TomcatStatus.RUNNING.getCode());
            } else {
                quartzRuntimeBean.setStatus(TomcatStatus.STOP.getCode());//停止
            }
            quartzRuntimeBeans.add(quartzRuntimeBean);
        }
        return quartzRuntimeBeans;
    }

    /***
     * quartz远程重启
     *
     * @param schedulerName
     * @param schedulerInstanceId
     * @throws Exception
     *
     */
    public void restart(String keyContent, String schedulerName, String schedulerInstanceId) throws Exception {
        ObjectName tomcatObjName = new ObjectName(keyContent);
        Set<ObjectName> s = mbsc.queryNames(tomcatObjName, null);
        for (ObjectName obj : s) {
            ObjectName objName = new ObjectName(obj.getCanonicalName());
            String name = String.valueOf(mbsc.getAttribute(objName, "SchedulerName"));
            String scheduleId = String.valueOf(mbsc.getAttribute(objName, "SchedulerInstanceId"));
            if (schedulerName.equals(name) && scheduleId.equals(schedulerInstanceId)) {
                QuartzSchedulerMBean quartzSchedulerMBean = MBeanServerInvocationHandler.newProxyInstance(mbsc, objName, QuartzSchedulerMBean.class, false);
                quartzSchedulerMBean.shutdown();
                quartzSchedulerMBean.start();
                break;
            }
        }
    }

    public String getMonitorURL() {
        return monitorURL;
    }


    public void setMonitorURL(String monitorURL) {
        this.monitorURL = monitorURL;
    }


    public MBeanServerConnection getMbsc() {
        return mbsc;
    }


    public void setMbsc(MBeanServerConnection mbsc) {
        this.mbsc = mbsc;
    }

}
