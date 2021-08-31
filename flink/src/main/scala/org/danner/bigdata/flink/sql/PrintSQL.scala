package org.danner.bigdata.flink.sql

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.table.api.{EnvironmentSettings, ExplainDetail}
import org.apache.flink.table.api.bridge.scala.StreamTableEnvironment

object PrintSQL {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val settings = EnvironmentSettings.newInstance()
      .useBlinkPlanner()
      .inStreamingMode()
      .build()
    val tEnv = StreamTableEnvironment.create(env,settings)
    env.setParallelism(1)

    tEnv.executeSql(
      s"""
         |CREATE TABLE table1 (
         | num bigint
         |) WITH (
         |'connector' = 'datagen',
         |'rows-per-second' = '1'
         |)
       """.stripMargin)

    tEnv.executeSql(
      s"""
         |CREATE TABLE sink_table (
         | num bigint,
         | cnt bigint
         |) WITH (
         |'connector' = 'print'
         |)
       """.stripMargin)

    tEnv.executeSql(
      s"""
         |insert into sink_table
         |select
         |num,
         |count(1)
         |from table1
         |group by num
       """.stripMargin)

  }

}
