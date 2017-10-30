package com.zicongcai.core;

import java.net.Socket;

/**
 * 客户端连接
 */
public class Connection {

    private Socket socket;
    private Player player;

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * 构造方法
     *
     * @param socket 客户端 Socket 连接实例
     */
    public Connection(Socket socket) {
        super();

        this.socket = socket;
    }

    public String getClientName() {
        return socket.getRemoteSocketAddress().toString();
    }

}
