package com.zicongcai.thirdparty.proxool;

import java.io.File;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.zicongcai.Main;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * proxool连接池启动监听器
 */
public class ProxoolListener implements ServletContextListener {

    private static final Log log = LogFactory.getLog(ProxoolListener.class);

    @Override
    public void contextInitialized(ServletContextEvent contextEvent) {
        String classPath = Main.class.getClassLoader().getResource("").getPath();
        String configDir = classPath + "conf" + File.separator;
        // proxool配置文件路径
        String proxoolPropertiesPath = configDir + "hibernate" + File.separator + "proxool.properties";
        try {
            // 加载proxool
            ProxoolLoader.load(proxoolPropertiesPath);
        } catch (Exception e) {
            log.error(String.format("Exception configuring [%s]", proxoolPropertiesPath), e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent contextEvent) {

    }
}
