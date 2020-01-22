package com.imyirem.nio;

import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author yiren
 */
public class FileChannelReadWriteDemo {
    public static void main(String[] args) throws IOException {
        URL resource = FileChannelReadWriteDemo.class.getClassLoader().getResource("test-in.txt");
        if (null == resource) {
            System.out.println("没有找到文件！");
            return;
        }
        String inPath = resource.getPath();
        File inFile = new File(inPath);
        if (!inFile.exists()) {
            System.out.println("读入文件不存在！");
            return;
        }

        String outPath = inPath.replace("test-in", "test-out");
        File outFile = new File(outPath);
        if (!outFile.exists()) {
            boolean newFile = outFile.createNewFile();
            System.out.println(newFile ? "out file 创建成功~" : "out file 创建失败~");
        }

        try (
                FileInputStream fileInputStream = new FileInputStream(inFile);
                FileChannel inFileChannel = fileInputStream.getChannel();
                FileOutputStream fileOutputStream = new FileOutputStream(outFile);
                FileChannel outfileChannel = fileOutputStream.getChannel()
        ) {

            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

            StringBuilder contentBuilder = new StringBuilder();
            while (true) {
                // 重置标志位
                byteBuffer.clear();
                // 从in channel 中读取数据
                int read = inFileChannel.read(byteBuffer);
                if (-1 == read) {
                    break;
                }

                contentBuilder.append(new String(byteBuffer.array(), 0, byteBuffer.position()));

                byteBuffer.flip();
                outfileChannel.write(byteBuffer);
            }

            System.out.println(contentBuilder.toString());
            System.out.println("--- end ---");

        }

    }
}
