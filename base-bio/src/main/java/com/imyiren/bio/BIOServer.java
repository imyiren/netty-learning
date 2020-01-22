package com.imyiren.bio;


import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author feng
 */
public class BIOServer {
    public static void main(String[] args) throws IOException {
        // 创建一个线程池
        ExecutorService executorService = Executors.newCachedThreadPool();
        // 创建serverSocket
        ServerSocket serverSocket = new ServerSocket(6666);

        // 监听客户端连接
        while (true) {
            final Socket accept = serverSocket.accept();
            System.out.println("连接到客户端！");
            executorService.execute(() -> {
                try {
                    String hostAddress = accept.getInetAddress().getHostAddress();
                    String port = String.valueOf(accept.getPort());

                    InputStream inputStream = accept.getInputStream();
                    byte[] bytes = new byte[1024];

                    StringBuilder builder = new StringBuilder();
                    int len = 0;
                    while (-1 != (len = inputStream.read(bytes))) {
                        builder.append(new String(bytes, 0, len));
                    }
                    System.out.println("Thread: " + Thread.currentThread().getName() + " " + Thread.currentThread().getId() + ", From " + hostAddress + ":" + port + ", Accept: " + builder.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        accept.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
