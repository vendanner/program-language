package org.danner.bigdata.flink.sql

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.table.api.EnvironmentSettings
import org.apache.flink.table.api.bridge.scala.StreamTableEnvironment

/**
  * binlog 数据直接转化为 table source
  *
  * @Created by ztocwst
  * @Date 2020-11-11 19:33
  */
object BinlogTest {

  def main(args: Array[String]): Unit = {

    val env = StreamExecutionEnvironment.getExecutionEnvironment

    val settings = EnvironmentSettings.newInstance()
      .useBlinkPlanner()
      .inStreamingMode()
      .build()

    val tEnv = StreamTableEnvironment.create(env,settings)

    tEnv.executeSql(
      s"""
         |CREATE TABLE topic_products (
         | id BIGINT,
         | name STRING,
         | description STRING,
         | weight DECIMAL(10, 2)
         |) WITH (
         |'connector' = 'kafk',
         |'topic' = 'maxwell',
         |'properties.bootstrap.servers' = 'localhost:9092',
         |'properties.group.id' = 'testGroup',
         |'format' = 'json')
       """.stripMargin)

    tEnv.executeSql(
      s"""
         |CREATE TABLE topic_sink (
         | name STRING,
         | sum_weight DECIMAL(10, 2)
         |) WITH (
         |'connector.type' = 'jdbc',
         |'connector.url' = 'jdbc:mysql://127.0.0.1:4000/test?',
         |'connector.table' = 'topic_sink',
         |'connector.driver' = 'com.mysql.jdbc.Driver',
         |'connector.username' = 'yuncang',
         |'connector.password' = 'yuncang_yDE65SGvQQLm',
         |'connector.write.flush.max-rows' = '100',
         |'connector.write.flush.interval' = '30s',
         |'connector.write.max-retries' = '3'
         |)
       """.stripMargin)

//    tEnv.executeSql(
//      s"""
//         |CREATE TABLE topic_sink (
//         | name STRING,
//         | sum_weight DECIMAL(10, 2)
//         |) WITH (
//         |'connector' = 'jdbc',
//         |'url' = 'jdbc:mysql://127.0.0.1:4000/test?',
//         |'table-name' = 'topic_sink',
//         |'driver' = 'com.mysql.jdbc.Driver',
//         |'username' = 'yuncang',
//         |'password' = 'yuncang_yDE65SGvQQLm',
//         |'sink.buffer-flush.max-rows' = '100',
//         |'sink.buffer-flush.interval' = '30s',
//         |'sink.max-retries' = '3'
//         |)
//       """.stripMargin)

    tEnv.executeSql(
      s"""
         |insert into topic_sink
         |select name,
         |sum(weight) as sum_weight
         |from topic_products
         |group by name
       """.stripMargin)

//    env.execute(this.getClass.getSimpleName)
  }
}
