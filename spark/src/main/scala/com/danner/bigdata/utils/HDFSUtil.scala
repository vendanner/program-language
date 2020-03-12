package com.danner.bigdata.utils

import java.io.IOException

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}


object HDFSUtil {

    def clearFile(fileName:String,configuration: Configuration): Boolean={
        var flag = true
        try{
            val fileSystem = FileSystem.get(configuration)
            val path = new Path(fileName)
            if(fileSystem.exists(path)){
                fileSystem.delete(path)
            }
        }catch {
            case e:IOException => e.printStackTrace()
            case e:Exception => e.printStackTrace()
        }
        flag
    }
}
