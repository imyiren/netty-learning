package com.imyirem.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @author feng
 */
public class NIOServer {

    public static void main(String[] args) throws IOException {
        // 创建server socket channel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 得到一个Selector对象
        Selector selector = Selector.open();
        // 绑定端口
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));
        // 设置成非阻塞
        serverSocketChannel.configureBlocking(false);
        // 把serverSocketChannel注册到selector 关心事件为OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        // 循环等待客户端连接
        while (true) {
            // 这里我们等待一秒 没有连接事件发生，就返回
            if (selector.select(1000) == 0) {
                System.out.println("服务器等待了 1s 无连接");
                continue;
            }

            // 如果返回的不是 > 0 就获取到相关的selectionKey集合
            // 1、如果返回大于0 就代表已经获取到关注的事件
            // 2、selector.selectedKeys() 返回的是关注事件的集合。
            //      通过selectionKey 可以反向获取通道
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();

                // 根据key通道发生的事情a，来做对应的处理
                // 新的客户端连接 给客户端生成一个socketChannel
                if (selectionKey.isAcceptable()) {
                    // accept方法是阻塞的，但是这个时候已经知道连接进来了，所以不会阻塞
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    System.out.println("连接+1： " + socketChannel);
                    // 将socketChannel 注册到selector，关注事件为OP_READ，同时给socketChannel关联一个Buffer
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));

                }

                // 发生OP_READ
                if (selectionKey.isReadable()) {
                    // 通过key反向获取到对应的channel
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    // 获取该channel关联的buffer
                    ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();
                    // 读取客户端发送过来的数据
                    socketChannel.read(buffer);

                    System.out.println("客户端发送过来的数据：" + new String(buffer.array(), 0, buffer.position()));
                }

                // 处理完成从集合中移除这个key 防止重复操作。
                iterator.remove();

            }


        }


    }
}
