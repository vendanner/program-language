package com.danner.bigdata.spark.core

import com.danner.bigdata.utils.ContextUtils
import com.danner.bigdata.utils.ImplicitAspect._
import org.apache.spark.rdd.RDD

object RDDApp {
    def main(args: Array[String]): Unit = {

        val sc = ContextUtils.getSparkContext(this.getClass.getSimpleName)

        val rdd = sc.parallelize(List(1,6,3,4,5,2),3)
        val otherRdd = sc.parallelize(List(6,6,8,9,7,1,2,4))

//        val array: Array[Array[Int]] = rdd.glom().collect()
//        for(arr <- array){
//            println("***********************************")
//            for( i <- arr){
//                println(i)
//            }
//        }
        val rdd1 = sc.parallelize(List(("John",18),("li",30),("John",10)))
      val rdd2: RDD[(String, Int)] = sc.parallelize(List(("John",100),("Tom",200),("Tom",100)))
//        rdd1.join(rdd2).printInfo()
//        rdd1.leftOuterJoin(rdd2).printInfo()
//        rdd1.rightOuterJoin(rdd2).printInfo()
//        rdd1.fullOuterJoin(rdd2).printInfo()
        rdd1.cogroup(rdd2).printInfo()

//       println(rdd.top(2)(Ordering[Int].reverse).toList)
//        println(rdd.takeOrdered(2)(Ordering[Int].reverse).toList)
//        rdd.map((_,1)).countByValue()
//        rdd.map((_,1)).collectAsMap()

        rdd.mapPartitionsWithIndex((index,partition)=>{
            partition.map(x => s"分区是$index,元素是$x")
        }).printInfo()

        sc.stop()
    }
}
class Person
class User extends Person
class Child extends User