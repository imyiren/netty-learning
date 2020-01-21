package com.imyiren.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @author feng
 */
public class TestServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast("HttpServerCodec", new HttpServerCodec());
        //HttpServerCodec 是netty提供的处理http的编码解码器
        ch.pipeline().addLast("TestServerHandler", new TestServerHandler());

    }
}
