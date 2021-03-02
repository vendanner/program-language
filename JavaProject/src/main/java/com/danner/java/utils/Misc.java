package com.danner.java.utils;

import java.io.Closeable;
import java.io.IOException;

public class Misc {

    public static void closed(Closeable closeable){
        if (null != closeable){
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
