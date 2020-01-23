package com.imyiren.netty.stickunpack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.util.UUID;

/**
 * @author yiren
 */
public class ServerHandler extends SimpleChannelInboundHandler<MessageProtocol> {

    private static int count = 0;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        System.out.println("----------------------------------------------------------------------------");
        count++;
        System.out.println("from client " + ctx.channel().remoteAddress() + "receive msg, count: " + count);
        System.out.println("msg.len : " + msg.getLen());
        System.out.println("msg.content : " + new String(msg.getContent(), CharsetUtil.UTF_8));

        // 回送消息
        String uuid = UUID.randomUUID().toString().replace("-", "");
        byte[] content = uuid.getBytes(CharsetUtil.UTF_8);
        int length = content.length;
        MessageProtocol messageProtocol = new MessageProtocol();
        messageProtocol.setContent(content);
        messageProtocol.setLen(length);
        ctx.writeAndFlush(messageProtocol);
        System.out.println("uuid : " + uuid);
        System.out.println("----------------------------------------------------------------------------\n");

    }
}
