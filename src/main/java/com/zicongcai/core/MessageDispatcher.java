package com.zicongcai.core;

import com.zicongcai.logic.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 消息分发器
 */
public class MessageDispatcher {

    public ConnectionMessageHandler connectionMessageHandler = new ConnectionMessageHandler();
    public PlayerMessageHandler playerMessageHandler = new PlayerMessageHandler();
    public PlayerEventHandler playerEventHandler = new PlayerEventHandler();
    public BattleMessageHandler battleMessageHandler = new BattleMessageHandler();
    public RoomMessageHandler roomMessageHandler = new RoomMessageHandler();

    private static final Log log = LogFactory.getLog(MessageDispatcher.class);

    private static MessageDispatcher instance;

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

        // FIXME: 为简单起见，暂时使用 Switch 语句实现消息分发，后续改为监听者模式会更好一些

        switch (msgType) {

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

            case MessageType.MSG_GETACHIEVEMENT:
                playerMessageHandler.getAchievement(conn.getPlayer(), proto);
                break;

            case MessageType.MSG_GETLIST:
                playerMessageHandler.getList(conn.getPlayer(), proto);
                break;

            case MessageType.MSG_GETSCORE:
                playerMessageHandler.getScore(conn.getPlayer(), proto);
                break;

            case MessageType.MSG_ADDSCORE:
                playerMessageHandler.addScore(conn.getPlayer(), proto);
                break;

            case MessageType.MSG_UPDATEINFO:
                playerMessageHandler.updateInfo(conn.getPlayer(), proto);
                break;

            case MessageType.MSG_GETROOMLIST:
                roomMessageHandler.getRoomList(conn.getPlayer(), proto);
                break;

            case MessageType.MSG_GETROOMINFO:
                roomMessageHandler.getRoomInfo(conn.getPlayer(), proto);
                break;

            case MessageType.MSG_CREATEROOM:
                roomMessageHandler.createRoom(conn.getPlayer(), proto);
                break;

            case MessageType.MSG_ENTERROOM:
                roomMessageHandler.enterRoom(conn.getPlayer(), proto);
                break;

            case MessageType.MSG_LEAVEROOM:
                roomMessageHandler.leaveRoom(conn.getPlayer(), proto);
                break;

            case MessageType.MSG_STARTFIGHT:
                battleMessageHandler.startFight(conn.getPlayer(), proto);
                break;

            case MessageType.MSG_PING:
                battleMessageHandler.ping(conn.getPlayer(), proto);
                break;

            case MessageType.MSG_TRUESYNC:
                battleMessageHandler.trueSync(conn.getPlayer(), proto);
                break;

            default:
                log.info("=== [未处理的消息类型]: " + msgType);
                break;
        }
    }
}
