package com.zicongcai.thirdparty.quartz.service.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.zicongcai.thirdparty.quartz.job.QuartzJobFactory;
import com.zicongcai.thirdparty.quartz.model.CronScheduleJob;
import com.zicongcai.thirdparty.quartz.model.IScheduleJob;
import com.zicongcai.thirdparty.quartz.model.SimpleScheduleJob;
import com.zicongcai.thirdparty.quartz.service.IScheduleService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.JobListener;
import org.quartz.Matcher;
import org.quartz.ScheduleBuilder;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdScheduler;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.matchers.KeyMatcher;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.quartz.impl.triggers.SimpleTriggerImpl;
import org.springframework.stereotype.Service;

/**
 * 任务调度服务实现类
 */
@Service("scheduleService")
public class ScheduleServiceImpl implements IScheduleService {

    private static final Log log = LogFactory.getLog(ScheduleServiceImpl.class);

    @Resource(name = "schedulerFactoryBean")
    private StdScheduler scheduler;

    /**
     * 通过ScheduleJob对象中的信息创建ScheduleBuilder对象
     * （ScheduleBuilder对象有可能是SimpleScheduleBuilder类型或者是CronScheduleBuilder类型）
     *
     * @param job 调度任务
     * @return ScheduleBuilder
     */
    @SuppressWarnings({"rawtypes", "static-access"})
    private ScheduleBuilder getScheduleBuilder(IScheduleJob job) {

        ScheduleBuilder scheduleBuilder = null;

        if (job instanceof SimpleScheduleJob) {
            SimpleScheduleJob newJob = (SimpleScheduleJob) job;
            if (newJob.getTotalCount() > 0) {
                scheduleBuilder = SimpleScheduleBuilder.simpleSchedule().repeatSecondlyForTotalCount(
                        newJob.getTotalCount(), newJob.getInterval());
            } else {
                scheduleBuilder = SimpleScheduleBuilder.simpleSchedule().repeatSecondlyForever(newJob.getInterval());
            }
        } else {
            CronScheduleJob newJob = (CronScheduleJob) job;
            scheduleBuilder = CronScheduleBuilder.cronSchedule(newJob.getCronExpression())
                    .withMisfireHandlingInstructionDoNothing();
        }

        return scheduleBuilder;
    }

    @Override
    public void addJob(IScheduleJob job) {
        this.addJob(job, null);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void addJob(IScheduleJob job, Map data) {

        TriggerKey triggerKey = TriggerKey.triggerKey(job.getName(), job.getGroup());
        Trigger trigger;

        try {

            trigger = scheduler.getTrigger(triggerKey);

            if (null == trigger) {
                // 新增任务
                JobDetail jobdetail = JobBuilder.newJob(job.getJobClass()).withIdentity(job.getName(), job.getGroup())
                        .build();

                if (data != null) {
                    jobdetail.getJobDataMap().putAll(data);
                }

                ScheduleBuilder scheduleBuilder = this.getScheduleBuilder(job);

                if (job instanceof SimpleScheduleJob) {
                    // 默认延时10秒后再启动任务
                    trigger = TriggerBuilder.newTrigger().withIdentity(job.getName(), job.getGroup())
                            .withSchedule(scheduleBuilder).startAt(new Date(new Date().getTime() + 1000L)).build();
                } else {
                    trigger = TriggerBuilder.newTrigger().withIdentity(job.getName(), job.getGroup())
                            .withSchedule(scheduleBuilder).build();
                }

                scheduler.scheduleJob(jobdetail, trigger);

            } else {
                // 修改任务
            }

        } catch (SchedulerException e) {
            log.error(String.format("添加调度任务[%s]异常：", job.getName()), e);
        }

    }

    @Override
    public void removeJob(String name, String group) {
        JobKey jobKey = JobKey.jobKey(name, group);
        if (null != jobKey) {
            try {
                scheduler.deleteJob(jobKey);
                log.info(String.format("已删除调度任务[%s]", name));
            } catch (SchedulerException e) {
                log.error(String.format("删除调度任务[%s]异常：", name), e);
            }
        }
    }

    @Override
    public void removeJob(IScheduleJob job) {
        removeJob(job.getName(), job.getGroup());
    }

    @Override
    public void updateJob(IScheduleJob job) {
        TriggerKey triggerKey = TriggerKey.triggerKey(job.getName(), job.getGroup());
        Trigger trigger;
        try {
            trigger = scheduler.getTrigger(triggerKey);
            if (trigger instanceof SimpleTriggerImpl) {
                SimpleTriggerImpl newTrigger = (SimpleTriggerImpl) trigger;
                SimpleScheduleJob newJob = (SimpleScheduleJob) job;
                newTrigger.setRepeatCount(newJob.getTotalCount());
                newTrigger.setRepeatInterval(newJob.getInterval() * 1000L);
                scheduler.rescheduleJob(triggerKey, newTrigger);
            } else if (trigger instanceof CronTriggerImpl) {
                CronTriggerImpl newTrigger = (CronTriggerImpl) trigger;
                CronScheduleJob newJob = (CronScheduleJob) job;
                newTrigger.setCronExpression(newJob.getCronExpression());
                scheduler.rescheduleJob(triggerKey, newTrigger);
            }
        } catch (Exception e) {
            log.error(String.format("更新调度任务[%s]异常：", job.getName()), e);
        }
    }

    @Override
    public void addJobListener(IScheduleJob job, JobListener listener) {
        try {
            JobKey jobKey = JobKey.jobKey(job.getName(), job.getGroup());
            Matcher<JobKey> macher = KeyMatcher.keyEquals(jobKey);
            scheduler.getListenerManager().addJobListener(listener, macher);
        } catch (Exception e) {
            log.error(String.format("为调度任务[%s]增加监听器异常：", job.getName()), e);
        }
    }

    @Override
    public String getAllJobInfo() {
        try {
            StringBuffer sb = new StringBuffer("\njobKeys:");
            GroupMatcher<JobKey> matcher = GroupMatcher.anyGroup();
            Set<JobKey> set = scheduler.getJobKeys(matcher);
            for (Iterator<JobKey> it = set.iterator(); it.hasNext(); ) {
                JobKey jobKey = it.next();
                sb.append(" " + jobKey.getName());
            }

            sb.append("\ntriggerKeys:");

            GroupMatcher<TriggerKey> matcher1 = GroupMatcher.anyGroup();
            Set<TriggerKey> set1 = scheduler.getTriggerKeys(matcher1);
            for (Iterator<TriggerKey> it1 = set1.iterator(); it1.hasNext(); ) {
                TriggerKey triggerKey = it1.next();
                sb.append(" " + triggerKey.getName());
            }
            return sb.toString();
        } catch (SchedulerException e) {
            log.error("获取所有调度任务信息异常：", e);
        }
        return null;
    }

    @SuppressWarnings("unused")
    private void test() {
        for (int i = 0; i < 1; i++) {
            SimpleScheduleJob job = new SimpleScheduleJob("name" + i, "group", QuartzJobFactory.class);
            job.setTotalCount(10);
            job.setInterval(60);
            this.addJob(job);

            job.setTotalCount(10);
            job.setInterval(5);
            this.updateJob(job);
        }
    }
}
