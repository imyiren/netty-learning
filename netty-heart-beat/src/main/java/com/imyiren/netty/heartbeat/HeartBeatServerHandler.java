package com.imyiren.netty.heartbeat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @author feng
 */
public class HeartBeatServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;

            /**
             *  IdleState event.state()
             * READER_IDLE,
             * No data was sent for a while.
             * WRITER_IDLE,
             * No data was either received or sent for a while.
             * ALL_IDLE
             */
            String eventType = null;
            switch (event.state()) {
                case READER_IDLE:
                    eventType = "READER_IDLE";
                    break;
                case WRITER_IDLE:
                    eventType = "WRITER_IDLE";
                    break;
                case ALL_IDLE:
                    eventType = "ALL_IDLE";
                    break;
                default:
            }

            System.out.println(ctx.channel() + ", EventType: " + eventType);

            // 如果关闭 就只会发生一次空闲状态 是按照三个空闲状态的时间顺序的第一个
            // ctx.channel().close();
        }

    }
}
