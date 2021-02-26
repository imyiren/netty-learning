package com.imyiren.gateway.handler;

import com.imyiren.gateway.common.Const;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * mailto:yiren.dev@gmail.com
 *
 * @author yiren
 * @date 2021/2/16
 */
public class HttpHandlerInitializer  extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        final ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpObjectAggregator(Const.C_1MB));
        pipeline.addLast(new HttpInboundHandler());
    }

}
