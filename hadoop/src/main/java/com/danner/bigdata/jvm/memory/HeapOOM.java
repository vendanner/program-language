package com.danner.bigdata.jvm.memory;

import java.util.ArrayList;
import java.util.List;

/**
 * VM Args: -Xms20m -Xmx20m -XX:+HeapDumpOnOutOfMemoryError
 * 设置JVM 的 Java 堆大小 20M 且不可扩展
 */
public class HeapOOM {

    public static class OOMObject{

    }
    public static void main(String[] args) {

        List<OOMObject> list = new ArrayList<>();

        while (true){
            // 一直创建对象导致 OOM
            list.add(new OOMObject());
        }
    }

}
