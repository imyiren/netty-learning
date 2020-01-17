package com.imyirem.nio.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @author feng
 */
public class GroupChatServer {

    private Selector selector;

    private ServerSocketChannel listenChannel;

    public static final int PORT = 6667;

    public GroupChatServer() {
        // 初始化工作
        try {
            // 得到选择器
            selector = Selector.open();
            // 初始化ServerSocketChannel
            listenChannel = ServerSocketChannel.open();
            // 绑定端口
            listenChannel.socket().bind(new InetSocketAddress(PORT));

            // 设置非阻塞模式
            listenChannel.configureBlocking(false);

            // 将listenChannel注册到Selector
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listen() {
        try {
            while (true) {
                int count = selector.select(2000);
                if (count < 0) {
                    System.out.println("等待事件发生");
                    continue;
                }
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();

                    // 如果是ACCEPT
                    if (key.isAcceptable()) {
                        // accept方法是阻塞的，但是这个时候已经知道连接进来了，所以不会阻塞
                        SocketChannel socketChannel = listenChannel.accept();
                        socketChannel.configureBlocking(false);
                        // 将socketChannel 注册到selector，关注事件为OP_READ，同时给socketChannel关联一个Buffer
                        socketChannel.register(selector, SelectionKey.OP_READ);
                        System.out.println("/" + socketChannel.getRemoteAddress() + " 上线");

                    }

                    // READ 处理读
                    if (key.isReadable()) {
                        readKey(key);
                    }

                    // 当前这个key删除 防止重复处理
                    iterator.remove();

                }

            }
        } catch (IOException e) {
            e.printStackTrace();

        } finally {

        }
    }

    private void readKey(SelectionKey key) {
        SocketChannel channel = null;
        try {
            channel = (SocketChannel) key.channel();
            // 创建缓冲
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            int len = channel.read(byteBuffer);
            if (len < 0) {
                return;
            }
            String msg = new  String(byteBuffer.array(), 0, len);
            System.out.println("from client " + channel.getRemoteAddress() + " : " + msg);

            sendInfo2otherChannel(msg, channel);

        } catch (IOException e) {
            try {
                System.out.println(channel.getRemoteAddress() + " 可能离线了");
                // 取消注册
                key.cancel();
                // 关闭这个通道
                channel.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void sendInfo2otherChannel(String msg, SocketChannel self) throws IOException {
        System.out.println("服务器转发消息中......");

        // 所有注册到selector上的channel发一遍  除去自己
        Set<SelectionKey> keys = selector.keys();
        for (SelectionKey key : keys) {
            Channel channel = null;
            if ((channel = key.channel()) instanceof SocketChannel && self != channel) {
                SocketChannel socketChannel = (SocketChannel) channel;
                ByteBuffer byteBuffer = ByteBuffer.wrap(msg.getBytes());
                socketChannel.write(byteBuffer);
            }
        }
    }

    public static void main(String[] args) {

    }
}
