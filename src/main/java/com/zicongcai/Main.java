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

    @Autowired
    private ServerConfig serverConfig;

    @PostConstruct
    public void init() {
        log.info("====== Proton Server STARTING UP... ======");

        try {

            printServerConfig();

            NetworkManager.getInstance().start();

        } catch (Exception e) {
            log.error("There was an error occur!", e);
        }

        log.info("====== Proton Server is READY! ======");
    }

    /**
     * 打印服务端配置信息
     */
    private void printServerConfig() {
        log.info(serverConfig.toString());
    }
}
