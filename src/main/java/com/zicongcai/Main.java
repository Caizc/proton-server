package com.zicongcai;

import com.zicongcai.core.NetworkManager;
import com.zicongcai.core.ServerConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service("MainClass")
public class Main {

    private static final Log log = LogFactory.getLog(Main.class);

    /**
     * 应用是否已经就绪
     */
    public static boolean isReady = false;

    @Autowired
    private ServerConfig serverConfig;

    @PostConstruct
    public void init() {

        log.info("====== Proton Server STARTING UP... ======");

        try {

            // 打印配置信息
            printServerConfig();

            // 启动 Socket 监听器
            NetworkManager.getInstance().start();

            isReady = true;

            log.info("====== Proton Server is READY! ======");
        } catch (Exception e) {
            log.error("There was an error occur!", e);
        }
    }

    /**
     * 打印服务端配置信息
     */
    private void printServerConfig() {
        log.info(serverConfig.toString());
    }
}
