package com.zicongcai.logic;

import java.io.Serializable;

/**
 * 玩家角色持久化数据
 */
public class PlayerData implements Serializable {

    public int score = 0;

    public int win = 0;

    public int lost = 0;

    public PlayerData() {
        this.score = 100;
    }
}
