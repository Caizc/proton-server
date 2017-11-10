package com.zicongcai.thirdparty.quartz.model;

import org.quartz.Job;

/**
 * 调度任务接口类
 */
public interface IScheduleJob {

    /**
     * 获取任务ID
     */
    String getId();

    /**
     * 获取任务名称
     */
    String getName();

    /**
     * 获取任务分组
     */
    String getGroup();

    /**
     * 获取任务状态：0 禁用 1 启用 2 删除
     */
    String getStatus();

    /**
     * 获取任务具体执行类
     */
    Class<? extends Job> getJobClass();
}
