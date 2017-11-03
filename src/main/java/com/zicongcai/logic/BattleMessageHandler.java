package com.zicongcai.logic;

import com.zicongcai.core.Connection;
import com.zicongcai.core.Protocol;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 战斗消息处理类
 */
public class BattleMessageHandler {

    private static final Log log = LogFactory.getLog(BattleMessageHandler.class);

    /**
     * Ping
     */
    public void ping(Connection conn, Protocol proto) {
        // TODO:
//        log.info("[Ping] " + conn.getClientName());
    }

}
