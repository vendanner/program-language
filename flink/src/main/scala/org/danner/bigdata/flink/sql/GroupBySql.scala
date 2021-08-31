package org.danner.bigdata.flink.sql

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.table.api.{EnvironmentSettings, ExplainDetail}
import org.apache.flink.table.api.bridge.scala.StreamTableEnvironment
import org.apache.flink.api.scala._
import org.apache.flink.table.api._

/**
 * desc:
 *
 * @author reese
 * @date 2021/08/12
 *
 */
object GroupBySql {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val settings = EnvironmentSettings.newInstance()
      .useBlinkPlanner()
      .inStreamingMode()
      .build()
    val tEnv = StreamTableEnvironment.create(env,settings)

    val socketSource = env.socketTextStream("127.0.0.1", 8001)
    val orderStream = socketSource.map(x => {
      val spilts = x.split(",")
      order(spilts(0), spilts(1))
    })
    tEnv.createTemporaryView("table1", orderStream)

    tEnv.executeSql(
      s"""
         |CREATE TABLE sink_table (
         | name STRING,
         | cnt bigint
         |) WITH (
         |'connector' = 'print'
         |)
       """.stripMargin)

    tEnv.executeSql(
      s"""
         |insert into sink_table
         |select
         |user,
         |count(1) as cnt
         |from table1
         |group by user
       """.stripMargin)
  }

  case class order(user:String, item:String)
}
