package org.codwh.redis.monitor.service.util;

import org.codwh.common.exception.DataPersistentException;
import org.codwh.common.util.SpringUtils;
import org.codwh.redis.custom.model.system.SystemTrackingInfo;
import org.codwh.redis.custom.model.system.SystemTrackingLog;
import org.codwh.redis.monitor.service.util.quartz.IQuartzFramework;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;
import java.util.List;

public class ConcurrentExecutionJobQuartz extends QuartzJobBean {

    private static Logger logger = LoggerFactory.getLogger(ConcurrentExecutionJobQuartz.class);

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        Date triggerTime = new Date();
        JobKey jobKey = context.getTrigger().getJobKey();
        Integer jobGroup = Integer.parseInt(jobKey.getGroup());
        String jobName = jobKey.getName();
        SystemTrackingInfo jobInfo = new SystemTrackingInfo();

        try {
            jobInfo.setAddTime(triggerTime);
            jobInfo.setJobName(jobName);
            jobInfo.setJobGroup(jobGroup);
            List<SystemTrackingInfo> jobInfos = DynamicSchedulerUtils.systemTrackingInfoService.queryList(jobInfo);
            if (jobInfos != null && jobInfos.size() > 0) {
                jobInfo = jobInfos.get(0);
                Date handlerTime = new Date();
                if (jobInfo.getJobClass() == 2) { //串行
                    StringBuilder builder = new StringBuilder();
                    builder.append(jobGroup).append("_").append(jobName);
                    DistributedLock distributedLock = new RedisDistributedLock(builder.toString(), 30); //5s超时
                    if (distributedLock.tryLock()) {
                        try {
                            IQuartzFramework quartzFramework = (IQuartzFramework) SpringUtils.getBean(jobInfo.getClassName());
                            String res = quartzFramework.invoke(jobInfo.getAlarmThreshold());
                            createLog(triggerTime, handlerTime, "成功", "调度成功", res, res, jobInfo);
                        } finally {
                            distributedLock.unlock();
                        }
                    }
                } else {
                    IQuartzFramework quartzFramework = (IQuartzFramework) SpringUtils.getBean(jobInfo.getClassName());
                    String res = quartzFramework.invoke(jobInfo.getAlarmThreshold());
                    createLog(triggerTime, handlerTime, "成功", "调度成功", res, res, jobInfo);
                }
            }
        } catch (SchedulerException e) {
            logger.error("execute ConcurrentExecutionJobQuartz failed: {}", e.getLocalizedMessage());
            createLog(triggerTime, triggerTime, "失败", "调度失败", "执行失败", e.getMessage(), jobInfo);
        } catch (DataPersistentException ee) {
            logger.error("execute ConcurrentExecutionJobQuartz failed: {}", ee.getMessage());
            createLog(triggerTime, triggerTime, "失败", "调度失败", "执行失败", ee.getMessage(), jobInfo);
        } catch (Exception e) {
            logger.error("获取分布式锁失败: {}", e.getMessage());
            createLog(triggerTime, triggerTime, "失败", "调度失败", "执行失败", e.getMessage(), jobInfo);
        }
    }

    private static void createLog(Date triggerTime, Date handleTime, String status, String handlerStatus,
                                  String handlerResult, String message, SystemTrackingInfo jobInfo) {
        try {
            SystemTrackingLog log = new SystemTrackingLog();

            log.setTriggerTime(triggerTime);
            log.setTriggerStatus(status);
            log.setTriggerMsg(message);

            log.setHandleStatus(handlerStatus);
            log.setHandleMsg(handlerResult);
            log.setHandleTime(handleTime);

            log.setJobClass(jobInfo.getJobClass());
            log.setJobClassName(jobInfo.getJobClassName());
            log.setJobCron(jobInfo.getJobCron());
            log.setJobData(" ");
            log.setJobDesc(jobInfo.getJobDesc());
            log.setJobGroup(jobInfo.getJobGroup());
            log.setJobName(jobInfo.getJobName());

            DynamicSchedulerUtils.systemTrackingLogService.create(log);
        } catch (Exception e) {
            logger.error("createLog失败: {}", e.getMessage());
        }
    }
}
