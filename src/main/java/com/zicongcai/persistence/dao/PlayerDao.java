package com.zicongcai.persistence.dao;

import com.zicongcai.persistence.po.PlayerPo;
import org.springframework.stereotype.Repository;

/**
 * player 对象的 DAO
 */
@Repository("playerDao")
public class PlayerDao extends BaseDao<PlayerPo, String> {
    // nothing to do here.
}
