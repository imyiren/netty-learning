package com.imyiren.netty.log4j;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * @author yiren
 */
public class NettyClient {
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            ChannelFuture channelFuture = bootstrap
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new NettyClientInitializer())
                    .connect(new InetSocketAddress("127.0.0.1", 7005)).sync()
                    .channel().closeFuture().sync();
        }finally {
            group.shutdownGracefully();
        }
    }

}
