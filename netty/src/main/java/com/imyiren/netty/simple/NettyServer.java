package com.imyiren.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {
    public static void main(String[] args) throws InterruptedException {
        // 创建BossGroup 和 WorkGroup
        // 1. bossGroup 只处理连接请求 真正和客户端处理业务 会 交给workGroup
        // 2. 两个group都是无限循环
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {

            // 穿件服务器端的启动对象，配置参数
            ServerBootstrap bootstrap = new ServerBootstrap();

            // 使用链式编程设置
            bootstrap
                    // 设置两个线程组
                    .group(bossGroup, workGroup)
                    // 设置服务器通道使用类型
                    .channel(NioServerSocketChannel.class)
                    // 设置线程队列得到连接个数
                    .option(ChannelOption.SO_BACKLOG, 128)
                    // 设置保持活动连接状态
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    // 给workGroup 的EventLoop 对应的管道设置处理器 创建一个通道 初始化对象（使用匿名对象）。
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        /**
                         * 给pipeline设置处理器
                         * @param socketChannel
                         * @throws Exception
                         */
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            // 向管道最后添加处理器
                            pipeline.addLast(new NettyServerHandler());
                        }
                    });

            System.out.println("Server is ready!");
            // 绑定一个端口，并且同步，生成一个ChannelFuture对象 | 这里相当于启动服务器了
            ChannelFuture channelFuture = bootstrap.bind(6668).sync();

            // 对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }

    }
}
