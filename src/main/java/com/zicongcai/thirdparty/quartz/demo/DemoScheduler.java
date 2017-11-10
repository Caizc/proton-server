package com.zicongcai.thirdparty.quartz.demo;

import javax.annotation.Resource;

import com.zicongcai.thirdparty.quartz.model.SimpleScheduleJob;
import com.zicongcai.thirdparty.quartz.service.IScheduleService;
import org.springframework.stereotype.Service;


/**
 * 定时任务调度器Demo
 */
//@Service("demoScheduler")
public class DemoScheduler {

    // @Resource(name = "scheduleService")
    private IScheduleService scheduleService;

    // @PostConstruct
    public void init() {

        // 新建Demo定时任务
        SimpleScheduleJob job = new SimpleScheduleJob("DEMOJOB", "DEMO", DemoJob.class);

        // 设置任务定时执行间隔为5分钟
        job.setInterval(5 * 60);

        // 设置任务的总调用次数
        job.setTotalCount(0);

        // 向系统的任务调度服务添加该任务
        scheduleService.addJob(job);
    }
}
