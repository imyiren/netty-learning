package com.imyiren.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;


/**
 * 自定义一个handler， 需要继承netty规定好的 某个 HandlerAdapter
 * 这时我们自定义的Handler，才能称为handler。 ChannelInboundHandlerAdapter:
 *
 * @author yiren
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 这里可以读取客户端发送的数据
     *
     * @param ctx 上下文对象：含有管道pipeline 、 通道channel 、 地址等
     * @param msg 客户端发送的数据
     * @throws Exception
     */
    // @Override
    // public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    //     System.out.println("current thread name: " + Thread.currentThread().getName());
    //     ChannelPipeline pipeline = ctx.pipeline();
    //     Channel channel = ctx.channel();
    //     System.out.println("ctx.channel: " + channel + ", ctx.pipeline: " + pipeline);
    //     System.out.println("server ctx : " + ctx);
    //     // 将msg 转成一个byteBuf 这个是netty提供的 （不是NIO的ByteBuffer）
    //     ByteBuf buf = (ByteBuf) msg;
    //     System.out.println("client msg : " + buf.toString(CharsetUtil.UTF_8));
    //     System.out.println("client ip  : " + ctx.channel().remoteAddress());
    // }
    // @Override
    // public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    //     // 模拟耗时操作 这样会发生阻塞
    //     // Thread.sleep(10 * 1000);
    //     // ctx.writeAndFlush(Unpooled.copiedBuffer("hello, Client - long time ", CharsetUtil.UTF_8));
    //
    //     // 第一个方案： 用户自定义普通任务来完成 该任务是提交到taskQueue中
    //     // 立即执行 10s完成
    //     ctx.channel().eventLoop().execute(()->{
    //         try {
    //             Thread.sleep(10 * 1000);
    //         } catch (InterruptedException e) {
    //             System.out.println("error occur !");
    //             e.printStackTrace();
    //         }
    //         ctx.writeAndFlush(Unpooled.copiedBuffer("hello, Client - long time 1", CharsetUtil.UTF_8));
    //     });
    //     // 延迟10s执行，也就是说执行是在10s后 然后执行20s 也就是30s后才会有数据
    //     ctx.channel().eventLoop().execute(()->{
    //         try {
    //             Thread.sleep(20 * 1000);
    //         } catch (InterruptedException e) {
    //             System.out.println("error occur !");
    //             e.printStackTrace();
    //         }
    //         ctx.writeAndFlush(Unpooled.copiedBuffer("hello, Client - long time 2", CharsetUtil.UTF_8));
    //     });
    //     System.out.println("go on...");
    //
    // }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 第一个方案： 用户自定义定时任务来完成 该任务是提交到scheduleTaskQueue中
        // 5s后执行
        ctx.channel().eventLoop().schedule(() -> {
            try {
                Thread.sleep(5 * 1000);
            } catch (InterruptedException e) {
                System.out.println("error occur !");
                e.printStackTrace();
            }
            ctx.writeAndFlush(Unpooled.copiedBuffer("hello, Client - long time 1", CharsetUtil.UTF_8));
        }, 5, TimeUnit.SECONDS);

        System.out.println("go on...");

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
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
