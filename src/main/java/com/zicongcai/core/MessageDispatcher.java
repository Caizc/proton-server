package com.zicongcai.core;

import com.zicongcai.logic.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 消息分发器
 */
public class MessageDispatcher {

    private static final Log log = LogFactory.getLog(MessageDispatcher.class);

    private static MessageDispatcher instance;

    private ConnectionMessageHandler connectionMessageHandler = new ConnectionMessageHandler();
    private PlayerMessageHandler playerMessageHandler = new PlayerMessageHandler();
    private PlayerEventHandler playerEventHandler = new PlayerEventHandler();
    private BattleMessageHandler battleMessageHandler = new BattleMessageHandler();
    private RoomMessageHandler roomMessageHandler = new RoomMessageHandler();

    /**
     * 不允许外部类使用构造方法获取该类的实例
     */
    private MessageDispatcher() {
        super();
    }

    /**
     * 单例模式，获取消息分发器实例
     *
     * @return 消息分发器实例
     */
    public static MessageDispatcher getInstance() {
        if (null != instance) {
            return instance;
        } else {
            synchronized (MessageDispatcher.class) {
                if (null == instance) {
                    instance = new MessageDispatcher();
                    return instance;
                } else {
                    return instance;
                }
            }
        }
    }

    /**
     * 根据消息类型向相应的接口传递消息
     *
     * @param msgType
     * @param conn
     * @param proto
     */
    public void deliverMessage(String msgType, Connection conn, Protocol proto) {

        // TODO: 为简单起见，暂时使用 Switch 语句实现消息分发，后续改为监听者模式会更好一些

        switch (msgType) {

            case MessageType.MSG_PING:
//                battleMessageHandler.ping(conn, proto);
                break;

            case MessageType.MSG_HEARTBEAT:
                connectionMessageHandler.heartBeat(conn, proto);
                break;

            case MessageType.MSG_REGISTER:
                connectionMessageHandler.register(conn, proto);
                break;

            case MessageType.MSG_LOGIN:
                connectionMessageHandler.login(conn, proto);
                break;

            case MessageType.MSG_LOGOUT:
                connectionMessageHandler.logout(conn, proto);
                break;

            default:
                log.info("=== " + msgType);
                break;
        }

    }

}
