package com.danner.bigdata.hadoop.mapreduce.utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class FileUtils {

    /**
     * 递归删除
     * @param name
     * @param configuration
     * @throws Exception
     */
    public static void clearFile(String name, Configuration configuration) throws Exception {

        FileSystem fileSystem = FileSystem.get(configuration);

        Path path = new Path(name);
        if (fileSystem.exists(path)){
            fileSystem.delete(path,true);
        }
    }
}
