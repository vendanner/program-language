package com.danner.bigdata.hadoop

import java.io.{BufferedReader, File, FileInputStream, FileOutputStream}

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FSDataOutputStream, FileSystem, Path}
import java.net.URI

import org.apache.hadoop.io.IOUtils

object HdfsApp  {

//    private val PATH = "hdfs://192.168.22.147:9000"
private val PATH = "hdfs://hadoop001:8020"

    def main(args: Array[String]): Unit = {
        val conf = new Configuration()
        conf.set("dfs.client.use.datanode.hostname", "true");
        val fileSystem = FileSystem.get(new URI(PATH),conf,"hadoop")

//        if (mkdir(fileSystem,"/win")){
//            println(" create dir succ")
//        }else{
//            println("create dir error")
//        }

        download(fileSystem,"/tmp/nohup.out","input/hdfs/a")
//        uploadForStream(fileSystem,"input/com.apk","/tmp/com.apk")
//        list(fileSystem,"/")

//        if( delete(fileSystem,"/a.txt")){
//            println("delete file succ")
//        }else{
//            println("delete file error")
//        }

//        downloadBlock(fileSystem,"/tmp/hadoop-2.6.0-cdh5.15.1.tar.gz","input/a.part2")
    }

    def mkdir(fileSystem: FileSystem,path:String):Boolean = fileSystem.mkdirs(new Path(path))

    def download(fileSystem: FileSystem,src:String,dst:String):Unit =
        fileSystem.copyToLocalFile(new Path(src),new Path(dst))

    def uploadForStream(fileSystem: FileSystem,src:String,dst:String):Unit = {
        val in = new FileInputStream(src)
        // IOUtils 操作时，filesystem 创建流
        val out = fileSystem.create(new Path(dst))
        IOUtils.copyBytes(in,out,4096)

    }

    def list(fileSystem: FileSystem,path:String):Unit = {
        val fileStatuses = fileSystem.listStatus(new Path(path))
        for(fileStatus <- fileStatuses){
            val dir = if (fileStatus.isDirectory) "文件夹" else "文件"
            val name = fileStatus.getPath.getName
            val size = fileStatus.getLen
            val replication = fileStatus.getReplication
            val permission = fileStatus.getPermission.toString
            println(dir + "\t" + permission + "\t" + replication + "\t" + size + "\t" + name)
        }
    }

    def delete(fileSystem: FileSystem,path:String): Boolean = fileSystem.delete(new Path(path),true)

    def downloadBlock(fileSystem: FileSystem,src:String,dst:String): Unit ={
        val out = new FileOutputStream(new File(dst))
        val in = fileSystem.open(new Path(src))

        // 下载第二个 block
        in.seek(1024*1024*128)
        var bytes = new Array[Byte](1024)
        for (i <- 0.until(1024*128)){
            in.read(bytes)
            out.write(bytes)
        }
    }
}
