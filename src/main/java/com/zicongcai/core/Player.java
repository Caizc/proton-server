package com.zicongcai.core;

import com.zicongcai.logic.PlayerData;
import com.zicongcai.thirdparty.SpringContextHolder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 玩家角色
 */
public class Player {

    private static final Log log = LogFactory.getLog(Player.class);

    private String id;

    private PlayerData data;

    private Connection conn;

    private DataManager dataManager;

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
        this.dataManager = SpringContextHolder.getBean("dataManager");
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

    /**
     * 将该玩家踢下线
     */
    public void kickOffTheLine(Protocol proto) {
        // TODO: 踢下线，未实现
    }

    /**
     * 玩家登出
     */
    public boolean logout() {

        // TODO: 触发登出事件

        // 先保存角色数据
        if (!dataManager.savePlayer(this)) {
            return false;
        }

        // 移除引用，关闭连接
        conn.setPlayer(null);
        conn.close();
        conn = null;

        return true;
    }
}
