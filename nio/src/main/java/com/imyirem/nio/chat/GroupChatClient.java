package com.imyirem.nio.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 * @author feng
 */
public class GroupChatClient {
    // 服务器ip
    private final String HOST = "127.0.0.1";
    // 端口
    private final int PORT = 6667;

    private Selector selector;

    private SocketChannel socketChannel;

    private String username;

    /**
     * 完成初始化工作
     */
    public GroupChatClient() throws IOException {
        selector = Selector.open();
        // 开始连接服务器
        socketChannel = SocketChannel.open(new InetSocketAddress(HOST, PORT));
        // 非阻塞
        socketChannel.configureBlocking(false);
        // 注册
        socketChannel.register(selector, SelectionKey.OP_READ);

        username = socketChannel.getLocalAddress().toString();

        System.out.println(username + " is ok");

    }

    public void sendMsg(String msg) {
        ByteBuffer buffer = ByteBuffer.wrap((username + " 说 : " + msg).getBytes());
        try {
            socketChannel.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readFromServer() {
        try {
            int count = selector.select();
            if (count <= 0) {
                System.out.println("无可用通道！");
            }
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                if (key.isReadable()) {
                    SocketChannel channel = (SocketChannel) key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    channel.read(buffer);

                    String msg = new String(buffer.array(), 0, buffer.position());
                    System.out.println(msg);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void startClient() throws IOException {
        GroupChatClient groupChatClient = new GroupChatClient();
        new Thread(() -> {
            while (true) {
                groupChatClient.readFromServer();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String msg = scanner.nextLine();
            groupChatClient.sendMsg(msg);
        }
    }

}
