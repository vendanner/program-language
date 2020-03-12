package com.danner.bigdata.hadoop.hdfs.homework;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.log4j.Logger;

import java.io.File;
import java.net.URI;

/**
 * /ruozedata/20191001/a.txt
 * /ruozedata/20191001/b.txt
 * /ruozedata/20191001/c.txt
 *
 *  使用 HDFS API 完成如下格式的输出
 * /ruozedata/191001/1-20191001.txt
 * /ruozedata/191001/2-20191001.txt
 * /ruozedata/191001/3-20191001.txt
 */

public class RenameApp {
    public static void main(String[] args) throws Exception {

        Configuration configuration = new Configuration();
        configuration.set("dfs.replication","1");
        FileSystem fileSystem = FileSystem.get(new URI("hdfs://192.168.22.147:9000"), configuration, "hadoop");
        rename(fileSystem ,"/win/danner/20190826/");

        fileSystem.close();
    }
    public static void rename(FileSystem fileSystem ,String name) throws Exception {

        String date = new Path(name).getName();
        // 修改当前目录下文件名称
        int index = 1;
        FileStatus[] fileStatuses = fileSystem.listStatus(new Path(name));
        for(FileStatus fileStatus : fileStatuses){
            // hdfs 中文件名已排序
            String fileName = fileStatus.getPath().getName();
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            String newFileName = index + "-" + date + "." + suffix;
            String newPathName = fileStatus.getPath().toString().replace(fileName, newFileName);
            fileSystem.rename(fileStatus.getPath(),new Path(newPathName));
            index ++;
        }
        // 修改当前目录名称
        fileSystem.rename(new Path(name),new Path(new Path(name).getParent().toString()+
                File.separator + date.substring(2)));
    }
}
