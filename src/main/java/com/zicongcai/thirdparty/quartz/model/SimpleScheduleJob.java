package com.zicongcai.thirdparty.quartz.model;

import org.quartz.Job;

/**
 * 简单调度任务
 */
public class SimpleScheduleJob extends AbstractScheduleJob {

    /**
     * 总共执行次数
     */
    private int totalCount = 0;

    /**
     * 执行间隔，以秒为单位
     */
    private int interval = 30;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    /**
     * 简单调度任务
     *
     * @param name     任务名称
     * @param group    任务分组
     * @param jobClass 任务具体执行类
     */
    public SimpleScheduleJob(String name, String group, Class<? extends Job> jobClass) {
        super(name, group, jobClass);
    }
}
