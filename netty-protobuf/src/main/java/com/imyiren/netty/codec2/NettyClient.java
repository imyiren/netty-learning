package com.imyiren.netty.codec2;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufEncoder;

import java.net.InetSocketAddress;

/**
 * @author yiren
 */
public class NettyClient {
    public static void main(String[] args) throws InterruptedException {
        // 客户端需要一个时间循环组
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {

            // 创建客户端启动对象 客户端使用的是Bootstrap 不是ServerBootstrap
            Bootstrap bootstrap = new Bootstrap();

            // 设置相关参数
            bootstrap
                    // 设置线程组
                    .group(eventLoopGroup)
                    // 设置客户端通道的实现类
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {

                            // 在pipeline中加入ProtoBufEncoder
                            ch.pipeline().addLast("encoder", new ProtobufEncoder());
                            ch.pipeline().addLast(new NettyClientHandler());

                        }
                    });
            System.out.println("client is ok");
            // channelFuture涉及到netty异步模型
            ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress("127.0.0.1", 6668)).sync();
            // 给关闭的通道进行监听
            channelFuture.channel().closeFuture().sync();

        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }
}
