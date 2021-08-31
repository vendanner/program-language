package org.danner.bigdata.flink.sql

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.table.api.EnvironmentSettings
import org.apache.flink.table.api.bridge.scala.StreamTableEnvironment
import org.apache.flink.api.scala._


/**
 * fromDataStream dataStream 转换成 table
 */
object FromDataStreamApp {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val settings = EnvironmentSettings.newInstance()
      .useBlinkPlanner()
      .inStreamingMode()
      .build()
    val tEnv = StreamTableEnvironment.create(env,settings)

    tEnv.executeSql(
      """
        |create table print_table (
        |name string,
        |age string
        |) with (
        |'connector' = 'print'
        |)
        |""".stripMargin)

    val event = env.socketTextStream("127.0.0.1", 8001)
    val data: DataStream[(String, String)] = event.map(x => {
      val splits = x.split(",")
      (splits(0), splits(1))
    })

    // 提取 typeinfomation 组装 table schema
    val table = tEnv.fromDataStream(data)
    val statementSet = tEnv.createStatementSet()
    statementSet.addInsert("print_table", table)
    val result = statementSet.execute()
    result.getJobClient.get().getJobStatus.get()

  }
}
