package com.zicongcai.logic;

import com.zicongcai.core.Player;
import com.zicongcai.core.Protocol;
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
    }

    /**
     * Ping
     */
    public void ping(Player player, Protocol proto) {
        // TODO:
//        log.info("[Ping] " + conn.getClientName());
    }
}
