package com.zicongcai.core;

import com.zicongcai.thirdparty.quartz.model.SimpleScheduleJob;
import com.zicongcai.thirdparty.quartz.service.IScheduleService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * 心跳检测定时任务调度器
 */
@Service("heartbeatScheduler")
public class HeartbeatScheduler {

    @Resource(name = "scheduleService")
    private IScheduleService scheduleService;

    @PostConstruct
    public void init() {

        // 新建心跳检测定时任务
        SimpleScheduleJob job = new SimpleScheduleJob("HEARTBEATJOB", "HEARTBEAT", HeartbeatJob.class);

        // 设置任务定时执行间隔为3分钟
        job.setInterval(3 * 60);

        // 设置任务的总调用次数
        job.setTotalCount(0);

        // 向系统的任务调度服务添加该任务
        scheduleService.addJob(job);
    }
}

