package com.imyiren.netty.log4j;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @author yiren
 */
public class NettyClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new LongToByteEncoder());
        // pipeline.addLast(new ByteToLongDecoder());
        pipeline.addLast(new ByteToLongReplayingDecoder());
        pipeline.addLast(new NettyClientHandler());
    }
}
