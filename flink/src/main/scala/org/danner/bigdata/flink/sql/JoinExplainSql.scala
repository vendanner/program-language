package org.danner.bigdata.flink.sql

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.table.api.EnvironmentSettings
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

    println(tEnv.explainSql(
      s"""
         |insert into sink_table
         |select a.name,
         |a.cnt * b.price
         |from table1 as a
         |join table2 as b
         |on a.name = b.name
       """.stripMargin))

  }
}
