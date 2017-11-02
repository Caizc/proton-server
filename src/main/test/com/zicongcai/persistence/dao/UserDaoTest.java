package com.zicongcai.persistence.dao;

import com.googlecode.genericdao.search.Search;
import com.zicongcai.persistence.po.UserPo;
import com.zicongcai.thirdparty.JUnit4ClassRunner;
import com.zicongcai.thirdparty.SpringContextHolder;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.logicalcobwebs.proxool.configuration.PropertyConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

@RunWith(JUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:conf/spring/applicationContext.xml"})
public class UserDaoTest {

    @Autowired
    private UserDao userDao;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {

        String classPath = JUnit4ClassRunner.class.getClassLoader().getResource("").getPath();
        String configDir = classPath + "conf" + File.separator;

        // proxool配置文件路径
        String proxoolPropertiesPath = configDir + "hibernate" + File.separator + "proxool.properties";

        PropertyConfigurator.configure(proxoolPropertiesPath);
    }

    @Before
    public void setUp() throws Exception {
        System.out.println("------------- 测试开始 -------------");
    }

    @After
    public void tearDown() throws Exception {
        System.out.println("------------- 测试结束 -------------");
    }

    @Test
    public void test() {

        Search search = new Search();
        search.addFilterEqual("id", "abc");

        List<UserPo> list = userDao.search(search);

        if (list == null || list.size() == 0) {
            System.out.println("Got nothing!");
        } else {
            for (UserPo userPo : list) {
                System.out.println(userPo.toString());
            }
        }
    }
}
