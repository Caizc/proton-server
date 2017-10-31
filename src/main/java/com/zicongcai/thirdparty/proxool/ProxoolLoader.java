package com.zicongcai.thirdparty.proxool;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.logicalcobwebs.proxool.ProxoolException;

/**
 * proxool加载类
 */
public class ProxoolLoader {

    private static final Log log = LogFactory.getLog(ProxoolLoader.class);

    /**
     * 加载proxool配置文件
     *
     * @param propertiesFilePath 配置文件路径
     */
    public static void load(String propertiesFilePath) {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(propertiesFilePath));
        } catch (IOException e) {
            log.error(String.format("Couldn't load property file [%s]", propertiesFilePath));
        }
        try {
            if (properties.size() > 0) {
                configure(properties);
            } else {
                log.error(String.format("Couldn't load property file [%s]", propertiesFilePath));
            }
        } catch (Exception e) {
            log.error(String.format("Exception configuring [%s]", propertiesFilePath), e);
        }
    }

    /**
     * 配置proxool
     *
     * @param properties Properties
     * @throws ProxoolException
     */
    private static void configure(Properties properties) throws ProxoolException {
        Map<String, Properties> propertiesMap = new HashMap<String, Properties>();
        Iterator<Object> allPropertyKeysIterator = properties.keySet().iterator();
        Properties proxoolProperties = null;
        Properties globalProperties = new Properties();
        while (true) {
            if (!allPropertyKeysIterator.hasNext()) {
                break;
            }
            String key = (String) allPropertyKeysIterator.next();
            String value = properties.getProperty(key);
            if (key.startsWith("jdbc")) {
                int a = key.indexOf(".");
                if (a == -1) {
                    throw new ProxoolException((new StringBuilder()).append("Property ").append(key)
                            .append(" must be of the format ").append("jdbc*.*").toString());
                }
                String tag = key.substring(0, a);
                String name = key.substring(a + 1);
                proxoolProperties = propertiesMap.get(tag);
                if (null == proxoolProperties) {
                    proxoolProperties = new Properties();
                    propertiesMap.put(tag, proxoolProperties);
                }
                proxoolProperties.put(name, value);
            } else if (key.startsWith("proxool")) {
                int a = key.indexOf(".");
                if (a == -1) {
                    throw new ProxoolException((new StringBuilder()).append("Property ").append(key)
                            .append(" must be of the format ").append("proxool.*").toString());
                }
                String name = key.substring(a + 1);
                globalProperties.put(name, value);
            }
        }
        ProxoolConnectionPoolManager.globalProxoolProperties = globalProperties;
        Iterator<String> tags = propertiesMap.keySet().iterator();
        while (tags.hasNext()) {
            proxoolProperties = (Properties) propertiesMap.get(tags.next());
            String alias = proxoolProperties.getProperty("proxool.alias");
            String driverClass = proxoolProperties.getProperty("proxool.driver-class");
            String driverUrl = proxoolProperties.getProperty("proxool.driver-url");
            String user = proxoolProperties.getProperty("user");
            String password = proxoolProperties.getProperty("password");
            if (null == alias || 0 == alias.length()) {
                throw new ProxoolException("You must define the proxool.alias.");
            }
            if (null == driverClass || 0 == driverClass.length()) {
                throw new ProxoolException("You must define the proxool.driver-class.");
            }
            if (null == driverUrl || 0 == driverUrl.length()) {
                throw new ProxoolException("You must define the proxool.driver-url.");
            }
            if (null == user || 0 == user.length()) {
                throw new ProxoolException("You must define the user.");
            }
            if (null == password || 0 == password.length()) {
                throw new ProxoolException("You must define the password.");
            }
            proxoolProperties.remove("proxool.alias");
            proxoolProperties.remove("proxool.driver-class");
            proxoolProperties.remove("proxool.driver-url");
            proxoolProperties.remove("user");
            proxoolProperties.remove("password");
            proxoolProperties.put("driver", driverClass);
            proxoolProperties.put("url", driverUrl);
            proxoolProperties.put("username", user);
            proxoolProperties.put("password", password);
            ProxoolConnectionPoolManager.setProperties(alias, proxoolProperties);
        }
    }
}
