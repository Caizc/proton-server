package com.zicongcai.persistence.dao;

import com.zicongcai.persistence.po.UserPo;
import org.springframework.stereotype.Repository;

/**
 * user 对象的 DAO
 */
@Repository("userDao")
public class UserDao extends BaseDao<UserPo, String> {
    // nothing to do here.
}
