package com.danner.bigdata.jvm.memory;

import sun.misc.Unsafe;
import java.lang.reflect.Field;

/**
 * VM Args ：-Xmx20M -XX:MaxDirectMemorySize=10M
 * 设置 本地内存 10M
 */
public class NativeMemoryOOM {

    private static final int MB = 1024*1024;

    public static void main(String[] args) throws Exception {
        Field unsafeField = Unsafe.class.getDeclaredFields()[0];
        unsafeField.setAccessible(true);
        Unsafe unsafe = (Unsafe) unsafeField.get(null);
        while (true){
            // 直接开辟本地内存
            unsafe.allocateMemory(MB);
        }
    }

}
