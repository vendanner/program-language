package com.danner.bigdata.spark.core.homework

import com.danner.bigdata.utils.{ContextUtils, HDFSUtil}
import com.danner.bigdata.utils.ImplicitAspect._

object PartitonerApp {
    def main(args: Array[String]): Unit = {

        val TOPN = 5
        val sc = ContextUtils.getSparkContext(this.getClass.getSimpleName)

        sc.textFile("input/access.log").map(x => {
            val splits = x.split("\t")
            (splits(1), AccessInfo(splits(0), splits(1), splits(2).toFloat, splits(3), splits(4).toInt,
                splits(5), splits(6).toInt, splits(7), splits(8), splits(9)))
        }).sortBy(-_._2.traffic).take(TOPN).foreach(println)

        sc.stop()
    }
}
