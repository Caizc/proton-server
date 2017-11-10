package com.zicongcai.thirdparty.quartz.model;

import org.quartz.Job;

/**
 * 使用Cron表达式的调度任务
 */
public class CronScheduleJob extends AbstractScheduleJob {

    /**
     * Cron表达式
     */
    private String cronExpression;

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    /**
     * 构造方法
     *
     * @param name     任务名称
     * @param group    任务分组
     * @param jobClass 任务具体执行类
     */
    public CronScheduleJob(String name, String group, Class<? extends Job> jobClass) {
        super(name, group, jobClass);
    }
}
