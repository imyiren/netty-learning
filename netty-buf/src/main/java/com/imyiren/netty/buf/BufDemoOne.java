package com.imyiren.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author feng
 */
public class BufDemoOne {
    public static void main(String[] args) {
        // 1. 创建一个对象，该对象包含一个数组， 是一个byte[10]
        // 2. 在netty中，buf 不需要像nio中的buffer进行flip读写反转，因为它底层维护了readerIndex 和 writerIndex
        ByteBuf buf = Unpooled.buffer(10);

        for (int i = 0; i < 10; i++) {
            buf.writeByte(i);
        }

        System.out.println("buf.capacity() : " + buf.capacity());
        for (int i = 0; i < buf.capacity(); i++) {
            // 这个会导致readerIndex++
            System.out.println(buf.readByte());
            // 这个不会导致readerIndex++
            // System.out.println(buf.getByte(i));
        }


    }
}
