package com.danner.java.io;

import com.danner.java.utils.Misc;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 *  BufferedReader
 *      InputStreamReader
 *          Reader
 *          InputStream
 */
public class InputStreamReaderApp {

    public static void main(String[] args) {
        read();
    }

    private static void read() {
        String inPath = "input/jps.sh";
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(inPath)));
            String content = null;

            while (null !=(content = reader.readLine())){
                System.out.println(content);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            Misc.closed(reader);
        }
    }


}
