package com.zicongcai.core;

import com.zicongcai.logic.PlayerData;
import com.zicongcai.thirdparty.JUnit4ClassRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.logicalcobwebs.proxool.configuration.PropertyConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@RunWith(JUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:conf/spring/applicationContext.xml"})
public class DataManagerTest {

    @Autowired
    private DataManager dataManager;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        String classPath = JUnit4ClassRunner.class.getClassLoader().getResource("").getPath();
        PropertyConfigurator.configure(classPath + "/conf/hibernate/proxool.properties");
    }

    @Before
    public void setUp() throws Exception {
        System.out.println("------------- 测试开始 -------------");
    }

    @After
    public void tearDown() throws Exception {
        System.out.println("------------- 测试结束 -------------");
    }

    public void registerTest() {
        dataManager.register("guest2", "guest2");
    }

    public void createPlayerTest() {
        dataManager.createPlayer("guest");
    }


    public void checkPasswordTest() {
        dataManager.checkPassword("guest", "guest");
    }

    @Test
    public void getPlayerDataTest() {

        PlayerData playerData = dataManager.getPlayerData("guest");
        System.out.println(playerData.score);
    }

    public void savePlayerTest() {

        Player player = new Player("kitty", null);
        PlayerData playerData = new PlayerData();
        playerData.score = 101;

        player.setId("guest");
        player.setData(playerData);

        dataManager.savePlayer(player);
    }
}
