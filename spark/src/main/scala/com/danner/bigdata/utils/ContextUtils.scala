package com.danner.bigdata.utils

import org.apache.spark.{SparkConf, SparkContext}

object ContextUtils {

    def getSparkContext(name:String)={
        // 本地测试 master 先固定
        val sparkConf = new SparkConf().setMaster("local[2]").setAppName(name)
        new SparkContext(sparkConf)
    }
}
