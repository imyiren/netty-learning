package com.imyiren.netty.chat;

/**
 * @author yiren
 */
public class Client2 {

    public static void main(String[] args) throws Exception {
        new GroupChatClient("127.0.0.1", 7000).run();

    }
}
