package com.zicongcai.logic;

import com.zicongcai.core.Player;
import com.zicongcai.core.Protocol;
import com.zicongcai.core.ProtocolBytes;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 玩家消息处理类
 */
public class PlayerMessageHandler {

    private static final Log log = LogFactory.getLog(PlayerMessageHandler.class);

    /**
     * 获取成就
     */
    public void getAchievement(Player player, Protocol proto) {

        if (player == null || player.getData() == null) {
            log.error("[获取成就] Player or PlayerData is null!");
            return;
        }

        // 回应消息
        ProtocolBytes responseProto = new ProtocolBytes();
        responseProto.addString(MessageType.MSG_GETACHIEVEMENT);
        responseProto.addInt(player.getData().win);
        responseProto.addInt(player.getData().lost);

        // 向玩家发送回应数据
        player.send(responseProto);

        log.info("[获取成就] [" + player.getId() + "] " + player.getData().win + ":" + player.getData().lost);
    }

    /**
     * 获取玩家列表
     */
    public void getList(Player player, Protocol proto) {
        // TODO: 调用Scene类
    }

    /**
     * 获取分数
     */
    public void getScore(Player player, Protocol proto) {

        if (player == null || player.getData() == null) {
            log.error("[获取分数] Player or PlayerData is null!");
            return;
        }

        // 回应消息
        ProtocolBytes responseProto = new ProtocolBytes();
        responseProto.addString(MessageType.MSG_GETSCORE);
        responseProto.addInt(player.getData().score);

        // 向玩家发送回应数据
        player.send(responseProto);

        log.info("[获取分数] [" + player.getId() + "] score: " + player.getData().score);
    }

    /**
     * 增加分数
     */
    public void addScore(Player player, Protocol proto) {

        int start = 0;
        int[] end = {0};

        // 解析消息
        ProtocolBytes protocolBytes = (ProtocolBytes) proto;
        String protoName = protocolBytes.getString(start, end);

        // FIXME: 协议消息中是否有增加的分数，需验证

        player.getData().score += 1;

        log.info("[增加分数] [" + player.getId() + "] score: " + player.getData().score);
    }

    /**
     * 更新信息
     */
    public void updateInfo(Player player, Protocol proto) {
        // TODO: 调用Scene类
    }
}
