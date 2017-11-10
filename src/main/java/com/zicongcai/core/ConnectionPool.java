package com.zicongcai.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.Socket;

/**
 * 客户端连接池
 */
public class ConnectionPool {

    private static final Log log = LogFactory.getLog(ConnectionPool.class);

    private static ConnectionPool instance;

    // TODO: 应修改为从配置文件读取
    /**
     * 默认最大连接数
     */
    private static final int MAXIMUM = 50;

    /**
     * 客户端连接数组
     */
    private Connection[] connArray = new Connection[MAXIMUM];

    /**
     * 不允许外部类使用构造方法获取该类的实例
     */
    private ConnectionPool() {
        super();
    }

    /**
     * 单例模式，获取连接池实例
     *
     * @return 连接池实例
     */
    public static ConnectionPool getInstance() {
        if (null != instance) {
            return instance;
        } else {
            synchronized (ConnectionPool.class) {
                if (null == instance) {
                    instance = new ConnectionPool();
                    return instance;
                } else {
                    return instance;
                }
            }
        }
    }

    /**
     * 获取一个可用连接
     */
    public Connection get(Socket socket) {

        // 遍历连接数组，寻找空闲的连接
        for (int i = 0; i < MAXIMUM; i++) {
            if (connArray[i] == null || !connArray[i].isInUse()) {
                connArray[i] = new Connection(socket);
                return connArray[i];
            } else {
                continue;
            }
        }

        // 如果遍历完数组后无可用连接，打印警告信息
        log.error("客户端连接已达最大限额，不再响应新的连接！");

        return null;
    }

    /**
     * 根据玩家ID获取相应连接
     */
    public Connection get(String playerId) {

        for (Connection conn : connArray) {
            if (conn == null || !conn.isInUse() || conn.getPlayer() == null) {
                continue;
            } else if (conn.getPlayer().getId().equals(playerId)) {
                return conn;
            }
        }

        return null;
    }
}
