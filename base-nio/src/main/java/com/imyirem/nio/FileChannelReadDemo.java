package com.imyirem.nio;

import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author yiren
 */
public class FileChannelReadDemo {
    public static void main(String[] args) throws IOException {
        URL resource = FileChannelReadWriteDemo.class.getClassLoader().getResource("test-in.txt");
        if (null == resource) {
            System.out.println("没有找到文件！");
            return;
        }
        String inPath = resource.getPath();
        File file = new File(inPath);
        // 创建一个输出流-> channel
        try (FileInputStream fileInputStream = new FileInputStream(file);
             // 通过 fileInputStream 获取对应的文件channel (实际类型是FileChannelImpl)
             FileChannel fileChannel = fileInputStream.getChannel()) {

            // 创建一个缓冲区
            ByteBuffer byteBuffer = ByteBuffer.allocate(Long.valueOf(file.length()).intValue());
            // buffer从channel中读取数据
            fileChannel.read(byteBuffer);

            String result = new String(byteBuffer.array());
            System.out.println(result);
        }

    }
}
