package com.zicongcai.logic;

/**
 * 玩家临时数据
 */
public class PlayerTempData {

    /**
     * 当前状态枚举
     */
    public enum Status {
        NONE,
        ROOM,
        FIGHTING
    }

    /**
     * 玩家当前状态
     */
    private Status status;

    /**
     * 所在房间
     */
    private Room room;

    /**
     * 是否房主
     */
    public boolean isRoomOwner = false;

    /**
     * 所属队伍
     */
    private int team = 1;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public boolean isRoomOwner() {
        return isRoomOwner;
    }

    public void setRoomOwner(boolean roomOwner) {
        isRoomOwner = roomOwner;
    }

    public int getTeam() {
        return team;
    }

    public void setTeam(int team) {
        this.team = team;
    }

    /**
     * 构造方法
     */
    public PlayerTempData() {
        this.status = Status.NONE;
    }
}
