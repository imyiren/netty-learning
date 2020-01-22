package com.imyiren.netty.heartbeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author yiren
 */
public class HeartBeatServer {
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup parentGroup = new NioEventLoopGroup();
        EventLoopGroup childGroup = new NioEventLoopGroup();


        ServerBootstrap bootstrap = new ServerBootstrap();

        bootstrap
                .group(parentGroup, childGroup)
                .channel(NioServerSocketChannel.class)
                // 在parentGroup中加一个日志处理器
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        // 加入一个netty提供的IdleStateHandler
                        //(1. 处理空闲状态的处理器，
                        // 2. readerIdleTime, 表示多长时间没有读取数据，超时就会发送一个心跳检测包，检测是否连接的状态,
                        // 3. writerIdleTime, 表示多长时间没有写，超时就会发一个心跳检测包，检测是否连接的状态,
                        // 4. allIdleTime, 表示多长事件没有读写，超时就会发心跳检测包，检测是否连接的状态,
                        // JDK doc : Triggers an {@link IdleStateEvent} when a {@link Channel} has not performed read, write, or both operation for a while.
                        // 当IdleStateEvent触发后，就会传递给管道的下一个handler 不处理 通过调用触发下一个handler的userEventTiggered 在该方法中去处理IdleStateEvent（三种情况都有可能）
                        pipeline.addLast(new IdleStateHandler(3, 5, 7, TimeUnit.SECONDS));
                        // 加入一个对空闲检测的自定义handler
                        pipeline.addLast(new HeartBeatServerHandler());
                    }
                });

        ChannelFuture channelFuture = bootstrap.bind(7001).sync();
        channelFuture.addListener(future -> {
            if (channelFuture.isSuccess()) {
                System.out.println("server started. bind 7001 success!");
            } else {
                System.out.println("server start failed.");
            }
        });
        channelFuture.channel().closeFuture().sync();
    }
}
