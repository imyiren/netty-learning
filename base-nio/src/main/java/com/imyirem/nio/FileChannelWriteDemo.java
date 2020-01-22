package com.imyirem.nio;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author yiren
 */
public class FileChannelWriteDemo {
    public static void main(String[] args) throws IOException {
        // 创建一个输出流-> channel
        try (FileOutputStream fileOutputStream = new FileOutputStream("test.txt");
             // 通过 fileOutputStream 获取对应的文件channel (实际类型是FileChannelImpl)
             FileChannel fileChannel = fileOutputStream.getChannel()) {

            String string = "hello world";


            // 创建一个缓冲区
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

            // 将String放入ByteBuffer中去
            byteBuffer.put(string.getBytes());
            // 反转 开始读
            byteBuffer.flip();

            // buffer写入channel
            fileChannel.write(byteBuffer);
        }

    }
}
