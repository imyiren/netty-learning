package com.imyirem.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author yiren
 */
public class NIOClient {

    public static void main(String[] args) throws IOException {

        // 得到一个网络通道
        SocketChannel socketChannel = SocketChannel.open();

        // 设置非阻塞模式
        socketChannel.configureBlocking(false);

        // 提供服务器端的ip和端口
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 6666);

        if (!socketChannel.connect(inetSocketAddress)) {
            while (!socketChannel.finishConnect()) {
                System.out.println("因为连接需要时间，客户端不会阻塞，可以做其他工作。");
            }
        }
        // 连接成功 就发送数据
        String str = "hello nio!";
        ByteBuffer byteBuffer = ByteBuffer.wrap(str.getBytes());
        // 讲buffer数据写入channel
        socketChannel.write(byteBuffer);

        System.out.println("结束！");

        int read = System.in.read();
        System.out.println(read);

    }
}
