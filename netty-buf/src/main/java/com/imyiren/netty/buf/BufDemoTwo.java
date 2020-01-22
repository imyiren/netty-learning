package com.imyiren.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author feng
 */
public class BufDemoTwo {
    public static void main(String[] args) {
        // 创建ByteBuf
        ByteBuf buf = Unpooled.copiedBuffer("hello world!", CharsetUtil.UTF_8);
        // 使用相关的方法

        System.out.println("有没有分配数组");
        if (buf.hasArray()) {
            byte[] content = buf.array();
            // 将 content 转成字符串
            System.out.println(new String(content, CharsetUtil.UTF_8));
        }

        System.out.println("byteBuf: " + buf);

        System.out.println("buf.getByte(0) : " + buf.getByte(0));

        System.out.println("buf.arrayOffset() : " + buf.arrayOffset());

        System.out.println("buf.readerIndex() : " + buf.readerIndex());

        System.out.println("buf.writerIndex() : " + buf.writerIndex());

        System.out.println("buf.capacity() : " + buf.capacity());

        System.out.println("buf.readableBytes() : " + buf.readableBytes());

        System.out.println("buf.readByte() : " + buf.readByte());

        // 使用for循环取出各个字节
        for (int i = 0; i < buf.readableBytes(); i++) {
            System.out.println((char) buf.getByte(i));
        }

        // 从索引为2开始读 读四个
        System.out.println("buf.getCharSequence(1, 3, CharsetUtil.UTF_8) : " + buf.getCharSequence(3, 4, CharsetUtil.UTF_8));

    }
}
