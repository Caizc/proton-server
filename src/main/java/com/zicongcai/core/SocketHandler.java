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
 * 处理接收到的客户端 Socket 连接
 */
public class SocketHandler implements Callable<Object> {

    private static final Log log = LogFactory.getLog(SocketHandler.class);

    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    // FIXME: 暂时先使用字节流协议
    private Protocol defaultProto = new ProtocolBytes();

    /**
     * 构造方法
     *
     * @param socket 接收到的客户端 Socket 连接
     */
    public SocketHandler(Socket socket) {

        this.socket = socket;

        try {
            this.dataInputStream = new DataInputStream(socket.getInputStream());
            this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            log.error("DataInputStream/DataOutputStream error!", e);
        }
    }

    @Override
    public Object call() {

        // 构造客户端连接实例
        Connection conn = new Connection(socket);

        log.warn("=== Got a new Connection from: " + conn.getClientName());

        // 不断读取并处理客户端 Socket 连接中的数据流
        while (true) {
            try {

                if (socket.isClosed()) {
                    log.info("Connection [" + conn.getClientName() + "] has been CLOSED!");
                    break;
                }

                if (dataInputStream.available() == 0) {
                    continue;
                }

                // 读取消息包
                readMessage(conn);

            } catch (Exception e) {
                log.error("There was an error occur!", e);
            }
        }

        try {
            dataInputStream.close();
            dataOutputStream.close();
            socket.close();
        } catch (IOException e) {
            log.error("Error occur when closing Socket/DataInputStream/DataOutputStream!", e);
        }

        // FIXME: 返回值为 null 不好吧？
        return null;
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

        String name = msgProto.getName();
        String methodName = "Msg" + name;

        log.info("=== " + methodName);

        // 将消息传递给相应的消息处理类
        MessageDispatcher.getInstance().deliverMessage(name, conn, msgProto);
    }
}
