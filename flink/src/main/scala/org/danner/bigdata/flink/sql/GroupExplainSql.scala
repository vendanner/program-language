package org.danner.bigdata.flink.sql

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.table.api.{EnvironmentSettings, ExplainDetail}
import org.apache.flink.table.api.bridge.scala.StreamTableEnvironment

object GroupExplainSql {
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
         |'connector' = 'kafka',
         | 'topic' = 'products_binlog',
         | 'properties.bootstrap.servers' = '127.0.0.1:9092',
         | 'properties.group.id' = 'testGroup',
         | 'scan.startup.mode' = 'earliest-offset',
         | 'format' = 'canal-json'
         |)
       """.stripMargin)

    tEnv.executeSql(
      s"""
         |CREATE TABLE sink_table (
         | name STRING,
         | money bigint,
         | cnt bigint
         |) WITH (
         |'connector' = 'print'
         |)
       """.stripMargin)

    println(tEnv.explainSql(
      s"""
         |insert into sink_table
         |select
         |name,
         |sum(cnt) as cnt,
         |max(cnt)
         |from table1
         |group by name
       """.stripMargin, ExplainDetail.JSON_EXECUTION_PLAN))
    tEnv.executeSql(
      s"""
         |insert into sink_table
         |select
         |name,
         |sum(cnt) as cnt,
         |max(cnt)
         |from table1
         |group by name
       """.stripMargin)

  }
}

