package org.danner.bigdata.flink.stream

import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.api.scala._
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.table.api.{EnvironmentSettings, Table}
import org.apache.flink.types.Row

/**
  * TODO
  *
  * @Created by ztocwst
  * @Date 2020-08-13 07:45
  */
object WindowWordCount {

  def main(args: Array[String]): Unit = {
    val parameterTool = ParameterTool.fromArgs(args)
//    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI( new Configuration)
    val text = env.readTextFile(parameterTool.get("input")).setParallelism(1)

    env.getConfig.setGlobalJobParameters(parameterTool)

    val windowSize = parameterTool.getInt("window", 10)
    val slideSize = parameterTool.getInt("slide", 5)

    val counts = text.flatMap(_.split(",")).map((_, 1)).setParallelism(4)
      .slotSharingGroup("flatmap_sg")
      .keyBy(0)
      .countWindow(windowSize, slideSize)
      .sum(1).setParallelism(3).slotSharingGroup("sum_sg")

    counts.print().setParallelism(3)


//    val settings = EnvironmentSettings.newInstance()
//      .useBlinkPlanner()
//      .inStreamingMode()
//      .build()
//    val tEnv = StreamTableEnvironment.create(env,settings)
//
//    val names = env.fromElements('1'.toString, '2'.toString).map(x => {
//      name.apply(x)
//    })
//    tEnv.createTemporaryView("names", names)
//    val result = tEnv.sqlQuery(
//      s"""
//         |select case when id = 1 then 'ss' else 'a' end as t
//         |from names
//         """.stripMargin)
//    val rStream: DataStream[(Boolean, Row)] = tEnv.toRetractStream(result)
//    rStream.print()

    env.execute("test")
  }
  case class name(id:String)
}
