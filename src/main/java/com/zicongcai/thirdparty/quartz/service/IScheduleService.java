package com.zicongcai.thirdparty.quartz.service;

import java.util.Map;

import com.zicongcai.thirdparty.quartz.model.IScheduleJob;
import org.quartz.JobListener;

/**
 * 任务调度服务接口类
 */
public interface IScheduleService {

    /**
     * 增加一个调度任务
     *
     * @param job 调度任务
     */
    public void addJob(IScheduleJob job);

    /**
     * 增加一个调度任务
     *
     * @param job  调度任务
     * @param data 传送给任务的数据
     */
    public void addJob(IScheduleJob job, @SuppressWarnings("rawtypes") Map data);

    /**
     * 根据任务名称和分组删除调度任务
     *
     * @param name  任务名称
     * @param group 任务分组
     */
    public void removeJob(String name, String group);

    /**
     * 删除一个调度任务
     *
     * @param job 调度任务
     */
    public void removeJob(IScheduleJob job);

    /**
     * 修改一个调度任务
     *
     * @param job 调度任务
     */
    public void updateJob(IScheduleJob job);

    /**
     * 在调度任务上增加监听器
     *
     * @param job      调度任务
     * @param listener 任务监听器
     */
    public void addJobListener(IScheduleJob job, JobListener listener);

    /**
     * 获取JOB信息
     */
    public String getAllJobInfo();

}
