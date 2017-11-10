package com.zicongcai.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTimeUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * 客户端连接
 */
public class Connection {

    private static final Log log = LogFactory.getLog(Connection.class);

    /**
     * 本连接是否正使用中
     */
    private boolean inUse;

    private NetworkManager networkManager;

    /**
     * Socket连接
     */
    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    /**
     * 所属玩家对象
     */
    private Player player;

    /**
     * 最后心跳时间
     */
    private long lastTickTime;

    public boolean isInUse() {
        return inUse;
    }

    public void setInUse(boolean inUse) {
        this.inUse = inUse;
    }

    public Socket getSocket() {
        return socket;
    }

    public DataInputStream getDataInputStream() {
        return dataInputStream;
    }

    public DataOutputStream getDataOutputStream() {
        return dataOutputStream;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public long getLastTickTime() {
        return lastTickTime;
    }

    public void setLastTickTime(long lastTickTime) {
        this.lastTickTime = lastTickTime;
    }

    /**
     * 构造方法
     *
     * @param socket 客户端 Socket 连接实例
     */
    public Connection(Socket socket) {

        super();

        this.inUse = true;

        this.networkManager = NetworkManager.getInstance();

        this.socket = socket;

        try {
            this.dataInputStream = new DataInputStream(socket.getInputStream());
            this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            log.error("DataInputStream/DataOutputStream error!", e);
        }

        this.lastTickTime = DateTimeUtils.currentTimeMillis();
    }

    public String getClientName() {
        return socket.getRemoteSocketAddress().toString();
    }

    /**
     * 关闭连接
     */
    public void close() {

        // 处理用户登出
        MessageDispatcher.getInstance().playerEventHandler.logout(player);

        log.warn("[关闭客户端连接] " + getClientName());

        if (!socket.isClosed()) {
            try {
                dataInputStream.close();
                dataOutputStream.close();
                socket.close();
            } catch (IOException e) {
                log.error("Error occur when closing Socket/DataInputStream/DataOutputStream!", e);
            }
        }

        dataInputStream = null;
        dataOutputStream = null;
        socket = null;
        player = null;

        inUse = false;
    }

    /**
     * 向客户端发送数据
     */
    public void send(Protocol proto) {
        networkManager.sendData(this, proto);
    }
}
