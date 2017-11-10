package com.zicongcai.thirdparty.quartz.job;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * quartz任务工厂
 */
@DisallowConcurrentExecution
public class QuartzJobFactory implements Job {

    protected final Log log = LogFactory.getLog(this.getClass());

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        log.info(context.getJobDetail().getKey().getName()
                + "-----------------------------------------" + Thread.currentThread().getName());
        log.info(context.getJobDetail().getKey().getName()
                + "-----------------------------------------" + context.getRefireCount());

    }
}
