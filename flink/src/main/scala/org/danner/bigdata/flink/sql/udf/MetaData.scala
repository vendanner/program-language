package org.danner.bigdata.flink.sql.udf

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.table.api.{EnvironmentSettings, ExplainDetail}
import org.apache.flink.table.api.bridge.scala.StreamTableEnvironment

/**
 * desc:
 *
 * @author reese
 * @date 2021/12/03
 *
 */
object MetaData {
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
         | `partition` int METADATA VIRTUAL,
         | name1 STRING,
         | procTime as proctime()
         |) WITH (
         |    'connector' = 'kafka',
         |    'properties.bootstrap.servers' = '127.0.0.1:9091',
         |    'scan.startup.mode' = 'latest-offset',
         |    'topic' = 'test',
         |    'properties.group.id' = 'test_group',
         |    'format' = 'raw'
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
         | money int,
         | name1 STRING
         |) WITH (
         |'connector' = 'print'
         |)
       """.stripMargin)

    // topn
    println(tEnv.explainSql(
      s"""
         |insert into sink_table
         |select  *
         |from table1
       """.stripMargin, ExplainDetail.JSON_EXECUTION_PLAN))

  }
}
