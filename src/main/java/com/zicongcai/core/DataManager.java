package com.zicongcai.core;

import com.googlecode.genericdao.search.Search;
import com.zicongcai.persistence.dao.UserDao;
import com.zicongcai.persistence.po.UserPo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 数据管理器
 */
@Service("dataManager")
public class DataManager {

    @Resource(name = "userDao")
    private UserDao userDao;

    public void test() {

        Search search = new Search();
        search.addFilterEqual("pw", "123");

        List<UserPo> list = userDao.search(search);

        if (list == null || list.size() == 0) {
            System.out.println("Get nothing!");
        } else {
            for (UserPo userPo : list) {
                System.out.println(userPo.toString());
            }
        }
    }
}
