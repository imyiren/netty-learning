package com.imyirem.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * @author yiren
 */
public class ScatteringGatheringDemo {
    public static void main(String[] args) throws IOException {
        // scattering 将数据写入到buffer时，可以采用buffer数组，依次写入 【分散】
        // gathering  从buffer读取数据时，可以采用buffer数组，依次读

        // 使用ServerSocketChannel 和 SocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);

        // 绑定端口到socket并启动
        serverSocketChannel.bind(inetSocketAddress);

        // 创建一个buffer数组
        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);

        // 等待客户端链接
        SocketChannel socketChannel = serverSocketChannel.accept();

        // 循环读取
        // 假定从客户端接收8字节
        int messageLength = 8;
        while (true) {
            int byteRead = 0;
            while (byteRead < messageLength) {
                long read = socketChannel.read(byteBuffers);
                byteRead += read;
                System.out.println("byteRead: " + byteRead);
                // 使用流打印
                Arrays.stream(byteBuffers)
                        .map(buffer -> "position = " + buffer.position() + ", limit = " + buffer.limit())
                        .forEach(System.out::println);
            }
            // 反转
            Arrays.asList(byteBuffers).forEach(Buffer::flip);

            // 将数据读出，显示到console
            long byteWrite = 0;
            while (byteWrite < messageLength) {
                long write = socketChannel.write(byteBuffers);
                byteWrite += write;
            }

            // 重置参数
            Arrays.asList(byteBuffers).forEach(Buffer::clear);

            System.out.println("byteRead: " + byteRead + ", byteWrite: " + byteWrite);

        }


    }
}
