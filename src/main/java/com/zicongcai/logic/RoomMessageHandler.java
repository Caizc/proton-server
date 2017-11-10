package com.zicongcai.logic;

import com.zicongcai.core.Player;
import com.zicongcai.core.Protocol;
import com.zicongcai.core.ProtocolBytes;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 房间消息处理类
 */
public class RoomMessageHandler {

    private static final Log log = LogFactory.getLog(RoomMessageHandler.class);

    private RoomManager roomManager;

    /**
     * 构造方法
     */
    public RoomMessageHandler() {
        this.roomManager = RoomManager.getInstance();
    }

    /**
     * 获取房间列表
     */
    public void getRoomList(Player player, Protocol proto) {

        player.send(roomManager.getRoomList());

        log.info("[获取房间列表] [" + player.getId() + "]");
    }

    /**
     * 创建房间
     */
    public void createRoom(Player player, Protocol proto) {

        ProtocolBytes responseProto = new ProtocolBytes();
        responseProto.addString(MessageType.MSG_CREATEROOM);

        // 仅当玩家不在房间和战斗中时，才可以创建房间
        if (player.getTempData().getStatus() != PlayerTempData.Status.NONE) {

            log.error("[创建房间失败] [" + player.getId() + "]");

            responseProto.addInt(-1);
            player.send(responseProto);

            return;
        }

        roomManager.createRoom(player);
        responseProto.addInt(0);
        player.send(responseProto);

        log.info("[创建房间成功] [" + player.getId() + "]");
    }

    /**
     * 进入房间
     */
    public void enterRoom(Player player, Protocol proto) {

        int start = 0;
        int[] end = {0};

        // 解析消息，获取房间编号
        ProtocolBytes protocolBytes = (ProtocolBytes) proto;
        String protoName = protocolBytes.getString(start, end);
        int roomIndex = protocolBytes.getInt(end[0], end);

        log.info("[进入房间] 房间编号: [" + roomIndex + "] 用户ID: [" + player.getId() + "]");

        // 回应消息协议
        ProtocolBytes responseProto = new ProtocolBytes();
        responseProto.addString(MessageType.MSG_ENTERROOM);

        if (roomIndex < 0 || roomIndex >= roomManager.getRooms().size()) {

            log.error("[房间编号错误] [" + player.getId() + "]");

            responseProto.addInt(-1);
            player.send(responseProto);

            return;
        }

        Room room = roomManager.getRooms().get(roomIndex);

        if (room.getStatus() != Room.Status.PREPARE) {

            log.error("[房间未就绪] [" + player.getId() + "]");

            responseProto.addInt(-1);
            player.send(responseProto);

            return;
        }

        // 向房间加入玩家
        if (room.addPlayer(player)) {

            room.broadcast(room.getRoomInfo());
            responseProto.addInt(0);
            player.send(responseProto);
        } else {

            log.error("[进入房间失败] 房间编号: [" + roomIndex + "] 用户ID: [" + player.getId() + "]");

            responseProto.addInt(-1);
            player.send(responseProto);
        }
    }

    /**
     * 获取房间信息
     */
    public void getRoomInfo(Player player, Protocol proto) {

        if (player.getTempData().getStatus() != PlayerTempData.Status.ROOM) {

            log.error("[获取房间信息失败] [" + player.getId() + "]");

            return;
        }

        player.send(player.getTempData().getRoom().getRoomInfo());
    }

    /**
     * 离开房间
     */
    public void leaveRoom(Player player, Protocol proto) {

        // 回应消息协议
        ProtocolBytes responseProto = new ProtocolBytes();
        responseProto.addString(MessageType.MSG_LEAVEROOM);

        if (player.getTempData().getStatus() != PlayerTempData.Status.ROOM) {

            log.error("[离开房间失败] [" + player.getId() + "]");

            responseProto.addInt(-1);
            player.send(responseProto);

            return;
        }

        responseProto.addInt(0);
        player.send(responseProto);

        roomManager.leaveRoom(player);

        // 广播通知房间中的其他玩家最新的房间信息
        Room room = player.getTempData().getRoom();
        if (room != null) {
            room.broadcast(room.getRoomInfo());
        }

        log.info("[离开房间] [" + player.getId() + "]");
    }
}
