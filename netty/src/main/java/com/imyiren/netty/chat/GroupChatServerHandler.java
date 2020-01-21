package com.imyiren.netty.chat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * @author feng
 */
public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {
    /**
     * 定义一个channel组来管理所有的channel
     * GlobalEventExecutor.INSTANCE 是一个全局的事件执行器， 是一个单例
     */
    private static volatile ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    /**
     * 表示连接建立，一旦连接，这个方法第一个被执行 需要将当前的 channel加入到channelGroup
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        // 将该该客户端 加入聊天的信息，推送给其他的在线客户端  (这个方法会遍历所有的channel并发送信息， 这个ChannelGroup实现了Set接口)
        channelGroup.writeAndFlush("[Client] " + channel.remoteAddress() + " join in group chat. \n");
        channelGroup.add(channel);
    }

    /**
     * 触发这个方法表示channel处于活动状态。提示xx上线
     *
     * @param ctx 上下文
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " online. 😁 " + LocalDateTime.now().format(formatter));
    }

    /**
     * 当channel处于不活动状态，就提示xx下线。
     *
     * @param ctx 上下文
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " offline. 😞");
    }


    /**
     * channel被移除，提醒客户端离开了。
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        // 触发这个方法 channelGroup 中的channel，会被移除。 不需要调用 channelGroup.remove(channel)
        channelGroup.writeAndFlush(LocalDateTime.now().format(formatter) + " [Client] " + channel.remoteAddress() + " leave. 😞");
    }

    /**
     * 读取数据 并转发消息给所有人
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();
        // 遍历channelGroup根据不同情况 回送不同消息，主要是去除自己
        channelGroup.forEach(ch -> {
            // 不是当前的channel。
            if (channel != ch) {
                ch.writeAndFlush(LocalDateTime.now().format(formatter) + " [Client] " + channel.remoteAddress() + " : " + msg);
            } else {
                ch.writeAndFlush(LocalDateTime.now().format(formatter) + " [Client] " + channel.remoteAddress() + " send msg : \"" + msg + " \" success!");
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        System.out.println("Client " + ctx.channel().remoteAddress() + " closing... occur error : " + cause.getMessage());
    }
}
