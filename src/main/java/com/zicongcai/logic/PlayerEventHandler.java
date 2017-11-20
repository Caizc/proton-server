package com.zicongcai.logic;

import com.zicongcai.core.*;
import com.zicongcai.thirdparty.SpringContextHolder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 玩家事件处理类
 */
public class PlayerEventHandler {

    private static final Log log = LogFactory.getLog(PlayerEventHandler.class);

    private DataManager dataManager;

    /**
     * 构造方法
     */
    public PlayerEventHandler() {

        // 注入DataManager实例
        dataManager = SpringContextHolder.getBean("dataManager");
    }

    /**
     * 玩家登录
     */
    public boolean login(Player player) {

        if (player == null) {
            return false;
        }

        log.info("[用户登录成功] 用户名: [" + player.getId() + "]");

        return true;
    }

    /**
     * 玩家登出
     */
    public boolean logout(Player player) {

        if (player == null) {
            return false;
        }

        // 如果玩家正在房间中
        if (player.getTempData().getStatus() == PlayerTempData.Status.ROOM || player.getTempData().getStatus() == PlayerTempData.Status.FIGHTING) {

            Room room = player.getTempData().getRoom();

            // 如果玩家正在战斗中，需要先退出战斗
            if (player.getTempData().getStatus() == PlayerTempData.Status.FIGHTING) {
                room.quitFight(player);
            }

            // 离开房间
            RoomManager.getInstance().leaveRoom(player);

            // 如果房间还有玩家的话，向房间中的所有玩家广播新的房间信息
            if (room != null && room.getPlayers().size() != 0) {
                room.broadcast(room.getRoomInfo());
            }
        }

        // 保存角色数据
        if (!dataManager.savePlayer(player)) {
            return false;
        }

        // 移除引用
        player.setConn(null);

        log.info("[用户登出成功] 用户名: [" + player.getId() + "]");

        return true;
    }

    /**
     * 将重复登录的（旧）用户踢下线
     */
    public void kickOffTheLine(String playerId) {

        Connection conn = ConnectionPool.getInstance().get(playerId);

        // 连接为空，表示没有重复登录的玩家；否则表示该用户正在线上，需要将其踢下线
        if (conn == null) {
            return;
        } else {

            synchronized (conn.getPlayer()) {

                // 向客户端发送用户登出消息，并处理用户登出的相应事件
                MessageDispatcher.getInstance().connectionMessageHandler.logout(conn);
            }

            log.info("[重复用户踢下线] 用户名: [" + playerId + "]");
        }
    }
}
