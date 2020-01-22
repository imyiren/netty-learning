package com.imyiren.netty.heartbeat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

/**
 * @author yiren
 */
public class GroupChatClient {
    private String host;
    private Integer port;

    public GroupChatClient(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    public void run() throws InterruptedException {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {

            Bootstrap bootstrap = new Bootstrap();

            bootstrap
                    .group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //得到pipeline
                            ChannelPipeline pipeline = ch.pipeline();
                            //加入相关handler
                            pipeline.addLast("decoder", new StringDecoder());
                            pipeline.addLast("encoder", new StringEncoder());
                            //加入自定义的handler
                            pipeline.addLast(new GroupChatClientHandler());
                        }
                    });

            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();

            channelFuture.addListener(future -> {
                if (channelFuture.isSuccess()) {
                    System.out.println("client " + channelFuture.channel().localAddress() + " is ready!");
                } else {
                    System.out.println("client connect " + host + ":" + port + " failed!");
                }
            });

            //得到channel
            Channel channel = channelFuture.channel();
            //客户端需要输入信息，创建一个扫描器
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.print("请输入（e退出）：");
                String msg = scanner.nextLine();
                if (msg.contains("e")) {
                    break;
                }
                //通过channel 发送到服务器端
                channel.writeAndFlush(msg);
            }

            channel.closeFuture().sync();

        } finally {
            eventLoopGroup.shutdownGracefully();
        }

    }

}
