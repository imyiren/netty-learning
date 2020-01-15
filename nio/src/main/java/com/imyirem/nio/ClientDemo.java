package com.imyirem.nio;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

/**
 * @author feng
 */
public class ClientDemo {
    public static void main(String[] args) throws IOException {
        Scanner input = new Scanner(System.in);
        Socket socket = new Socket("localhost", 7000);
        SocketChannel channel = socket.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        while (true) {
            System.out.println("请输入：");
            String content = input.nextLine();
            buffer.put(content.getBytes());
            channel.write(buffer);
            System.out.println("写入完毕");
            buffer.clear();
            channel.read(buffer);

            String result = new String(buffer.array());
            System.out.println("result: " + result);
        }

    }
}
