package com.zicongcai.util;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 多线程执行器
 */
public class Executor {

    private final Log log = LogFactory.getLog(this.getClass());

    private static Executor executor;

    /**
     * 最大线程数（初始化为50）
     */
    private static final int nThreads = 50;

    private ExecutorService executorService = Executors.newFixedThreadPool(nThreads);

    /**
     * 不允许使用构造方法新建该类的对象
     */
    private Executor() {
        super();
    }

    /**
     * 获取多线程执行器实例
     *
     * @return 多线程执行器实例
     */
    public static Executor getInstance() {
        if (null != executor) {
            return executor;
        } else {
            synchronized (Executor.class) {
                if (null == executor) {
                    executor = new Executor();
                    return executor;
                } else {
                    return executor;
                }
            }
        }
    }

    /**
     * 提交待查询的任务
     *
     * @param task 待执行的任务
     * @return Task Result
     */
    public Future<Object> submit(Callable<Object> task) {

        showThreadPoolInfo();

        return executorService.submit(task);
    }

    /**
     * 打印线程池信息
     */
    public void showThreadPoolInfo() {
        int maximumPoolSize = ((ThreadPoolExecutor) executorService).getMaximumPoolSize();
        int activeCount = ((ThreadPoolExecutor) executorService).getActiveCount();
        int waitCount = ((ThreadPoolExecutor) executorService).getQueue().size();

        StringBuilder sb = new StringBuilder();
        sb.append("[System]:").append("\n");
        sb.append("=== 线程池信息 ===").append("\n");
        sb.append(String.format("最大线程数：[%s]", maximumPoolSize)).append("\n");
        sb.append(String.format("活动线程数：[%s]", activeCount)).append("\n");
        sb.append(String.format("等待线程数：[%s]", waitCount)).append("\n");
        sb.append("================");
        log.info(sb.toString());

        if (waitCount > 0) {
            log.warn("注意：线程资源即将耗尽！");
        }
    }
}

