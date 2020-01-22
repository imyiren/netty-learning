package com.imyirem.nio;

import java.nio.ByteBuffer;

/**
 * @author yiren
 */
public class BufferPutGetByTypeDemo {
    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(64);

        byteBuffer.putChar('A');
        byteBuffer.putLong(123L);
        byteBuffer.putInt(123);

        byteBuffer.flip();

        // 获取的时候需要按照上面的顺序 否则就会报BufferUnderFlowException
        System.out.println(byteBuffer.getChar());
        System.out.println(byteBuffer.getLong());
        System.out.println(byteBuffer.getInt());

        ByteBuffer readOnlyBuffer = byteBuffer.asReadOnlyBuffer();

        // put 会报错 ReadOnlyBufferException
        // readOnlyBuffer.putInt(12);

        readOnlyBuffer.flip();

        System.out.println(readOnlyBuffer.getChar());
        System.out.println(readOnlyBuffer.getLong());
        System.out.println(readOnlyBuffer.getInt());

    }
}
