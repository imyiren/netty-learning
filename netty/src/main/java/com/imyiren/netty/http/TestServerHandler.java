package com.imyiren.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

/**
 * SimpleChannelInboundHandler 是 ChannelInboundHandlerAdapter的子类
 * HttpObject 表示的是，客户端和服务端相互通信的数据被封装成HttpObject
 * @author feng
 */
public class TestServerHandler extends SimpleChannelInboundHandler<HttpObject> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        // 判断msg 是不是httpRequest请求
        if (msg instanceof HttpRequest) {
            System.out.println("msg type: " + msg.getClass());
            System.out.println("client ip: " + ctx.channel().remoteAddress());

            HttpRequest request = (HttpRequest) msg;
            URI uri = new URI(request.uri());

            if ("/favicon.ico".equals(uri.getPath())) {
                System.out.println("request favicon.ico");
                return;
            }

            ByteBuf content = Unpooled.copiedBuffer("Hello, I'm server.", CharsetUtil.UTF_8);

            // 构造一个Http响应，即HttpResponse
            HttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());

            // 将构建好的response
            ctx.writeAndFlush(response);
        }

    }
}
