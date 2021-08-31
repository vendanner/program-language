package org.danner.bigdata.flink.sql

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.table.api.{EnvironmentSettings, ExplainDetail}
import org.apache.flink.table.api.bridge.scala.StreamTableEnvironment

object JoinExplainSql {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val settings = EnvironmentSettings.newInstance()
      .useBlinkPlanner()
      .inStreamingMode()
      .build()
    val tEnv = StreamTableEnvironment.create(env,settings)

    tEnv.executeSql(
      s"""
         |CREATE TABLE table1 (
         | name STRING,
         | cnt int
         |) WITH (
         |'connector' = 'datagen'
         |)
       """.stripMargin)

    tEnv.executeSql(
      s"""
         |CREATE TABLE table2 (
         | name STRING,
         | price int
         |) WITH (
         |'connector' = 'datagen'
         |)
       """.stripMargin)

    tEnv.executeSql(
      s"""
         |CREATE TABLE sink_table (
         | name STRING,
         | money bigint
         |) WITH (
         |'connector' = 'print'
         |)
       """.stripMargin)

    // a.cnt > b.price 会在 join operation 先判断 condition(a.cnt > b.price) 是否满足再join
    // a.cnt > 10，会谓词下推到scan table
    println(tEnv.explainSql(
      s"""
         |insert into sink_table
         |select a.name,
         |a.cnt * b.price
         |from table1 as a
         |join table2 as b
         |on a.name = b.name
         |and a.cnt > b.price
       """.stripMargin, ExplainDetail.JSON_EXECUTION_PLAN))

  }
}
