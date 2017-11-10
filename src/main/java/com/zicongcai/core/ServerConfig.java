package com.zicongcai.core;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

/**
 * 服务端程序配置
 */
@Service("serverConfig")
@PropertySource("classpath:conf/server.properties")
public class ServerConfig {

    @Value("${server.socket.ip}")
    public String ip;

    @Value("${server.socket.port}")
    public int port;

    @Value("${server.maximum-connection-count}")
    public int maxConnectionCount;

    @Override
    public String toString() {

        StringBuffer sb = new StringBuffer();
        sb.append("\n");
        sb.append("=== Server Configuration ===").append("\n");
        sb.append("server.socket.ip = ").append(ip).append("\n");
        sb.append("server.socket.port = ").append(port).append("\n");
        sb.append("server.maximum-connection-count = ").append(maxConnectionCount).append("\n");
        sb.append("============================");

        return sb.toString();
    }
}
