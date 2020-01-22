package com.imyirem.nio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author feng
 */
public class MappedByteBufferDemo {
    public static void main(String[] args) throws IOException {
        // MappedByteBuffer 可以让文件直接在内存（堆外内存）中修改 即操作系统不需要copy一次

        URL resource = FileChannelReadWriteDemo.class.getClassLoader().getResource("test-in.txt");
        if (null == resource) {
            System.out.println("没有找到文件！");
            return;
        }
        String inPath = resource.getPath();
        RandomAccessFile randomAccessFile = new RandomAccessFile(inPath, "rw");

        // 获取对应的文件通道
        try (FileChannel channel = randomAccessFile.getChannel();){
            /**
             * 参数1： 是读写模式，
             * 参数2：0代表起始位置，
             * 参数3：映射到内存的大小 最多5个字节
             * 可以直接修改的就是0- 5 （是大小  不是索引位置 比如：3-3 是指 3，4，5 的索引位置 ）
             * 这个 MappedByteBuffer 是堆外内存，是操作系统级别的修改，不需要copy到JVM内部的内存来来修改。
             */
            MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
            mappedByteBuffer.put(0, (byte) 'H');
            mappedByteBuffer.put(3, (byte) 'L');
            // 会报错 IndexOutOfBoundsException
            // mappedByteBuffer.put(5, (byte) 'A');
            System.out.println("--- end ---");
        }


    }
}
