package com.zicongcai;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service("MainClass")
public class Main {
    
    private static final Log log = LogFactory.getLog(Main.class);

    @PostConstruct
    public void init() {
        log.info("====== Proton Server STARTING UP... ======");

        // do something...

        log.info("====== Proton Server is READY! ======");
    }
}
