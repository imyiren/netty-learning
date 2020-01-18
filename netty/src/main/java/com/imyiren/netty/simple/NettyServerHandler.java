package com.imyiren.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;


/**
 * 自定义一个handler， 需要继承netty规定好的 某个 HandlerAdapter
 * 这时我们自定义的Handler，才能称为handler。 ChannelInboundHandlerAdapter:
 * @author feng
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 这里可以读取客户端发送的数据
     *
     * @param ctx 上下文对象：含有管道pipeline 、 通道channel 、 地址等
     * @param msg 客户端发送的数据
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("server ctx : " + ctx);
        // 将msg 转成一个byteBuf 这个是netty提供的 （不是NIO的ByteBuffer）
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("client msg : " + buf.toString(CharsetUtil.UTF_8));
        System.out.println("client ip  : " + ctx.channel().remoteAddress());
    }

    /**
     * 数据读取完毕
     *
     * @param ctx 上下文对象
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 把数据写入缓冲然后刷新缓冲 一般讲 我们要对这个发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello, Client", CharsetUtil.UTF_8));

    }


    /**
     * 异常处理 一般是需要关闭通道
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
