package com.zicongcai.core;

import com.zicongcai.Main;
import com.zicongcai.thirdparty.SpringContextHolder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTimeUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 心跳检测定时任务
 */
public class HeartbeatJob implements Job {

    private static final Log log = LogFactory.getLog(com.zicongcai.thirdparty.quartz.demo.DemoJob.class);

    /**
     * 连接的最大心跳间隔（默认为3分钟）
     */
    private static final long MAXIMUM_HEARTBEAT_INTERVAL = 180000L;

    /**
     * 最大客户端连接数
     */
    private int maxConnectionCount;

    /**
     * 构造方法
     */
    public HeartbeatJob() {
        super();

        ServerConfig serverConfig = SpringContextHolder.getBean("serverConfig");
        this.maxConnectionCount = serverConfig.maxConnectionCount;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        // 如果应用未就绪，不执行该任务
        if (!Main.isReady) {
            return;
        }

        log.info("[心跳检测] 定时任务执行中...");

        for (int i = 0; i < maxConnectionCount; i++) {

            Connection conn = ConnectionPool.getInstance().get(i);

            if (conn == null || !conn.isInUse()) {
                continue;
            }

            // 如果客户端连接上报的最后一次心跳时间已超过最大心跳间隔，则关闭该连接
            if (DateTimeUtils.currentTimeMillis() - MAXIMUM_HEARTBEAT_INTERVAL > conn.getLastTickTime()) {

                log.info("[心跳超时] 关闭连接: " + conn.getClientName());

                synchronized (conn) {
                    conn.close();
                }
            }
        }

        log.info("[心跳检测] 定时任务执行完毕");
    }
}
