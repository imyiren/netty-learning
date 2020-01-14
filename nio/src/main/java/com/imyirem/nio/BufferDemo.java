package com.imyirem.nio;

import java.nio.IntBuffer;

/**
 * @author feng
 */
public class BufferDemo {

    public static void main(String[] args) {
        // buffer除了Boolean没有 其他基本数据类型都有

        // 创建一个Buffer 大小为5 可以存放5个int
        IntBuffer intBuffer = IntBuffer.allocate(5);

        // 向buffer中存放数据
        for (int i = 0; i < intBuffer.capacity(); i++) {
            intBuffer.put(i * 2);
        }

        // 从buffer读取数据
        // 将buffer转换，读写切换
        intBuffer.flip();
        // 从第二位开始读 （从0开始数）
        intBuffer.position(1);
        // 读到第三位
        intBuffer.limit(3);
        while (intBuffer.hasRemaining()) {
            int value = intBuffer.get();
            System.out.println(value);
        }
        //
        System.out.println("======>");
        // 清除缓冲区，没有修改内部存储的元素，数据并没有清除，只是后面添加数据的时候会覆盖
        intBuffer.clear();
        // hasRemaining 是 获取当前位置和限制之间是否存在元素
        while (intBuffer.hasRemaining()) {
            // get就是获取当前位置元素，并且把指针向后移。
            int value = intBuffer.get();
            System.out.println(value);
        }
    }
}
