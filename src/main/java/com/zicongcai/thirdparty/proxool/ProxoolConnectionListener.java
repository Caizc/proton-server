package com.zicongcai.thirdparty.proxool;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.logicalcobwebs.proxool.ConnectionListenerIF;
import org.logicalcobwebs.proxool.util.AbstractListenerContainer;

/**
 * 连接监听器
 */
public class ProxoolConnectionListener extends AbstractListenerContainer implements
        ConnectionListenerIF {

    private static final Log log = LogFactory.getLog(ProxoolConnectionListener.class);

    @Override
    public void onBirth(Connection connection) throws SQLException {
        log.debug("====== New Proxool Connection created!");
    }

    @Override
    public void onDeath(Connection connection, int reasonCode) throws SQLException {

    }

    @Override
    public void onExecute(String command, long elapsedTime) {
        // FIXME:
        log.debug(String.format("====== Execute: [%s]", command));
    }

    @Override
    public void onFail(String command, Exception exception) {

    }

}
