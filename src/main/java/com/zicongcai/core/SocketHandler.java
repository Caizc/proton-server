package com.zicongcai.core;

import com.zicongcai.util.ByteUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Callable;

/**
 * 处理接收到的客户端Socket连接
 */
public class SocketHandler implements Callable<Object> {

    private static final Log log = LogFactory.getLog(SocketHandler.class);

    /**
     * 一个Socket连接对应一个客户端连接实例
     */
    private Connection conn;

    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    // FIXME: 暂时默认使用字节流协议
    private Protocol defaultProto = new ProtocolBytes();

    /**
     * 构造方法
     */
    public SocketHandler(Connection conn) {
        this.conn = conn;
        this.socket = conn.getSocket();
        this.dataInputStream = conn.getDataInputStream();
        this.dataOutputStream = conn.getDataOutputStream();
    }

    @Override
    public Object call() {

        log.info("=== Got a new Connection from: " + conn.getClientName());

        // 不断读取并处理客户端 Socket 连接中的数据流
        while (true) {
            try {

                if (socket.isClosed()) {
                    break;
                }

                if (dataInputStream.available() == 0) {
                    continue;
                }

                // 读取消息包
                readMessage(conn);

            } catch (Exception e) {
                log.error("There was an error occur!", e);
                break;
            }
        }

        // 关闭连接
        conn.close();

        // 将客户端连接引用置空
        conn = null;

        return true;
    }

    /**
     * 读取消息包
     *
     * @param conn 客户端连接实例
     * @throws IOException
     */
    private void readMessage(Connection conn) throws IOException {

        // 读取数据流前 4 个字节的消息包长度
        byte[] msgLenBytes = new byte[4];
        dataInputStream.read(msgLenBytes, 0, msgLenBytes.length);
        int msgLen = ByteUtils.byteArray2Int(msgLenBytes);

        // 根据消息包长度，从输入流中读取完整的消息包
        byte[] msgBytes = new byte[msgLen];
        int readCount = 0;
        while (readCount < msgLen) {
            readCount = readCount + dataInputStream.read(msgBytes);
        }

        // 解码消息包为默认协议内容
        Protocol msgProto = defaultProto.decode(msgBytes, 0, msgLen);

        // 处理消息包，将消息分发传递给相应的处理类
        handleMessage(conn, msgProto);
    }

    /**
     * 处理消息包
     *
     * @param conn     客户端连接实例
     * @param msgProto 消息协议对象
     */
    private void handleMessage(Connection conn, Protocol msgProto) {

        String msgType = msgProto.getName();

        // 将消息传递给相应的消息处理类
        MessageDispatcher.getInstance().deliverMessage(msgType, conn, msgProto);
    }
}
