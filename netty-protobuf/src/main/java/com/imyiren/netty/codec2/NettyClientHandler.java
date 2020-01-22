package com.imyiren.netty.codec2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.Random;

/**
 * @author feng
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    /**
     * 当通道就绪时 就会触发该方法
     *
     * @param ctx 上下文
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 随机发送Student 或者 Worker 对象
        int random = new Random().nextInt(3);
        DataInfo.Message message = null;

        Channel channel = ctx.channel();
        if (0 == random) {
            channel.writeAndFlush(DataInfo.Message.newBuilder()
                    .setDataType(DataInfo.Message.DataType.StudentType)
                    .setStudent(DataInfo.Student.newBuilder().setId(34).setName("xuesheng").build()).build());
        } else {
            channel.writeAndFlush(DataInfo.Message.newBuilder()
                    .setDataType(DataInfo.Message.DataType.WorkerType)
                    .setWorker(DataInfo.Worker.newBuilder().setAge(18).setName("workerName").build()).build());
        }

    }

    /**
     * 当通道有读取事件时 会触发。
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("server msg : " + buf.toString(CharsetUtil.UTF_8));
        System.out.println("server ip  : " + ctx.channel().remoteAddress());
    }
}
