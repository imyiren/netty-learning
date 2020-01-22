package com.imyirem.nio.zerocopy;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

/**
 * @author feng
 */
public class NIOClient {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 7001));
        String filename = "";

        FileInputStream fileInputStream = new FileInputStream(filename);
        FileChannel fileChannel = fileInputStream.getChannel();


        long startTime = System.currentTimeMillis();

        /*
            linux transferTo 一次调用即可完成
            windows 下每次只能最大发送8M，所以需要分段传输 而且要注意传输时的位置。

            transferTo 底层就是使用的零拷贝
        */

        long count = fileChannel.transferTo(0, fileChannel.size(), socketChannel);

        System.out.println("发送总字节数： " + count + ", 耗时： " + (System.currentTimeMillis() - startTime));

    }
}
