package com.imyiren.netty.heartbeat;

import com.imyiren.netty.chat.GroupChatClient;

/**
 * @author feng
 */
public class Client {

    public static void main(String[] args) throws Exception {
        new GroupChatClient("127.0.0.1", 7001).run();

    }
}
