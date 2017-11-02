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

    private NetworkManager networkManager = NetworkManager.getInstance();

    private DataManager dataManager;

    /**
     * 构造方法
     */
    public ConnectionMessageHandler() {

        // 注入DataManager实例
        dataManager = SpringContextHolder.getBean("dataManager");
    }

    /**
     * 心跳消息
     */
    public void heartBeatMsg(Connection conn, Protocol proto) {

        conn.setLastTickTime(DateTimeUtils.currentTimeMillis());

        log.info("[更新心跳时间] " + conn.getClientName());
    }

    /**
     * 用户注册
     */
    public void registerMsg(Connection conn, Protocol proto) {

        int start = 0;
        int[] end = {0};

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
        networkManager.sendData(conn, responseProto);
    }

}
