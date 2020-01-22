package com.imyirem.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.FileChannel;

/**
 * @author feng
 */
public class FileChannelCopyDemo {
    public static void main(String[] args) throws IOException {
        URL resource = FileChannelReadWriteDemo.class.getClassLoader().getResource("img-in.jpg");
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

        String outPath = inPath.replace("img-in", "img-out");
        File outFile = new File(outPath);
        if (!outFile.exists()) {
            boolean newFile = outFile.createNewFile();
            System.out.println(newFile ? "jpg-out file 创建成功~" : "jpg-out file 创建失败~");
        }

        try (
                FileInputStream fileInputStream = new FileInputStream(inFile);
                FileChannel inFileChannel = fileInputStream.getChannel();
                FileOutputStream fileOutputStream = new FileOutputStream(outFile);
                FileChannel outfileChannel = fileOutputStream.getChannel()
        ) {
            // 使用transferFrom
            // outfileChannel.transferFrom(inFileChannel, 0, inFileChannel.size());
            // 或者 使用transferTo
            inFileChannel.transferTo(0, inFileChannel.size(), outfileChannel);
        }
    }
}
