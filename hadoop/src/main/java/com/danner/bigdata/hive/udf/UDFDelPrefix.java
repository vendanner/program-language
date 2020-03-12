package com.danner.bigdata.hive.udf;

import org.apache.hadoop.hive.ql.exec.UDF;

public class UDFDelPrefix extends UDF {
    public String evaluate(String content){
       if(content.contains("_") && (content.split("_").length == 2)){
           return content.split("_")[1];
       }
       return content;
    }

    public static void main(String[] args) {
        System.out.println(new UDFDelPrefix().evaluate("1_aaa"));
    }
}
