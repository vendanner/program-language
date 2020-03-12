package com.danner.bigdata.java.io;

import com.danner.bigdata.utils.Misc;

import java.io.*;


/**
 *  InputStream
 *      FileInputStream
 *  OutputStream
 *      FileOuputStream
 */
public class InputOutStreamApp {
    public static void main(String[] args) {
//        read();
        write();
    }

    /**
     * FileOutputStream
     */
    private static void write() {

        String outPath = "out/stream.txt";
        String content = "1231212312412412424";
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(outPath);
            byte[] buffer = content.getBytes();

            fos.write(buffer,0,buffer.length);
            fos.flush();
            System.out.println("写数据到" + outPath + "成功");
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            Misc.closed(fos);
        }

    }

    /**
     * FileInputStream 读字节到 buffer
     */
    private static void read() {
        String inPth = "input/jps.sh";
        FileInputStream fis = null;

        try {
             fis = new FileInputStream(inPth);
            byte[] buffer = new byte[1024];
            int length = 0;

            while ( (length = fis.read(buffer,0,buffer.length) )!= -1){
                System.out.println(new String(buffer,0,length));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }  finally {
            Misc.closed(fis);
        }
    }
}
