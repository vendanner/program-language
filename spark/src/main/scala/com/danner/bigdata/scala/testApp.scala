package com.danner.bigdata.scala

import java.io._

import scala.util.Random

object testApp {
    def main(args: Array[String]): Unit = {

        var out:BufferedWriter = null


        def func(v1:Vector[String],v2:Vector[String]): Boolean ={
            var res = true
            for(i <- v1.indices) {
                if(v1(i) != v2(i)){
                    res = false
                }
            }
            res
        }

        val v1 = Vector("abc"," dev"," aaa")
        val v2 = Vector("abc"," dev"," aaa")
        println(func(v1,v2))
        for (i <- 1 to 3; j <-1 to 3 if i > j) {
            println(s"i=$i, j=$j, i+j=${i + j}")
        }

//        try{
//            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("input/access.txt",true)))
//            val random = new Random()
//            for (i <- 1.to(30000000)){
//               val sb = new StringBuilder
//                for (j <- 1.to(random.nextInt(5) + 5)){
//                    sb.append((random.nextInt(20) + 97).toChar)
//                }
//                out.write(sb.toString() + "\r\n")
//            }
//            out.flush()
//        }catch {
//            case ex:Exception => ex.printStackTrace()
//        }finally {
//            try{
//                if (out != null){
//                    out.close()
//                }
//            }catch {
//                case ex:Exception => ex.printStackTrace()
//
//            }
//        }
    }
}
