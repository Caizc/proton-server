package com.zicongcai.thirdparty.quartz.demo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Demo定时任务
 */
public class DemoJob implements Job {

    private static final Log log = LogFactory.getLog(DemoJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        log.info("[Demo]:Demo定时任务正在执行中...");
    }
}
