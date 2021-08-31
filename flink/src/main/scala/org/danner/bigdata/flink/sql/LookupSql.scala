package org.danner.bigdata.flink.sql

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.table.api.bridge.scala.StreamTableEnvironment
import org.apache.flink.table.api.{EnvironmentSettings, ExplainDetail}

object LookupSql {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val settings = EnvironmentSettings.newInstance()
      .useBlinkPlanner()
      .inStreamingMode()
      .build()
    val tEnv = StreamTableEnvironment.create(env, settings)

    tEnv.executeSql(
      s"""
         |CREATE TABLE table1 (
         | name STRING,
         | cnt int,
         | procTime as proctime()
         |) WITH (
         |    'connector' = 'kafka',
         |    'properties.bootstrap.servers' = '127.0.0.1:9091',
         |    'scan.startup.mode' = 'latest-offset',
         |    'topic' = 'test',
         |    'properties.group.id' = 'test_group',
         |    'format' = 'json'
         |)
       """.stripMargin)

    tEnv.executeSql(
      s"""
         |CREATE TABLE mysql_table (
         | name STRING,
         | price int
         |) WITH (
         | 'connector' = 'jdbc',
         | 'url' = 'jdbc:mysql://localhost:3306/database',
         | 'username' = 'test',
         | 'password' = 'test',
         | 'table-name' = 'test'
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

//    println(tEnv.explainSql(
//      s"""
//         |insert into sink_table
//         |select a.name,
//         |a.cnt * b.price
//         |from table1 as a
//         |left join mysql_table as b
//         |on a.name = b.name
//         |and a.cnt > b.price
//       """.stripMargin, ExplainDetail.JSON_EXECUTION_PLAN))

    // 维表 join
    println(tEnv.explainSql(
      s"""
         |insert into sink_table
         |select a.name,
         |a.cnt * b.price
         |from table1 as a
         |left join mysql_table FOR SYSTEM_TIME AS OF a.procTime as b
         |on a.name = b.name
         |and a.cnt > b.price
       """.stripMargin, ExplainDetail.JSON_EXECUTION_PLAN))

  }
}
