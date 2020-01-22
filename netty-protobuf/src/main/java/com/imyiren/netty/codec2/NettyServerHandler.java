package com.imyiren.netty.codec2;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;


/**
 * @author yiren
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<DataInfo.Message> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DataInfo.Message msg) throws Exception {
        System.out.println(msg);
    }
}
