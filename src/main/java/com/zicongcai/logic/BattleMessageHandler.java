package com.zicongcai.logic;

import com.zicongcai.core.Player;
import com.zicongcai.core.Protocol;
import com.zicongcai.core.ProtocolBytes;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 战斗消息处理类
 */
public class BattleMessageHandler {

    private static final Log log = LogFactory.getLog(BattleMessageHandler.class);

    /**
     * 开始战斗
     */
    public void startFight(Player player, Protocol proto) {

        // 回应消息
        ProtocolBytes responseProto = new ProtocolBytes();
        responseProto.addString(MessageType.MSG_STARTFIGHT);

        if (player.getTempData().getStatus() != PlayerTempData.Status.ROOM) {

            log.info("[开始战斗失败] 玩家不在房间中 [" + player.getId() + "]");

            responseProto.addInt(-1);
            player.send(responseProto);

            return;
        }

        if (!player.getTempData().isRoomOwner) {

            log.info("[开始战斗失败] 玩家不是房主 [" + player.getId() + "]");

            responseProto.addInt(-1);
            player.send(responseProto);

            return;
        }

        if (!player.getTempData().getRoom().canStart()) {

            log.info("[开始战斗失败] 双方阵营人数未达标 [" + player.getId() + "]");

            responseProto.addInt(-1);
            player.send(responseProto);

            return;
        }

        responseProto.addInt(0);

        // 向玩家发送回应消息
        player.send(responseProto);

        log.info("[开始战斗成功] [" + player.getId() + "]");

        // 通知房间中的所有玩家可以执行战斗
        player.getTempData().getRoom().fight();
    }

    /**
     * TrueSync数据广播
     */
    public void trueSync(Player player, Protocol proto) {

        if (player.getTempData().getStatus().equals(PlayerTempData.Status.FIGHTING)) {

            // 仅当玩家处于战斗状态时，将TrueSync数据包原封不动的广播到同一个房间中的所有玩家
            player.getTempData().getRoom().broadcast(proto);
        }
    }

    /**
     * Ping
     */
    public void ping(Player player, Protocol proto) {

        // 将Ping消息包直接发回给原客户端
        player.send(proto);
    }
}
