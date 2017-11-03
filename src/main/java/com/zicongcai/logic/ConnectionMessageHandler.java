package com.zicongcai.logic;

import com.zicongcai.core.*;
import com.zicongcai.thirdparty.SpringContextHolder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTimeUtils;

/**
 * 连接消息处理类
 */
public class ConnectionMessageHandler {

    private static final Log log = LogFactory.getLog(ConnectionMessageHandler.class);

    private DataManager dataManager;

    /**
     * 构造方法
     */
    public ConnectionMessageHandler() {

        // 注入DataManager实例
        dataManager = SpringContextHolder.getBean("dataManager");
    }

    /**
     * 心跳
     */
    public void heartBeat(Connection conn, Protocol proto) {

        conn.setLastTickTime(DateTimeUtils.currentTimeMillis());

        log.info("[更新心跳时间] " + conn.getClientName());
    }

    /**
     * 用户注册
     */
    public void register(Connection conn, Protocol proto) {

        int start = 0;
        int[] end = {0};

        // 解析消息，获取注册的用户名、密码
        ProtocolBytes protocolBytes = (ProtocolBytes) proto;
        String protoName = protocolBytes.getString(start, end);
        String id = protocolBytes.getString(end[0], end);
        String pw = protocolBytes.getString(end[0], end);

        log.info("[用户注册] " + conn.getClientName() + " 用户名: [" + id + "] 密码: [" + pw + "]");

        // 回应消息协议
        ProtocolBytes responseProto = new ProtocolBytes();
        responseProto.addString(protoName);

        // 注册用户
        boolean isRegisterSucceed = dataManager.register(id, pw);

        if (isRegisterSucceed) {
            responseProto.addInt(0);
        } else {
            responseProto.addInt(-1);
        }

        // 创建角色
        dataManager.createPlayer(id);

        log.info("[用户注册成功] " + conn.getClientName() + " 用户名: [" + id + "]");

        // 向客户端发送回应消息
        conn.send(responseProto);
    }

    /**
     * 用户登录
     */
    public void login(Connection conn, Protocol proto) {

        int start = 0;
        int[] end = {0};

        // 解析消息，获取登录的用户名、密码
        ProtocolBytes protocolBytes = (ProtocolBytes) proto;
        String protoName = protocolBytes.getString(start, end);
        String id = protocolBytes.getString(end[0], end);
        String pw = protocolBytes.getString(end[0], end);

        log.info("[用户登录] " + conn.getClientName() + " 用户名: [" + id + "] 密码: [" + pw + "]");

        // 回应消息协议
        ProtocolBytes responseProto = new ProtocolBytes();
        responseProto.addString(protoName);

        // 用户名密码校验
        if (!dataManager.checkPassword(id, pw)) {

            log.info("[用户登录失败] 用户名: [" + id + "]");

            responseProto.addInt(-1);

            // 向客户端发送用户登录失败消息
            conn.send(responseProto);

            return;
        }

        // TODO: 如果用户已经登录，则需要将线上的用户‘踢下线’，再执行后续的操作

        PlayerData playerData = dataManager.getPlayerData(id);

        if (playerData == null) {

            log.info("[玩家数据为空] 用户名: [" + id + "]");

            responseProto.addInt(-1);

            // 向客户端发送获取玩家数据失败消息
            conn.send(responseProto);

            return;
        }

        conn.setPlayer(new Player(id, conn));
        conn.getPlayer().setData(playerData);

        // TODO: 触发用户登录事件

        responseProto.addInt(0);

        // 向客户端发送回应消息
        conn.send(responseProto);

        log.info("[用户登录成功] 用户名: [" + id + "]");
    }

    /**
     * 用户登出
     */
    public void logout(Connection conn, Protocol proto) {

        // 回应消息协议
        ProtocolBytes responseProto = new ProtocolBytes();
        responseProto.addString(MessageType.MSG_LOGOUT);
        responseProto.addInt(0);

        conn.send(responseProto);

        if (conn.getPlayer() == null) {
            conn.close();
        } else {
            String id = conn.getPlayer().getId();
            conn.getPlayer().logout();
            log.info("[用户登出成功] 用户名: [" + id + "]");
        }
    }
}
