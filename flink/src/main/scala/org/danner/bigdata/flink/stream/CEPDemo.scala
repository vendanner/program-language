package org.danner.bigdata.flink.stream

import java.util

import org.apache.flink.cep.{PatternFlatSelectFunction, PatternFlatTimeoutFunction, PatternSelectFunction}
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.cep.scala.CEP
import org.apache.flink.cep.scala.pattern.Pattern
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.util.Collector

import scala.collection.JavaConversions._

/**
  * pattern 在一条流上匹配，从开始到最后
  * 一：超时检测需要下一条数据进来才能触发，除非使用 TimeCharacteristic.IngestionTime
  * @Created by ztocwst
  * @Date 2020-11-09 14:21
  */
object CEPDemo {

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStreamTimeCharacteristic(TimeCharacteristic.IngestionTime)
    env.setParallelism(1)

    val source = env.socketTextStream("127.0.0.1", 1080)
    val eventStream = source.filter(x => {
        x.toString.contains(",")
      }).map(x => {
        val splits = x.toString.split(",")
        Event(splits(0), splits(1).toInt)
      }).keyBy(_.name)

    val pattern: Pattern[Event, Event] = Pattern
      .begin[Event]("start").where(_.cost > 10)
      .next("middle").where(_.cost > 30)
        .next("end").where(_.cost > 100).within(Time.seconds(10))

    val outputTag = new OutputTag[String]("timeout")
    val patternStream = CEP.pattern(eventStream, pattern)
        .flatSelect(outputTag,
          new PatternFlatTimeoutFunction[Event, String]{
            override def timeout(pattern: util.Map[String, util.List[Event]], timeoutTimestamp: Long, out: Collector[String]): Unit = {
              // 超时
              for (entry <- pattern) {
                for (event <- entry._2) {
                  out.collect("timeout: " + entry._1 + " " + event.name + "," + event.cost + " ; " + timeoutTimestamp)
                }
              }
            }
          },new PatternFlatSelectFunction[Event, String] {
            override def flatSelect(pattern: util.Map[String, util.List[Event]], out: Collector[String]): Unit = {
              for (entry <- pattern) {
                for (event <- entry._2) {
                  out.collect(entry._1 + " " + event.name + "," + event.cost)
                }
              }
            }
          })
    // 超时输出
    patternStream.getSideOutput(outputTag).print()
    // 符合条件输出
    patternStream.print()

    env.execute(this.getClass.getSimpleName)
  }

  case class Event(name:String, cost:Int)
}
