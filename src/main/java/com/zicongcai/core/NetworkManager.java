package com.zicongcai.core;

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
     * 不允许外部类使用构造方法获取该类的实例
     */
    private NetworkManager() {
        super();
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
}

/**
 * Socket 监听器（内部类）
 */
class SocketListener implements Callable<Object> {

    // TODO: 服务端 Socket 监听的 IP 和端口
    //    private static final String ip = "192.168.1.187";
    private static final int port = 9527;

    private ServerSocket serverSocket = null;

    @Override
    public Boolean call() throws Exception {

        serverSocket = new ServerSocket(port);

        while (true) {

            // 持续监听并接收客户端 Socket 连接
            Socket socket = serverSocket.accept();

            // 在新的线程中处理客户端 Socket 连接
            Executor.getInstance().submit(new SocketHandler(socket));
        }
    }
}