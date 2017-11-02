package com.zicongcai.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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

        this.socket = socket;

        try {
            this.dataInputStream = new DataInputStream(socket.getInputStream());
            this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            log.error("DataInputStream/DataOutputStream error!", e);
        }
    }

    public String getClientName() {
        return socket.getRemoteSocketAddress().toString();
    }

}
