package com.zicongcai.core;

import com.zicongcai.logic.PlayerData;

/**
 * 玩家角色
 */
public class Player {

    private String id;

    private PlayerData data;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PlayerData getData() {
        return data;
    }

    public void setData(PlayerData data) {
        this.data = data;
    }

}
