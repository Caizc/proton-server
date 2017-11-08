package com.zicongcai.logic;

import com.zicongcai.core.Player;
import com.zicongcai.core.Protocol;
import com.zicongcai.core.ProtocolBytes;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 房间
 */
public class Room {

    private static final Log log = LogFactory.getLog(Room.class);

    /**
     * 房间状态枚举（准备中/战斗中）
     */
    public enum Status {

        PREPARE(1),
        FIGHT(2);

        private int value;

        Status(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }

    /**
     * 房间状态
     */
    private Status status = Status.PREPARE;

    /**
     * 房间容量（最大玩家数）
     */
    private int capacity = 6;

    /**
     * 房间中的玩家列表
     */
    private ConcurrentHashMap<String, Player> players = new ConcurrentHashMap<>();

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public ConcurrentHashMap<String, Player> getPlayers() {
        return players;
    }

    public void setPlayers(ConcurrentHashMap<String, Player> players) {
        this.players = players;
    }

    /**
     * 加入玩家
     */
    public boolean addPlayer(Player player) {

        synchronized (players) {

            if (players.size() >= capacity) {
                log.info("[加入房间] 房间已满员，无法加入！");
                return false;
            }

            PlayerTempData tempData = player.getTempData();

            tempData.setRoom(this);
            tempData.setTeam(assignTeam());
            tempData.setStatus(PlayerTempData.Status.ROOM);

            // 如果房间当前没有别的玩家，则设置该玩家为房主
            if (players.size() == 0) {
                tempData.setRoomOwner(true);
            }

            players.put(player.getId(), player);
        }

        return true;
    }

    /**
     * 移除玩家
     */
    public void removePlayer(Player player) {

        synchronized (players) {

            if (!players.containsKey(player.getId())) {
                return;
            }

            // 重置玩家状态，将它从当前房间的玩家列表中移除
            player.getTempData().setStatus(PlayerTempData.Status.NONE);
            players.remove(player.getId());

            // 如果移除的玩家刚好是房主，还需要更换房主
            if (player.getTempData().isRoomOwner) {
                updateOwner();
            }
        }
    }

    /**
     * 广播消息
     */
    public void broadcast(Protocol proto) {

        // 遍历当前玩家列表，逐一发送消息
        for (Player player : players.values()) {
            player.send(proto);
        }
    }

    /**
     * 获取房间信息
     */
    public Protocol getRoomInfo() {

        ProtocolBytes proto = new ProtocolBytes();
        proto.addString(MessageType.MSG_GETROOMINFO);
        proto.addInt(players.size());

        for (Player player : players.values()) {
            proto.addString(player.getId());
            proto.addInt(player.getTempData().getTeam());
            proto.addInt(player.getData().win);
            proto.addInt(player.getData().lost);
            proto.addInt(player.getTempData().isRoomOwner ? 1 : 0);
        }

        return proto;
    }

    /**
     * 能否开战
     */
    public boolean canStart() {

        if (status != Status.PREPARE) {
            return false;
        }

        int counter1 = 0;
        int counter2 = 0;

        for (Player player : players.values()) {
            if (player.getTempData().getTeam() == 1) {
                counter1++;
            }

            if (player.getTempData().getTeam() == 2) {
                counter2++;
            }
        }

        if (counter1 < 1 || counter2 < 1) {
            return false;
        }

        return true;
    }

    /**
     * 开始战斗
     */
    public void startFight() {

        status = Status.FIGHT;

        ProtocolBytes proto = new ProtocolBytes();
        proto.addString(MessageType.MSG_FIGHT);

        synchronized (players) {

            proto.addInt(players.size());

            for (Player player : players.values()) {

                proto.addString(player.getId());
                proto.addInt(player.getTempData().getTeam());

                // FIXME: 这个协议需要修改一下
                proto.addInt(1);

                player.getTempData().setStatus(PlayerTempData.Status.FIGHT);
            }
        }

        // 向房间中的所有玩家广播开始战斗消息
        broadcast(proto);
    }

    /**
     * 胜负判断
     */
    private int isWin() {

        if (status != Status.FIGHT) {
            return 0;
        }

        // TODO: 胜负判断逻辑未完成

        return 0;
    }

    /**
     * 胜负结算
     */
    public void updateWin() {
        // TODO: 胜负结算逻辑未完成
    }

    /**
     * 中途退出战斗
     */
    public void quitFight(Player player) {
        // TODO: 中途退出战斗逻辑未完成
    }

    /**
     * 分配队伍
     */
    private int assignTeam() {

        int counter1 = 0;
        int counter2 = 0;

        for (Player player : players.values()) {
            if (player.getTempData().getTeam() == 1) {
                counter1++;
            }

            if (player.getTempData().getTeam() == 2) {
                counter2++;
            }
        }

        if (counter1 <= counter2) {
            return 1;
        } else {
            return 2;
        }
    }

    /**
     * 更换房主
     */
    private void updateOwner() {

        synchronized (players) {

            if (players.size() <= 0) {
                return;
            }

            boolean isFirst = true;

            // 设置房间的当前玩家列表中的第一个玩家为房主
            for (Player player : players.values()) {
                if (isFirst) {
                    player.getTempData().setRoomOwner(true);
                } else {
                    player.getTempData().setRoomOwner(false);
                }
            }
        }
    }
}
