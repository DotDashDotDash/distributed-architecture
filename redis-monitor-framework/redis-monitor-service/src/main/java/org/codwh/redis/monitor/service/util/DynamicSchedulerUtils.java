package org.codwh.redis.monitor.service.util;

import org.codwh.redis.custom.model.system.SystemTrackingInfo;
import org.codwh.redis.monitor.service.constants.JobType;
import org.codwh.redis.monitor.service.service.ISystemTrackingInfoService;
import org.codwh.redis.monitor.service.service.ISystemTrackingLogService;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.*;

public final class DynamicSchedulerUtils implements ApplicationContextAware, InitializingBean {

    static Logger logger = LoggerFactory.getLogger(DynamicSchedulerUtils.class);

    private static Scheduler scheduler;
    public static ISystemTrackingInfoService systemTrackingInfoService;
    public static ISystemTrackingLogService systemTrackingLogService;

    /**
     * 获取任务列表
     *
     * @return
     */
    @Deprecated
    public static List<Map<String, Object>> getJobList() {
        List<Map<String, Object>> jobList = new ArrayList<Map<String, Object>>();

        try {
            if (scheduler.getJobGroupNames() == null || scheduler.getJobGroupNames().size() == 0) {
                return null;
            }
            String groupName = scheduler.getJobGroupNames().get(0);
            Set<JobKey> jobKeys = scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName));
            if (jobKeys != null && jobKeys.size() > 0) {
                for (JobKey jobKey : jobKeys) {
                    TriggerKey triggerKey = TriggerKey.triggerKey(jobKey.getName(), Scheduler.DEFAULT_GROUP);
                    Trigger trigger = scheduler.getTrigger(triggerKey);
                    JobDetail jobDetail = scheduler.getJobDetail(jobKey);
                    Trigger.TriggerState triggerState = scheduler.getTriggerState(triggerKey);
                    Map<String, Object> jobMap = new HashMap<String, Object>();
                    jobMap.put("TriggerKey", trigger);
                    jobMap.put("Trigger", trigger);
                    jobMap.put("JobDetail", jobDetail);
                    jobMap.put("TriggerState", triggerState);
                    jobList.add(jobMap);
                }
            }
        } catch (SchedulerException e) {
            logger.error("获取job列表失败: {}", e.getLocalizedMessage());
            return null;
        }
        return jobList;
    }

    /**
     * 填充Job细节
     *
     * @param jobInfo
     */
    public static void fillJobInfo(SystemTrackingInfo jobInfo) {
        TriggerKey triggerKey = TriggerKey.triggerKey(jobInfo.getJobName(), String.valueOf(jobInfo.getJobGroup()));
        JobKey jobKey = new JobKey(jobInfo.getJobName(), String.valueOf(jobInfo.getJobGroup()));

        try {
            Trigger trigger = scheduler.getTrigger(triggerKey);
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            Trigger.TriggerState triggerState = scheduler.getTriggerState(triggerKey);

            if (trigger != null && trigger instanceof CronTriggerImpl) {
                String cronExpression = ((CronTriggerImpl) trigger).getCronExpression();
                jobInfo.setJobCron(cronExpression);
            }
            if (jobDetail != null) {
                jobInfo.setJobClassName(JobType.getMessage(jobInfo.getJobClass()));
            }
            if (triggerState != null) {
                jobInfo.setJobStatus(triggerState.name());
            }
        } catch (SchedulerException e) {
            logger.error("填充Job内容失败!");
        }
    }

    /**
     * 检查Job是否存在
     *
     * @param jobName
     * @param jobGroup
     * @return
     * @throws SchedulerException
     */
    public static boolean checkExists(String jobName, String jobGroup) throws SchedulerException {
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
        return scheduler.checkExists(triggerKey);
    }

    /**
     * 添加新的作业调度
     *
     * @param jobInfo
     * @return
     * @throws SchedulerException
     */
    @SuppressWarnings("unchecked")
    public static boolean addJob(SystemTrackingInfo jobInfo) throws SchedulerException {
        TriggerKey triggerKey = TriggerKey.triggerKey(jobInfo.getJobName(), String.valueOf(jobInfo.getJobGroup()));
        JobKey jobKey = new JobKey(jobInfo.getJobName(), String.valueOf(jobInfo.getJobGroup()));

        if (checkExists(jobInfo.getJobName(), String.valueOf(jobInfo.getJobGroup()))) {
            return false;   //已经有对应的job了，不能继续添加了
        }
        CronScheduleBuilder cronScheduleBuilder =
                CronScheduleBuilder.cronSchedule(jobInfo.getJobCron()).withMisfireHandlingInstructionDoNothing();
        CronTrigger cronTrigger =
                TriggerBuilder.newTrigger().withIdentity(triggerKey).withSchedule(cronScheduleBuilder).build();

        Class<? extends Job> jobClass = null;
        try {
            jobClass = (Class<? extends Job>) Class.forName(JobType.getClass(jobInfo.getJobClass()));
        } catch (ClassNotFoundException e) {
            logger.error("无法加载类: {}", jobInfo.getJobClassName());
            return false;
        }

        JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobKey).build();
        scheduler.scheduleJob(jobDetail, cronTrigger);
        return true;
    }

    /**
     * 重新调度job
     *
     * @param jobInfo
     * @return
     * @throws Exception
     */
    public static boolean reScheduleJob(SystemTrackingInfo jobInfo) throws Exception {
        //检查job是否存在
        if (!checkExists(jobInfo.getJobName(), String.valueOf(jobInfo.getJobGroup()))) {
            return false;   //如果job不存在，无法重新调度
        }

        TriggerKey triggerKey = TriggerKey.triggerKey(jobInfo.getJobName(), String.valueOf(jobInfo.getJobGroup()));
        JobKey jobKey = new JobKey(jobInfo.getJobName(), String.valueOf(jobInfo.getJobGroup()));
        CronScheduleBuilder cronScheduleBuilder =
                CronScheduleBuilder.cronSchedule(jobInfo.getJobCron()).withMisfireHandlingInstructionDoNothing();
        CronTrigger cronTrigger =
                TriggerBuilder.newTrigger().withIdentity(triggerKey).withSchedule(cronScheduleBuilder).build();

        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        HashSet<Trigger> triggers = new HashSet<Trigger>();
        triggers.add(cronTrigger);
        scheduler.scheduleJob(jobDetail, triggers, true);
        return true;
    }

    /**
     * 移除job
     *
     * @param jobName
     * @param jobGroup
     * @return
     * @throws Exception
     */
    public static boolean removeJob(String jobName, String jobGroup) throws Exception {
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
        if (checkExists(jobName, jobGroup)) {
            scheduler.unscheduleJob(triggerKey);
            JobKey jobKey = new JobKey(jobName, jobGroup);
            scheduler.deleteJob(jobKey);
            return true;
        }
        return false;
    }

    /**
     * 暂停job
     *
     * @param jobName
     * @param jobGroup
     * @return
     * @throws Exception
     */
    public static boolean pauseJob(String jobName, String jobGroup) throws Exception {
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
        if (checkExists(jobName, jobGroup)) {
            scheduler.pauseTrigger(triggerKey);
            return true;
        }
        return false;
    }

    /**
     * 恢复job
     *
     * @param jobName
     * @param jobGroup
     * @return
     * @throws Exception
     */
    public static boolean resumeJob(String jobName, String jobGroup) throws Exception {
        JobKey jobKey = new JobKey(jobName, jobGroup);
        if (checkExists(jobName, jobGroup)) {
            scheduler.resumeJob(jobKey);
            return true;
        }
        return false;
    }

    /**
     * 激活job，开始执行job
     *
     * @param jobName
     * @param jobGroup
     * @return
     * @throws Exception
     */
    public static boolean activateJob(String jobName, String jobGroup) throws Exception {
        JobKey jobKey = new JobKey(jobName, jobGroup);
        if (checkExists(jobName, jobGroup)) {
            scheduler.triggerJob(jobKey);
            return true;
        }
        return false;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        DynamicSchedulerUtils.systemTrackingInfoService = applicationContext.getBean(ISystemTrackingInfoService.class);
        DynamicSchedulerUtils.systemTrackingLogService = applicationContext.getBean(ISystemTrackingLogService.class);
    }

    public static Scheduler getScheduler() {
        return scheduler;
    }

    public static void setScheduler(Scheduler scheduler) {
        DynamicSchedulerUtils.scheduler = scheduler;
    }
}
