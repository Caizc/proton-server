package com.zicongcai.thirdparty.quartz.model;

import org.quartz.Job;

/**
 * 调度任务抽象类
 */
public abstract class AbstractScheduleJob implements IScheduleJob {

    /**
     * 任务ID
     */
    private String id;

    /**
     * 任务名称
     */
    private String name;

    /**
     * 任务分组
     */
    private String group;

    /**
     * 任务状态：0 禁用 1 启用 2 删除
     */
    private String status;

    /**
     * 任务具体执行类
     */
    private Class<? extends Job> jobClass;

    public Class<? extends Job> getJobClass() {
        return jobClass;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getGroup() {
        return group;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 构造方法
     *
     * @param name     任务名称
     * @param group    任务分组
     * @param jobClass 任务具体执行类
     */
    public AbstractScheduleJob(String name, String group, Class<? extends Job> jobClass) {
        super();
        this.name = name;
        this.group = group;
        this.jobClass = jobClass;
    }

    @Override
    public String toString() {
        return "AbstractScheduleJob [name=" + name + ", group=" + group + ", jobClass=" + jobClass
                + "]";
    }
}
