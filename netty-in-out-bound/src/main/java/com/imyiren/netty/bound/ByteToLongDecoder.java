package com.imyiren.netty.bound;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author yiren
 */
public class ByteToLongDecoder extends ByteToMessageDecoder {

    private static final int LONG_BYTE_SIZE = 8;


    /**
     * decode 会根据接收到的数据，被调用多次，直到确定没有新的元素被添加到list
     * 或者是ByteBuf 没有更多的可读字节为止
     * 如果List out 不为空 就会将List的内容传递给下一个ChannelInboundHandler处理，该处理器的方法也会被调用多次
     *
     * @param ctx 上下文
     * @param in  入栈的 ByteBuf
     * @param out list集合，解码后传输给下一个handler
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("ByteToLongDecoder decode");
        // 判断只有8个字节，才能读取一个Long
        if (in.readableBytes() >= LONG_BYTE_SIZE) {
            out.add(in.readLong());
        }
    }
}
