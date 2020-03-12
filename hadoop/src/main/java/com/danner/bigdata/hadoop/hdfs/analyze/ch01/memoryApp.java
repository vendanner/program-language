package com.danner.bigdata.hadoop.hdfs.analyze.ch01;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.CreateFlag;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsPermission;

import java.net.URI;
import java.util.EnumSet;

/**
 * hdfs 内存储存伪代码
 */
public class memoryApp {
    public static void main(String[] args) throws Exception {
        Configuration configuration = new Configuration();
        configuration.set("dfs.replication","1");
        FileSystem fileSystem = FileSystem.get(new URI("hdfs://192.168.22.147:9000"),
                configuration, "hadoop");
        Path path = new Path("");
        int bufferLength = 0;
        short replicationFactor = 0;
        long blockSize = 0;
        FSDataOutputStream fos = fileSystem.create(path,FsPermission.getFileDefault(),
                EnumSet.of(CreateFlag.CREATE , CreateFlag.LAZY_PERSIST),
                bufferLength,replicationFactor,blockSize,null);
    }
}
