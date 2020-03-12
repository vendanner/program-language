package com.danner.bigdata.hive.udf;

import org.apache.hadoop.hive.ql.exec.UDF;

import java.util.Random;

public class UDFAddPrefix extends UDF {
    public String evaluate(String content){
        Random random = new Random();
        int index = random.nextInt(10);
        return index + "_" + content;
    }
    public String evaluate(String content,int size){
        Random random = new Random();
        int index = random.nextInt(size);
        return index + "_" + content;
    }

    public static void main(String[] args) {
        System.out.println(new UDFAddPrefix().evaluate("aaa"));
    }
}
