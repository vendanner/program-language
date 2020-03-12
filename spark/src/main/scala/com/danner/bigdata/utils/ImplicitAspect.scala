package com.danner.bigdata.utils

import org.apache.spark.rdd.RDD

object ImplicitAspect {

    private val PRINT_FLAG = 0

    // 给 RDD 添加 printInfo 函数
    implicit class MyRdd[T](rdd:RDD[T]){
        def printInfo(flag:Int = PRINT_FLAG)={
            if(flag == PRINT_FLAG){
//                rdd.foreach(println)
                rdd.collect().foreach(println)
//                val ts = rdd.collect().foreach(println)
//                for(t <- ts){
//                    println(t)
//                }
            }
        }
    }
}


