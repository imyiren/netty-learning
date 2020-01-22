package com.imyiren.netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author yiren
 */
public class WebSocketServer {
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
                        // 因为基于Http协议，所以使用http的编解码器
                        pipeline.addLast(new HttpServerCodec());
                        // 是以块方式写 需要添加ChunkedWriteHandler
                        pipeline.addLast(new ChunkedWriteHandler());
                        /*
                            1. http数据在传输过程中是分段的， HttpObjectAggregator 就是可以将多个段聚合
                            2. 这就是为什么当浏览器发送大量数据时，就会发出多次http请求
                         */
                        pipeline.addLast(new HttpObjectAggregator(8192));
                        /*
                            1. 对于websocket 它的数据时以 帧 的形式传递的
                            2. 可以看到WebSocketFrame 下面有六个子类
                            3. 浏览器发送请求时，ws://localhost:port/hello 表示请求的uri
                            4. WebSocketServerProtocolHandler核心功能，把http协议升级成ws协议，保持长连接。
                         */
                        pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));

                        // 自定义handler
                        pipeline.addLast(new WebSocketServerFrameHandler());
                    }
                });

        ChannelFuture channelFuture = bootstrap.bind(7002).sync();
        channelFuture.addListener(future -> {
            if (channelFuture.isSuccess()) {
                System.out.println("server started. bind 7002 success!");
            } else {
                System.out.println("server start failed.");
            }
        });
        channelFuture.channel().closeFuture().sync();
    }

}
