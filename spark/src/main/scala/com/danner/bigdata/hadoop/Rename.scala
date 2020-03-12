package com.danner.bigdata.hadoop

import java.net.URI
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}

/**
  * /ruozedata/20191001/a.txt
  * /ruozedata/20191001/b.txt
  * /ruozedata/20191001/c.txt
  * 使用HDFS API完成如下格式的输出
  * /ruozedata/191001/1-20191001.txt
  * /ruozedata/191001/2-20191001.txt
  * /ruozedata/191001/3-20191001.txt
  *
  */
object Rename {

    private val PATH = "hdfs://192.168.22.147:9000"

    def main(args: Array[String]): Unit = {

        val conf = new Configuration()
        val fileSystem = FileSystem.get(new URI(PATH),conf,"hadoop")

        rename(fileSystem,"/tmp/20190903")
    }

    /**
      * 1、新建目录 191001
      * 2、a.txt move 到 191001/1-20191001.txt
      * 3、删除原先的目录
      * @param fileSystem
      * @param path
      */
    def rename(fileSystem: FileSystem,path:String)={
        val oldDir = new Path(path)
        if (fileSystem.exists(oldDir)){
            val newDir = oldDir.getParent.toString + Path.SEPARATOR + oldDir.getName.substring(2)
            // 1、创建新目录
            if (fileSystem.mkdirs(new Path(newDir))){
                var index = 1
                for( fileStatus <- fileSystem.listStatus(oldDir)){
                    if(fileStatus.isFile){
                        val name = fileStatus.getPath.getName
                        val suffix = name.substring(name.lastIndexOf("."))
                        val newFilePath = newDir + Path.SEPARATOR + index + suffix
                        fileSystem.rename(fileStatus.getPath,new Path(newFilePath))
                        index += 1
                    }
                }
                fileSystem.delete(oldDir,true)
            }
        }
    }
}
