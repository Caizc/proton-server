package com.zicongcai.core;

import com.google.common.base.CharMatcher;
import com.googlecode.genericdao.search.Search;
import com.zicongcai.logic.PlayerData;
import com.zicongcai.persistence.dao.PlayerDao;
import com.zicongcai.persistence.dao.UserDao;
import com.zicongcai.persistence.po.PlayerPo;
import com.zicongcai.persistence.po.UserPo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * 数据管理器
 */
@Service("dataManager")
public class DataManager {

    private static final Log log = LogFactory.getLog(DataManager.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private PlayerDao playerDao;

    /**
     * 检查用户输入的字符串是否安全
     *
     * @param str
     * @return
     */
    private boolean isSafeStr(String str) {

        // 不允许空字符串
        if (str == null || str.length() == 0) {
            return false;
        }

        // 仅允许字符串为字母和数字的组合
        String result = CharMatcher.javaLetterOrDigit().retainFrom(str);

        return str.equals(result);
    }

    /**
     * 检查用户ID是否已存在
     *
     * @param id
     * @return
     */
    private boolean canRegister(String id) {

        if (!isSafeStr(id)) {
            log.error("非法的用户ID: [" + id + "]");
            return false;
        }

        Search search = new Search();
        search.addFilterEqual("id", id);

        List<UserPo> list = userDao.search(search);

        if (list == null || list.size() == 0) {
            log.info("用户ID[" + id + "]可用");
            return true;
        } else {
            log.error("用户ID[" + id + "]已存在！");
            return false;
        }
    }

    /**
     * 注册用户
     *
     * @param id
     * @param pw
     * @return
     */
    public boolean register(String id, String pw) {

        if (!isSafeStr(id) || !isSafeStr(pw)) {
            log.error("用户注册失败，非法的用户ID或密码！");
            return false;
        }

        if (!canRegister(id)) {
            return false;
        }

        UserPo userPo = new UserPo();
        userPo.setId(id);
        userPo.setPw(pw);

        userDao.save(userPo);
        userDao.flush();

        log.info("用户[" + id + "]注册成功");

        return true;
    }

    /**
     * 创建角色
     *
     * @param id
     * @return
     */
    public boolean createPlayer(String id) {

        if (!isSafeStr(id)) {
            log.error("非法的用户ID: [" + id + "]");
            return false;
        }

        PlayerData playerData = new PlayerData();
        byte[] data = serializePlayerData(playerData);

        PlayerPo playerPo = new PlayerPo();
        playerPo.setId(id);
        playerPo.setData(data);

        playerDao.save(playerPo);
        playerDao.flush();

        log.info("用户[" + id + "]创建角色成功");

        return true;
    }

    /**
     * 用户名密码校验
     *
     * @param id
     * @param pw
     * @return
     */
    public boolean checkPassword(String id, String pw) {

        if (!isSafeStr(id) || !isSafeStr(pw)) {
            log.error("非法的用户ID或密码！");
            return false;
        }

        Search search = new Search();
        search.addFilterEqual("id", id);
        search.addFilterEqual("pw", pw);

        List<UserPo> list = userDao.search(search);

        if (list == null || list.size() == 0) {
            log.error("用户ID[" + id + "]不存在或错误的密码！");
            return false;
        } else {
            log.info("用户[" + id + "]密码校验通过");
            return true;
        }
    }

    /**
     * 获取角色数据
     *
     * @param id
     * @return
     */
    public PlayerData getPlayerData(String id) {

        PlayerData playerData = null;

        if (!isSafeStr(id)) {
            log.error("非法的用户ID: [" + id + "]");
            return playerData;
        }

        Search search = new Search();
        search.addFilterEqual("id", id);

        List<PlayerPo> list = playerDao.search(search);

        if (list == null || list.size() == 0) {
            log.warn("用户[" + id + "]的角色数据为空！");
            return playerData;
        } else {

            byte[] data = list.get(0).getData();

            // 将字节数组反序列化为PlayerData对象
            playerData = deserializePlayerData(data);

            return playerData;
        }
    }

    /**
     * 保存角色数据
     *
     * @param player
     * @return
     */
    public boolean savePlayer(Player player) {

        String id = player.getId();
        PlayerData playerData = player.getData();

        try {
            byte[] data = serializePlayerData(playerData);

            PlayerPo playerPo = new PlayerPo();
            playerPo.setId(id);
            playerPo.setData(data);

            playerDao.save(playerPo);
            playerDao.flush();
        } catch (Exception e) {
            log.error("保存用户[" + id + "]的角色数据过程出错！", e);
            return false;
        }

        log.info("用户[" + id + "]的角色数据已保存");

        return true;
    }

    /**
     * 将PlayerData对象序列化为字节数组
     *
     * @param playerData
     * @return
     */
    private byte[] serializePlayerData(PlayerData playerData) {

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(playerData);

            byte[] data = baos.toByteArray();

            return data;
        } catch (Exception e) {
            log.error("将PlayerData对象序列化为byte[]过程出错！", e);
        }

        return null;
    }

    /**
     * 将字节数组反序列化为PlayerData对象
     *
     * @param data
     * @return
     */
    private PlayerData deserializePlayerData(byte[] data) {

        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            ObjectInputStream ois = new ObjectInputStream(bais);

            PlayerData playerData = (PlayerData) ois.readObject();

            return playerData;
        } catch (Exception e) {
            log.error("将byte[]反序列化为PlayerData对象过程出错！", e);
        }

        return null;
    }
}
