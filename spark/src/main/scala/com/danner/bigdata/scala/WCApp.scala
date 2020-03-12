package com.danner.bigdata.scala

/**
  * scala wc demo
  */
object WCApp {
    def main(args: Array[String]): Unit = {

//        val array = Array(Array(1,2),Array(3,4),Array(5,6))
//        // flatMap = map + flat
//        println(array.flatMap(x =>{
//            println("----------------------")
//            x.foreach(println)
//            println("---------以上是 x 值-----------")
//            x.map(i =>{
//                println(i)
//                i*2
//            })
//        }))

        // 累加每个字符数量，并按数量倒序排列
//        val array = Array(("a",20),("b",10),("a",200),("c",1))
//        array.groupBy(_._1).map(x =>{
//            val key = x._1
//            val sum = x._2.map(_._2).sum
//            (key,sum)
//        }).toList.sortBy(-_._2).foreach(println)

        // word count
        val words = Array("hello hello world","world hello scala")
//        words.flatMap(_.split(" ")).map((_,1)).groupBy(_._1).map(x =>{
//            val key = x._1
//            val sum = x._2.map(_._2).sum
//            (key,sum)
//        }).toList.sortBy(-_._2).foreach(println)
        words.flatMap(_.split(" ")).map((_,1)).groupBy(_._1).
                mapValues(_.map(_._2).sum).toList.sortBy(-_._2).foreach(println)



    }
}
