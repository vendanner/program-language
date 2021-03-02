package com.danner.java.jvm.memory;

import java.util.ArrayList;
import java.util.List;

/**
 * Java8 VM Args:-Xms10m -Xmx10m -XX:-UseGCOverheadLimit
 * JDK 8 运行时常量池在 heap 中，设置对大小10M
 */
public class RuntimeConstantPoolOOM {
    public static void main(String[] args) {
        List<String> list = new ArrayList<String>();
        long i = 0L;
        while(true) {
            list.add(String.valueOf(i++).intern());
        }
    }
}

