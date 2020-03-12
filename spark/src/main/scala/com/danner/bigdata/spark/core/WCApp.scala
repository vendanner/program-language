package com.danner.bigdata.spark.core

import com.danner.bigdata.utils.ContextUtils
import org.apache.spark.rdd.RDD
import com.danner.bigdata.utils.ImplicitAspect._
import org.apache.spark.storage.StorageLevel
import org.apache.spark.{SparkConf, SparkContext}

object WCApp {
    def main(args: Array[String]): Unit = {
//        val sc = ContextUtils.getSparkContext(WCApp.getClass.getSimpleName)
        val conf = new SparkConf().setAppName("wcApp").setMaster("local[2]")
//        conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
//        conf.registerKryoClasses(Array(classOf[TestSerialization]))
        val sc = new SparkContext(conf)

//        val inputFile = "input/2019.log"
//        val rdd = sc.textFile(inputFile)
//        val seriRdd = rdd.flatMap(_.split(" ")).map(x => {
//            (TestSerialization(x), 1)
//        })
////        seriRdd.persist()
//        seriRdd.cache()
////        seriRdd.persist(StorageLevel.MEMORY_ONLY_SER)
////        println(seriRdd.distinct().count())
//        seriRdd.reduceByKey(_+_).printInfo()
//        println("____________________________________________")
//        seriRdd.filter(_._1.equals(TestSerialization("1000001"))).printInfo()
//
//        Thread.sleep(1000000)

        val rdd = sc.textFile("input/traffics.txt").map(x => {
            try {
                val splits = x.split(" ")
                (splits(0), (splits(1), splits(2), splits(3).toInt))
            } catch {
                case e: Exception => null
            }
        }).filter(_ != null)
        // groupBy value = 原来本身
        val value: RDD[(String, Iterable[(String, (String, String, Int))])] = rdd.groupBy(_._1)
        // groupByKey value 不带 key
        val value2: RDD[(String, Iterable[(String, String, Int)])] = rdd.groupByKey()

        value.mapValues(x => {
            var id = ""
            var price = 0

            x.foreach(item => {

                if(item._2._3 > price){
                    price = item._2._3
                    id = item._1
                }
            })
            price
        }).foreach(println)

        value2.mapValues(x => {
            var id = ""
            var price = 0

            x.foreach(item => {
                if(item._3 > price){
                    price = item._3
                    id = item._1
                }
            })
            price
        }).foreach(println)


        Thread.sleep(100000000)
        sc.stop()
    }

    case class TestSerialization(name:String)
}
