package com.danner.bigdata.spark.core

import com.danner.bigdata.utils.{ContextUtils, HDFSUtil}
import org.apache.hadoop.io.NullWritable
import org.apache.hadoop.mapred.lib.MultipleTextOutputFormat
import org.apache.spark.HashPartitioner

object MultiOutputApp {
    def main(args: Array[String]): Unit = {

        val TOPN = 2
        val sc = ContextUtils.getSparkContext(this.getClass.getSimpleName)

        val output = "output/mulit"
        HDFSUtil.clearFile(output,sc.hadoopConfiguration)
        val input = sc.textFile("input/site.log")

        val rdd = input.map(x => {
            val words = x.split(",")
            (words(0), x)
        })
        val count = rdd.map(_._1).distinct().count()

        /**
          * key 为目录
          */
        rdd.partitionBy(new HashPartitioner(count.toInt)).saveAsHadoopFile(output,classOf[String],
            classOf[String],classOf[MyMultipleTextOutputFormat])



        sc.stop()
    }

    class MyMultipleTextOutputFormat extends MultipleTextOutputFormat[Any,Any]{
        override def generateFileNameForKeyValue(key: Any, value: Any, name: String): String = {
            s"$key/$name"
        }

        override def generateActualKey(key: Any, value: Any): AnyRef = {
            NullWritable.get()
        }
    }
}
