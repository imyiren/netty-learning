package com.imyiren.netty.bound;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @author yiren
 */
public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // pipeline.addLast(new ByteToLongDecoder());
        pipeline.addLast(new ByteToLongReplayingDecoder());
        pipeline.addLast(new LongToByteEncoder());
        pipeline.addLast(new NettyServerHandler());
    }
}
