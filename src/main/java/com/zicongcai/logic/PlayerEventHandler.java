package com.zicongcai.logic;

import com.zicongcai.core.DataManager;
import com.zicongcai.core.Player;
import com.zicongcai.core.Protocol;
import com.zicongcai.thirdparty.SpringContextHolder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 玩家事件处理类
 */
public class PlayerEventHandler {

    private static final Log log = LogFactory.getLog(PlayerEventHandler.class);

    private DataManager dataManager;

    /**
     * 构造方法
     */
    public PlayerEventHandler() {

        // 注入DataManager实例
        dataManager = SpringContextHolder.getBean("dataManager");
    }

    /**
     * 玩家登录
     */
    public void login(Player player) {
        // TODO: 暂时没有需要处理的事件
    }

    /**
     * 玩家登出
     */
    public boolean logout(Player player) {

        // TODO: 处理登出事件，如退出房间、结束战斗等

        // 保存角色数据
        if (!dataManager.savePlayer(player)) {
            return false;
        }

        // 关闭连接，移除引用
        player.getConn().close();
        player.setConn(null);

        return true;
    }

    /**
     * 将玩家踢下线
     */
    public void kickOffTheLine(Player player, Protocol proto) {
        // TODO: 踢下线，未实现
    }
}
