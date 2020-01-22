package com.imyiren.netty.log4j;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * @author yiren
 */
public class ByteToLongReplayingDecoder extends ReplayingDecoder<Void> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("ByteToLongReplayingDecoder decode");
        // ReplayingDecoder 不需要判断数据是否足够读取，内不会进行判断。
        out.add(in.readLong());
    }

}
