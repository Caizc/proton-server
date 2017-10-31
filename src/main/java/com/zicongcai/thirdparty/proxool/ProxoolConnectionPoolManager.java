package com.zicongcai.thirdparty.proxool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.logicalcobwebs.proxool.ProxoolDataSource;
import org.logicalcobwebs.proxool.ProxoolFacade;

/**
 * proxool连接池管理器
 */
public class ProxoolConnectionPoolManager {

    private static final Log logger = LogFactory.getLog(ProxoolConnectionPoolManager.class);

    /**
     * proxool连接池全局配置
     */
    public static Properties globalProxoolProperties = new Properties();

    /**
     * Properties Map
     */
    private static ConcurrentHashMap<String, Properties> propertiesMap = new ConcurrentHashMap<String, Properties>();

    /**
     * DataSource Map
     */
    private static ConcurrentHashMap<String, ProxoolDataSource> dataSourceMap = new ConcurrentHashMap<String, ProxoolDataSource>();

    /**
     * 根据连接池ID获取Properties
     *
     * @param poolId 连接池ID
     * @return Properties
     */
    public static Properties getProperties(String poolId) {
        return propertiesMap.get(poolId);
    }

    /**
     * 添加Properties
     *
     * @param poolId     连接池ID
     * @param properties Properties
     */
    public static synchronized void setProperties(String poolId, Properties properties) {

        try {

            // 判断PropertiesMap中是否已存在该连接池ID
            // 如果不存在，则向该连接池注册该连接，并将相应Properties添加到Properties Map中
            if (!propertiesMap.containsKey(poolId)) {

                String dbUrl = properties.getProperty("url");
                String user = properties.getProperty("username");
                String password = properties.getProperty("password");
                String driver = properties.getProperty("driver");

                Class.forName("org.logicalcobwebs.proxool.ProxoolDriver");

                Properties info = new Properties();

                // 用户名
                info.setProperty("user", user);
                // 密码
                info.setProperty("password", password);

                // 设置proxool连接池全局配置
                Iterator<Object> allPropertyKeysIterator = globalProxoolProperties.keySet().iterator();
                while (allPropertyKeysIterator.hasNext()) {
                    String key = (String) allPropertyKeysIterator.next();
                    info.setProperty("proxool." + key, (String) globalProxoolProperties.get(key));
                }

                String url = "proxool." + poolId + ":" + driver + ":" + dbUrl;

                // 向连接池注册该连接
                ProxoolFacade.registerConnectionPool(url, info);

                // 注册自定义的连接监听器
                ProxoolFacade.addConnectionListener(poolId, new ProxoolConnectionListener());

                propertiesMap.put(poolId, properties);
            }// end if

        } catch (Exception e) {
            logger.error("[Connection Pool]: SetProperties error :" + poolId, e);
            throw new RuntimeException("SetProperties error :" + poolId, e);
        }// end try

    }

    /**
     * 根据连接池ID获取数据源
     *
     * @param poolId 连接池ID
     * @return 数据源
     */
    public static synchronized ProxoolDataSource getDataSource(String poolId) {

        ProxoolDataSource dataSource = dataSourceMap.get(poolId);

        if (null != dataSource) {
            return dataSource;
        } else {

            try {
                Properties properties = propertiesMap.get(poolId);
                if (null != properties) {

                    String driverUrl = properties.getProperty("url");
                    String user = properties.getProperty("username");
                    String password = properties.getProperty("password");
                    String driver = properties.getProperty("driver");
                    String initialPoolSize = properties.getProperty("prototype-count");
                    String minPoolSize = properties.getProperty("minimum-connection-count");
                    String maxPoolSize = properties.getProperty("maximum-connection-count");
                    String simultaneousBuildThrottle = properties.getProperty("simultaneous-build-throttle");

                    dataSource = new ProxoolDataSource();
                    dataSource.setAlias(poolId);
                    dataSource.setUser(user);
                    dataSource.setPassword(password);
                    dataSource.setDriverUrl(driverUrl);
                    dataSource.setDriver(driver);

                    if (null == initialPoolSize || 0 == initialPoolSize.length()) {
                        dataSource.setPrototypeCount(5);
                    } else {
                        dataSource.setPrototypeCount(Integer.parseInt(initialPoolSize));
                    }
                    if (null == minPoolSize || 0 == minPoolSize.length()) {
                        dataSource.setMinimumConnectionCount(5);
                    } else {
                        dataSource.setMinimumConnectionCount(Integer.parseInt(minPoolSize));
                    }
                    if (null == maxPoolSize || 0 == maxPoolSize.length()) {
                        dataSource.setMaximumConnectionCount(200);
                    } else {
                        dataSource.setMaximumConnectionCount(Integer.parseInt(maxPoolSize));
                    }
                    if (null == simultaneousBuildThrottle || 0 == simultaneousBuildThrottle.length()) {
                        dataSource.setSimultaneousBuildThrottle(200);
                    } else {
                        dataSource.setSimultaneousBuildThrottle(Integer.parseInt(simultaneousBuildThrottle));
                    }

                    dataSource.setMaximumActiveTime(18000000);
                    dataSource.setMaximumConnectionLifetime(18000000);
                    dataSource.setHouseKeepingSleepTime(90000);
                    dataSource.setTestBeforeUse(true);
                    dataSource.setHouseKeepingTestSql("SELECT SYSDATE FROM DUAL");
                    dataSource.setTrace(true);

                    dataSourceMap.put(poolId, dataSource);

                    return dataSource;
                } else {
                    logger.error("[Connection Pool]: Properties not found in pool: " + poolId);
                    throw new RuntimeException("Properties not found in pool: " + poolId);
                }// end if
            } catch (Exception e) {
                logger.error("[Connection Pool]: Datasource generate error: " + poolId, e);
                throw new RuntimeException("Datasource generate error: " + poolId, e);
            }// end try

        }// end if

    }

    /**
     * 根据连接池ID获取连接
     *
     * @param poolId 连接池ID
     * @return 连接
     * @throws SQLException
     */
    public static Connection getConnection(String poolId) throws SQLException {

        logger.debug("[Connection Pool]: The current propertiesMap size:[" + propertiesMap.size() + "]");

        Connection connection = DriverManager.getConnection("proxool." + poolId);

        return connection;
    }

    public static boolean isInDataSourcePool(String poolId) {
        boolean isIn = false;
        if (poolId != null) {
            synchronized (dataSourceMap) {
                if (dataSourceMap.containsKey(poolId)) {
                    isIn = true;
                }
            }
        }
        return isIn;
    }
}
