package org.danner.bigdata.flink.sink

import org.apache.flink.streaming.api.functions.co.CoFlatMapFunction
import org.apache.flink.util.Collector

/**
  * CoFlatMapFunction (输入1，输入2, 输出)
  *  由于flatmap 1 和 flatmap2 是乱序执行，所以第一次执行flatmap1 时，flatmap2 可能未执行导致map 为空
  *  故把 map 设置为类的静态变量，在第一次执行时延迟
  */
class MyCoFlatMapFunction extends CoFlatMapFunction[String,Map[String,String],String]{
    override def flatMap1(value: String, out: Collector[String]): Unit = {
        if(MyCoFlatMapFunction.map.isEmpty){
            try {
                // 第一次延迟
                Thread.sleep(5000)
            }catch {
                case e:Exception => e.printStackTrace()
            }
        }
        out.collect(value + " ==> " + MyCoFlatMapFunction.map.getOrElse(value,-99))
    }

    override def flatMap2(value: Map[String, String], out: Collector[String]): Unit = {
        if(MyCoFlatMapFunction.map.isEmpty){
            MyCoFlatMapFunction.map = value
            println("--------- map init ------------")
        }
    }
}

object MyCoFlatMapFunction  {
    @volatile var map = Map[String,String]()

    val singleObj = new MyCoFlatMapFunction

    def apply()={
        singleObj
    }

}
