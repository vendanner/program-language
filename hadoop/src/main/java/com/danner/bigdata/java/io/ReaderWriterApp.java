package com.danner.bigdata.java.io;

import com.danner.bigdata.utils.Misc;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


/**
 * Reader
 *      FileReader
 * Writer
 *      FileWriter
 */
public class ReaderWriterApp {
    public static void main(String[] args) {
//        read();
        write();
    }

    private static void write() {
        String content = "java";
        String outPath = "out/java.txt";
        FileWriter writer = null;

        try {
            writer = new FileWriter(outPath);

            writer.write(content,0,content.length());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            Misc.closed(writer);
        }
    }

    /**
     *
     */
    private static void read() {
        String inPth = "input/jps.sh";
        FileReader reader = null;

        try {
            reader = new FileReader(inPth);
            char[] buffer = new char[1024];
            int length = 0;

            while ((length = reader.read(buffer,0,buffer.length)) != -1){
                System.out.println(new String(buffer,0,length));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            Misc.closed(reader);
        }
    }
}
