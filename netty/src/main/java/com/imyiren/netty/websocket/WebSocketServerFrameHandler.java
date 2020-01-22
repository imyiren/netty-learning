package com.imyiren.netty.websocket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author feng
 */
public class WebSocketServerFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        System.out.println("server receive msg : " + msg.text());

        TextWebSocketFrame textWebSocketFrame = new TextWebSocketFrame(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " : " + msg.text());

        // 回复消息
        ctx.channel().writeAndFlush(textWebSocketFrame);

    }

    /**
     * 当web客户端连接后 触发
     * @param ctx 上下文
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        /*
        channel.id().asLongText() 是唯一的
        channel.id().asShortText() 不是唯一的
         */
        System.out.println("handlerAdded " + channel.id().asLongText() + " " + channel.remoteAddress());

    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println("handlerRemoved " + channel.id().asLongText() + " " + channel.remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("exception " + cause.getMessage());
        ctx.close();
    }
}
