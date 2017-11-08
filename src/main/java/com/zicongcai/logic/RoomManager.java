package com.zicongcai.logic;

import com.zicongcai.core.Player;
import com.zicongcai.core.Protocol;
import com.zicongcai.core.ProtocolBytes;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 房间管理器
 */
public class RoomManager {

    private static final Log log = LogFactory.getLog(RoomManager.class);

    private static RoomManager instance;

    /**
     * 不允许外部类使用构造方法获取该类的实例
     */
    private RoomManager() {
        super();
    }

    /**
     * 单例模式，获取房间管理器实例
     *
     * @return 房间管理器实例
     */
    public static RoomManager getInstance() {
        if (null != instance) {
            return instance;
        } else {
            synchronized (RoomManager.class) {
                if (null == instance) {
                    instance = new RoomManager();
                    return instance;
                } else {
                    return instance;
                }
            }
        }
    }

    /**
     * 房间列表
     */
    private List<Room> rooms = new ArrayList<>();

    public List<Room> getRooms() {
        return rooms;
    }

    /**
     * 创建房间
     */
    public void createRoom(Player player) {

        Room room = new Room();

        synchronized (rooms) {
            rooms.add(room);
            room.addPlayer(player);
        }
    }

    /**
     * 离开房间
     */
    public void leaveRoom(Player player) {

        if (player.getTempData().getStatus() == PlayerTempData.Status.NONE) {
            return;
        }

        Room room = player.getTempData().getRoom();

        synchronized (rooms) {
            room.removePlayer(player);

            if (room.getPlayers().size() <= 0) {
                rooms.remove(room);
            }
        }
    }

    /**
     * 获取房间列表
     */
    public Protocol getRoomList() {

        ProtocolBytes proto = new ProtocolBytes();
        proto.addString(MessageType.MSG_GETROOMLIST);

        int count = rooms.size();
        proto.addInt(count);

        for (int i = 0; i < count; i++) {
            Room room = rooms.get(i);
            proto.addInt(room.getPlayers().size());
            proto.addInt(room.getStatus().getValue());
        }

        return proto;
    }
}
