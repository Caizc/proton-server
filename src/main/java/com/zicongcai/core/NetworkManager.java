package com.zicongcai.core;

import com.zicongcai.thirdparty.SpringContextHolder;
import com.zicongcai.util.Executor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Callable;

/**
 * 网络管理器
 */
public class NetworkManager {

    private static final Log log = LogFactory.getLog(NetworkManager.class);

    private static NetworkManager instance;

    /**
     * 最大客户端连接数
     */
    private int maxConnectionCount;

    /**
     * 不允许外部类使用构造方法获取该类的实例
     */
    private NetworkManager() {
        super();

        ServerConfig serverConfig = SpringContextHolder.getBean("serverConfig");
        this.maxConnectionCount = serverConfig.maxConnectionCount;
    }

    /**
     * 单例模式，获取网络管理器实例
     *
     * @return 网络管理器实例
     */
    public static NetworkManager getInstance() {
        if (null != instance) {
            return instance;
        } else {
            synchronized (NetworkManager.class) {
                if (null == instance) {
                    instance = new NetworkManager();
                    return instance;
                } else {
                    return instance;
                }
            }
        }
    }

    /**
     * 启动服务端的 Socket 监听器
     */
    public void start() {
        Executor.getInstance().submit(new SocketListener());
    }

    /**
     * 向客户端发送数据包
     *
     * @param conn  客户端连接
     * @param proto 消息协议包
     */
    public void sendData(Connection conn, Protocol proto) {

        // FIXME: 无法向客户端发送应答数据时，相应事务应回滚

        if (conn.getSocket().isClosed()) {
            log.error("客户端 " + conn.getClientName() + " 的Socket连接已关闭，无法发送数据！");
        }

        // 打包协议消息包
        byte[] data = proto.pack();

        try {
            conn.getDataOutputStream().write(data, 0, data.length);
            conn.getDataOutputStream().flush();
        } catch (Exception e) {
            log.error("向 " + conn.getClientName() + " 发送数据过程出错！", e);
        }
    }

    /**
     * 向所有客户端广播消息
     *
     * @param proto 消息协议包
     */
    public void broadcast(Protocol proto) {

        if (proto == null) {
            return;
        }

        for (int i = 0; i < maxConnectionCount; i++) {

            Connection conn = ConnectionPool.getInstance().get(i);

            if (conn == null || !conn.isInUse() || conn.getPlayer() == null) {
                continue;
            }

            sendData(conn, proto);
        }

        log.info("向所有客户端广播消息: [" + proto.getDesc() + "]");
    }
}

/**
 * Socket 监听器（内部类）
 */
class SocketListener implements Callable<Object> {

    /**
     * Socket监听端口
     */
    private int port;

    private ServerSocket serverSocket = null;

    /**
     * 构造方法
     */
    public SocketListener() {
        super();

        ServerConfig serverConfig = SpringContextHolder.getBean("serverConfig");
        this.port = serverConfig.port;
    }

    @Override
    public Boolean call() throws Exception {

        serverSocket = new ServerSocket(port);

        while (true) {

            // 持续监听并接收客户端Socket连接
            Socket socket = serverSocket.accept();

            // 从连接池中获取一个客户端连接对象
            Connection conn = ConnectionPool.getInstance().get(socket);

            if (conn != null) {

                // 在新的线程中处理客户端连接
                Executor.getInstance().submit(new SocketHandler(conn));
            } else {
                continue;
            }
        }
    }
}