package com.imyiren.gateway;

import com.imyiren.gateway.common.Const;
import com.imyiren.gateway.handler.HttpHandlerInitializer;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * mailto:yiren.dev@gmail.com
 *
 * @author yiren
 * @date 2021/2/15
 */
public class GatewayServer {
    private static final Logger logger = LoggerFactory.getLogger(GatewayServer.class);

    private int port = 8888;
    private boolean run = false;

    public void run() {
        final int processors = Runtime.getRuntime().availableProcessors();
        EventLoopGroup bossGroup = new NioEventLoopGroup(processors < Const.PROCESSORS_THRESHOLD ? Const.BOSS_GROUP_DEFAULT : processors / 4);
        EventLoopGroup workerGroup = new NioEventLoopGroup(processors);
        try {
            final ServerBootstrap bootstrap = init(bossGroup, workerGroup);
            final ChannelFuture channelFuture = bootstrap.bind(port).sync();
            channelFuture.addListener(future -> {
                if (channelFuture.isSuccess()) {
                    logger.debug("Gateway Server run success, port: {}", port);
                }
            });
            channelFuture.channel().closeFuture().sync();
            run = true;
        } catch (Throwable e) {
            logger.error("Gateway Server run error occurred! err: ", e);
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private ServerBootstrap init(EventLoopGroup bossGroup, EventLoopGroup workerGroup) {
        ServerBootstrap bootstrap = new ServerBootstrap();
        return bootstrap.option(ChannelOption.SO_BACKLOG, Const.C_128)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.SO_RCVBUF, Const.C_32KB)
                .option(EpollChannelOption.SO_REUSEPORT, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new HttpHandlerInitializer())
                .channel(NioServerSocketChannel.class)
                .group(bossGroup, workerGroup);
    }

    public GatewayServer(int port) {
        this.port = port;
    }

    public GatewayServer() {
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        if (run) {
            throw new RuntimeException("Gateway Server has already run!");
        }
        this.port = port;
    }
}
