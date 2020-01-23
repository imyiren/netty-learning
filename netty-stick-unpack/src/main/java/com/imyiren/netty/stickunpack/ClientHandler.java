package com.imyiren.netty.stickunpack;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * @author yiren
 */
public class ClientHandler extends SimpleChannelInboundHandler<MessageProtocol> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < 5; i++) {
            byte[] msgBytes = "test 12345678901234567890".getBytes(CharsetUtil.UTF_8);
            int msgByteLength = msgBytes.length;
            MessageProtocol messageProtocol = new MessageProtocol();
            messageProtocol.setLen(msgByteLength);
            messageProtocol.setContent(msgBytes);
            ctx.writeAndFlush(messageProtocol);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        System.out.println("----------------------------------------------------------------------------");
        System.out.println("from server " + ctx.channel().remoteAddress() + "receive uuid");
        System.out.println("msg.len : " + msg.getLen());
        System.out.println("msg.content/uuid : " + new String(msg.getContent(), CharsetUtil.UTF_8));
        System.out.println("----------------------------------------------------------------------------\n");
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(cause.getMessage());
    }
}
