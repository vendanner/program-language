package com.danner.bigdata.hadoop.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionCodecFactory;
import org.apache.hadoop.io.compress.CompressionInputStream;
import org.apache.hadoop.io.compress.CompressionOutputStream;
import org.apache.hadoop.util.ReflectionUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * 压缩文件
 */
public class CompressApp {
    public static void main(String[] args) throws Exception {
//        compressFile("input/access.log","org.apache.hadoop.io.compress.BZip2Codec");
        deCompressFile("input/access_test.log.bz2");
    }

    /**
     * 压缩
     * @param path 原文件
     * @param codecName 支持的压缩的类名
     */
    public static void compressFile(String path,String codecName) throws Exception {
        FileInputStream fis = new FileInputStream(path);
        Class<?> aClass = Class.forName(codecName);
        CompressionCodec codec= (CompressionCodec)ReflectionUtils.newInstance(aClass, new Configuration());
        FileOutputStream fos = new FileOutputStream(path + codec.getDefaultExtension());
        CompressionOutputStream cos = codec.createOutputStream(fos);

        IOUtils.copyBytes(fis,cos,1024*4);

        IOUtils.closeStream(fis);
        IOUtils.closeStream(cos);
        IOUtils.closeStream(fos);

    }

    /**
     * 解压
     * @param path
     * @throws Exception
     */
    public static void deCompressFile(String path) throws Exception {

        CompressionCodec codec = new CompressionCodecFactory(new Configuration()).getCodec(new Path(path));
        CompressionInputStream cis = codec.createInputStream(new FileInputStream(path));

        FileOutputStream fos = new FileOutputStream(path.substring(0, path.lastIndexOf(".")));

        IOUtils.copyBytes(cis,fos,1024*4);

        IOUtils.closeStream(cis);
        IOUtils.closeStream(fos);
    }
}
