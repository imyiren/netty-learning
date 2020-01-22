package com.imyiren.netty.bound;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.util.Random;

/**
 * @author yiren
 */
public class NettyClientHandler extends SimpleChannelInboundHandler<Long> {


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        long num = new Random().nextLong();
        ctx.writeAndFlush(num);
        System.out.println("send long : " + num);

        /*
            "abcdabcdabcdabcd" 是16个字节
            1. 前一个处理器是LongToByteEncoder
            2. LongToByteEncoder 的父类 MessageToByteEncoder
            3. MessageToByteEncoder 的 write 方法会处理 数据是否是对应的类型，如果不是则不会处理

            所以下面这个不会走LongToByteEncoder类去编码
         */
        ctx.writeAndFlush(Unpooled.copiedBuffer("abcdabcdabcdabcd", CharsetUtil.UTF_8));

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {
        System.out.println("receive server msg : " + msg);
    }
}
