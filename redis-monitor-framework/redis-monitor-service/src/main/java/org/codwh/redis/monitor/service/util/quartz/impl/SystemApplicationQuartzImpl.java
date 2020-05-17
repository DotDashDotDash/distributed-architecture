package org.codwh.redis.monitor.service.util.quartz.impl;

import org.codwh.common.util.JVM.*;
import org.codwh.redis.custom.dao.ISystemApplicationDao;
import org.codwh.redis.custom.dao.ISystemApplicationMonitorDao;
import org.codwh.redis.custom.dao.ISystemComponentDao;
import org.codwh.redis.custom.model.system.SystemApplication;
import org.codwh.redis.custom.model.system.SystemApplicationMonitor;
import org.codwh.redis.custom.model.system.SystemComponent;
import org.codwh.redis.monitor.service.constants.FinalStatusType;
import org.codwh.redis.monitor.service.constants.MonitorModuleType;
import org.codwh.redis.monitor.service.constants.OpSystemStatusType;
import org.codwh.redis.monitor.service.constants.TomcatStatusType;
import org.codwh.redis.monitor.service.util.quartz.IQuartzFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service("systemApplicationQuartz")
public class SystemApplicationQuartzImpl implements IQuartzFramework {

    private Logger logger = LoggerFactory.getLogger(SystemApplicationQuartzImpl.class);

    @Autowired
    private ISystemApplicationDao systemApplicationDao;

    @Autowired
    private ISystemApplicationMonitorDao systemApplicationMonitorDao;

    @Autowired
    private ISystemComponentDao systemComponentDao;

    @Override
    public String invoke(int times) {
        try {
            List<SystemApplication> applications = systemApplicationDao.getListByStatus(1);
            if (applications == null || applications.size() == 0) {
                return "执行成功";
            }
            for (SystemApplication systemApplication : applications) {
                systemApplication.setApplicationStatus(OpSystemStatusType.DONE.getCode());//已经完成
                int count = systemApplicationDao.updateVersionByIdAndVersion(systemApplication);
                if (count > 0) {
                    try {
                        systemApplication.setVersion(systemApplication.getVersion() + 1);//版本号加1
                        MonitorJVM monitorJVM = getMonitorJVM(systemApplication, times, 0);
                        if (monitorJVM.getMbsc() == null) {//无法连接jmx，报警
                            systemApplication.setJmxStatus(FinalStatusType.FAIL.getCode());//成功
                            createSystemApplicationMonitor(systemApplication, MonitorModuleType.JMX.getCode());
                        } else {
                            monitorCPU(monitorJVM, systemApplication);//监控cpu
                            monitorMemrory(monitorJVM, systemApplication);//监控jvm
                            monitorTomcat(monitorJVM, systemApplication);//监控tomcat
                            monitorQuartz(monitorJVM, systemApplication);//监控quartz
                            monitorJVMThread(monitorJVM, systemApplication);//监控 jvm thread
                            systemApplication.setJmxStatus(FinalStatusType.SUCCESS.getCode());//成功
                        }
                        if (monitorJVM.getMbsc() != null) {
                            monitorJVM.close();
                        }

                    } finally {
                        systemApplication.setApplicationStatus(OpSystemStatusType.DOING.getCode());//设置为未完成,等待下一次监控
                        systemApplicationDao.updateVersionByIdAndVersion(systemApplication);
                    }


                }
            }
        } catch (Exception e) {
            return "执行失败,失败信息:" + e.getMessage() + ",原因:" + e.getCause();
        }
        return "执行成功";
    }

    /**
     * 监控cpu
     *
     * @throws Exception
     */
    private void monitorCPU(MonitorJVM monitorJVM, SystemApplication systemApplication) throws Exception {
        Long start = System.currentTimeMillis();
        CPUBean cPUBean = monitorJVM.getProcessCpuTime();
        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (Exception e) {
            logger.error("TimeUnit.SECONDS.sleep error", e);
        }
        Long end = System.currentTimeMillis();
        CPUBean cPUBean1 = monitorJVM.getProcessCpuTime();
        double ratio = (cPUBean1.getProcessCpuTime() - cPUBean.getProcessCpuTime()) / 1000000.0 / (end - start) / cPUBean.getAvailableProcessors();
        if (ratio >= 1) {
            createSystemApplicationMonitor(systemApplication, MonitorModuleType.CPU.getCode());
        }
    }

    /**
     * 监控jvm
     *
     * @param monitorJVM
     * @param systemApplication
     * @throws Exception
     */
    private void monitorMemrory(MonitorJVM monitorJVM, SystemApplication systemApplication) throws Exception {

        MemorySpaceBean memrorySpaceBean = monitorJVM.getMemorySpaceInfo();//查询老年代比例
        double oldRatio = memrorySpaceBean.getOldUsed() * 100 / memrorySpaceBean.getOldCommitted();
        if (oldRatio >= 100) {//老年代内存溢出
            createSystemApplicationMonitor(systemApplication, MonitorModuleType.OLD.getCode());
        }
        double permRatio = memrorySpaceBean.getPermUsed() * 100 / memrorySpaceBean.getPermCommitted();
        if (permRatio >= 100) {//持久代内存溢出
            createSystemApplicationMonitor(systemApplication, MonitorModuleType.PERM.getCode());
        }
    }

    /**
     * 监控jvm线程
     *
     * @param monitorJVM
     * @param systemApplication
     * @throws Exception
     */
    private void monitorJVMThread(MonitorJVM monitorJVM, SystemApplication systemApplication) throws Exception {
        ThreadMXBean threadMXBean = monitorJVM.getThreadMXBean();
        if (threadMXBean.findDeadlockedThreads() != null && threadMXBean.findDeadlockedThreads().length > 0) {
            createSystemApplicationMonitor(systemApplication, MonitorModuleType.THREAD.getCode());
        }
    }

    /**
     * 监控tomcat
     *
     * @param monitorJVM
     * @param systemApplication
     * @throws Exception
     */
    private void monitorTomcat(MonitorJVM monitorJVM, SystemApplication systemApplication) throws Exception {
        String keyWord = "";
        List<SystemComponent> systemComponents = systemComponentDao.getListByApplicationId(systemApplication.getId());
        if (systemComponents == null || systemComponents.size() == 0) {
            return;
        }
        for (SystemComponent systemComponent : systemComponents) {
            if (systemComponent.getKeyConfig().contains(MonitorJVM.TOMCAT_THREAD_POOL)) {
                keyWord = systemComponent.getKeyConfig();
                break;
            }
        }
        List<TomcatThreadBean> threadBeans = monitorJVM.getTomcatRuntimeBeans(keyWord, String.valueOf(systemApplication.getApplicationPort()));
        TomcatThreadBean tomcatThreadBean = threadBeans.get(0);
        if (tomcatThreadBean.getCurrentThreadCount() > tomcatThreadBean.getMaxThreads()) {
            createSystemApplicationMonitor(systemApplication, MonitorModuleType.TOMCAT.getCode());
        }
    }

    /**
     * 监控quartz
     *
     * @param monitorJVM
     * @param systemApplication
     * @throws Exception
     */
    private void monitorQuartz(MonitorJVM monitorJVM, SystemApplication systemApplication) throws Exception {
        List<String> keyWords = new ArrayList<String>();
        List<SystemComponent> systemComponents = systemComponentDao.getListByApplicationId(systemApplication.getId());
        for (SystemComponent systemComponent : systemComponents) {
            if (systemComponent.getKeyConfig().contains("quartz")) {
                keyWords.add(systemComponent.getKeyConfig());
            }
        }
        List<QuartzRuntimeBean> quartzRuntimeBeans = new ArrayList<QuartzRuntimeBean>();
        for (String keyword : keyWords) {
            List<QuartzRuntimeBean> runtimeBeans = monitorJVM.getQuartzRuntimeBeans(keyword);
            quartzRuntimeBeans.addAll(runtimeBeans);
        }
        for (QuartzRuntimeBean runtimeBean : quartzRuntimeBeans) {
            if (runtimeBean.getStatus() == TomcatStatusType.STOP.getCode()) {
                createSystemApplicationMonitor(systemApplication, MonitorModuleType.QUARTZ.getCode());
            }
        }
    }

    /**
     * 创建监控日志
     *
     * @param systemApplication
     */
    private void createSystemApplicationMonitor(SystemApplication systemApplication, Integer monitorMoudle) {
        SystemApplicationMonitor systemApplicationMonitor = new SystemApplicationMonitor();
        systemApplicationMonitor.setCreateTime(new Date());
        systemApplicationMonitor.setMonitorName(systemApplication.getApplicationName());
        systemApplicationMonitor.setMonitorHost(systemApplication.getJmxHost());
        systemApplicationMonitor.setMonitorModule(monitorMoudle);
        systemApplicationMonitor.setMonitorPort(systemApplication.getJmxPort());
        systemApplicationMonitor.setMonitorStatus(FinalStatusType.FAIL.getCode());
        systemApplicationMonitorDao.insert(systemApplicationMonitor);
    }

    /**
     * 获取jmx连接
     *
     * @param systemApplication
     * @param times
     * @param count
     * @return
     */
    private MonitorJVM getMonitorJVM(SystemApplication systemApplication, int times, int count) {
        MonitorJVM monitorJVM = null;
        try {
            monitorJVM = new MonitorJVM(systemApplication.getJmxHost(), systemApplication.getJmxPort(), systemApplication.getJmxUserName(), systemApplication.getJmxPassword());
            return monitorJVM;
        } catch (Exception e) {
            if (count < times) {
                count = count + 1;
                return getMonitorJVM(systemApplication, times, count);
            }
            return null;
        }
    }
}
