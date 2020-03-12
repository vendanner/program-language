package com.danner.bigdata.hadoop.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URI;

public class HDFSApp {

    Configuration configuration;
    FileSystem fileSystem;
    @Before
    public void setUp() throws Exception {
        configuration = new Configuration();
        // ha
//        configuration.set("fs.defaultFS","hdfs:ruozeclusterg7");
//        configuration.set("dfs.nameservices","ruozeclusterg7");
//        configuration.set("dfs.ha.namenodes.ruozeclusterg7","nn1,nn2");
//        configuration.set("dfs.namenode.rpc-address.ruozeclusterg7.nn1","47.99.201.105:8020");
//        configuration.set("dfs.namenode.rpc-address.ruozeclusterg7.nn2","47.110.62.105:8020");
//        configuration.set("dfs.client.failover.proxy.provider.ruozeclusterg7",
//                "org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider");
//        configuration.set("dfs.replication","3");
//        configuration.set("dfs.support.append","true");
//        // 使用host nmae 访问，本地需配置 hostname
//        configuration.set("dfs.client.use.datanode.hostname", "true");
//        fileSystem = FileSystem.get(new URI("hdfs://ruozeclusterg7"), configuration,"hadoop");
        // 虚拟机
        configuration.set("dfs.replication","1");
        fileSystem = FileSystem.get(new URI("hdfs://192.168.22.147:9000"), configuration,"hadoop");
    }

    @After
    public void tearDown() throws Exception{
        fileSystem.close();
    }

    @Test
    public void mkdir() throws Exception {
        boolean mkdirs = fileSystem.mkdirs(new Path("/"));
        assert mkdirs;
    }

    @Test
    public void download() throws Exception{

        Path src = new Path("/jps.sh");
        Path dst = new Path("E:\\big_data\\若泽\\project\\input\\jps.sh");
        fileSystem.copyToLocalFile(src,dst);
    }

    @Test
    public void upload() throws Exception{

        Path src = new Path("input/domain_traffic.txt");
        Path dst = new Path("/win/domain_traffic.txt");
        fileSystem.copyFromLocalFile(src,dst);
    }

    @Test
    public void listStatus() throws Exception{
        // listStatus 只会列出当前目录下的所有文件夹和文件
        // listFiles 可以递归当前目录下的所有文件，但也只显示文件
        FileStatus[] fileStatuses = fileSystem.listStatus(new Path("/"));

        for(FileStatus fileStatus:fileStatuses){
            String permission  = fileStatus.getPermission().toString();
            String isDir = fileStatus.isDirectory() ? "文件夹" : "文件";
            short numReplication = fileStatus.getReplication();
            long fileSize = fileStatus.getLen();
            String fileName = fileStatus.getPath().getName();
            System.out.println(isDir+"\t"+permission+"\t" + numReplication + "\t" + fileSize + "\t" + fileName);
        }
    }

    @Test
    public void IOUitlsUpload() throws Exception{
        // 流的方式上传文件
        FileInputStream in = new FileInputStream(new File("E:\\big_data\\若泽\\project\\input\\access.log"));
        FSDataOutputStream out = fileSystem.create(new Path("/win/access.log"));
        IOUtils.copyBytes(in,out,4096,true);
    }

    @Test
    public void IOUtilsDownload() throws Exception{
        // 流的方式下载文件；此方式下载不会带 crc 文件
        FSDataInputStream in = fileSystem.open(new Path("/win/start-yarn.sh"));
        FileOutputStream out = new FileOutputStream(new File("E:\\big_data\\若泽\\project\\input\\start_yarn.sh"));
        IOUtils.copyBytes(in,out,4096,true);
    }

    @Test
    public void IOUtilsDownloadBlock01() throws Exception{
        // 第一个 block 下载

        FSDataInputStream in = fileSystem.open(new Path("/win/jdk-8u45-linux-x64.gz"));
        FileOutputStream out = new FileOutputStream(new File("E:\\big_data\\若泽\\project\\input\\jdk-8u45-linux-x64.gz.part1"));

        byte[] buf = new byte[1024];
        for(int i=0;i<1024*128;i++){
            in.read(buf);
            out.write(buf);
        }
    }

    @Test
    public void IOUtilsDownloadBlock02() throws Exception{
        // 第二种方式 block 下载

        FSDataInputStream in = fileSystem.open(new Path("/win/jdk-8u45-linux-x64.gz"));
        FileOutputStream out = new FileOutputStream(new File("E:\\big_data\\若泽\\project\\input\\jdk-8u45-linux-x64.gz.part2"));

        // 移动偏移量，按这种方式可以读取除最后一个 block 的所有 block 读取
        // 最后一个 block 是不满 128 M
        in.seek(0);         //本例只有 2 个 block，故只偏移 0
        byte[] buf = new byte[1024];
        for(int i=0;i<1024*128;i++){
            in.read(buf);
            out.write(buf);
        }
        IOUtils.closeStream(in);
        IOUtils.closeStream(out);
    }

    @Test
    public void IOUtilsDownloadBlock03() throws Exception{
        // 最后一个 block 下载

        FSDataInputStream in = fileSystem.open(new Path("/win/jdk-8u45-linux-x64.gz"));
        FileOutputStream out = new FileOutputStream(new File("E:\\big_data\\若泽\\project\\input\\jdk-8u45-linux-x64.gz.part"));

        // 移动偏移量
        in.seek(1024*1024*128);
        // 最后一个 block 直接读取
        IOUtils.copyBytes(in,out,4096,true);
    }
}
