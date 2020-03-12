package com.danner.bigdata.spark.core

import com.danner.bigdata.utils.{ContextUtils, HDFSUtil}
import com.danner.bigdata.utils.ImplicitAspect._

object DomainCountApp {
    def main(args: Array[String]): Unit = {

        val TOPN = 2
        val sc = ContextUtils.getSparkContext(this.getClass.getSimpleName)

        val input = "input/site.log"
        val output = "output/sites"
        HDFSUtil.clearFile(output,sc.hadoopConfiguration)
        val rdd = sc.textFile(input).map(x => {
            val words = x.split(",")
            val domain = words(0)
            val url = words(1)
            (domain, url)
        })

//        rdd.map((_,1)).reduceByKey(_+_).sortBy(-_._2).printInfo()

//        domains.map(domain =>{
//            rdd.filter(_._1 == domain).map((_,1)).reduceByKey(_+_).sortBy(-_._2).take(TOPN).foreach(println)
//        })




        Thread.sleep(1000000)
        sc.stop()
    }
}
