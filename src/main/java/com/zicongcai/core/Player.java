package com.zicongcai.core;

import com.zicongcai.logic.PlayerData;
import com.zicongcai.logic.PlayerTempData;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 玩家角色
 */
public class Player {

    private static final Log log = LogFactory.getLog(Player.class);

    private String id;

    private PlayerData data;

    private PlayerTempData tempData;

    private Connection conn;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PlayerData getData() {
        return data;
    }

    public void setData(PlayerData data) {
        this.data = data;
    }

    public PlayerTempData getTempData() {
        return tempData;
    }

    public void setTempData(PlayerTempData tempData) {
        this.tempData = tempData;
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    /**
     * 构造方法
     */
    public Player(String id, Connection conn) {
        this.id = id;
        this.conn = conn;
        this.tempData = new PlayerTempData();
    }

    /**
     * 向该玩家发送数据
     */
    public void send(Protocol proto) {

        if (conn == null || conn.getSocket().isClosed()) {
            log.error("连接未就绪，无法向玩家 [" + id + "] 发送数据！");
            return;
        }

        conn.send(proto);
    }
}
