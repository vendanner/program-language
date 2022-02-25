package org.danner.bigdata.flink.sql.udf

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.table.api.{AnyWithOperations, EnvironmentSettings, ExplainDetail, FieldExpression, call}
import org.apache.flink.table.api.bridge.scala.StreamTableEnvironment
import org.danner.bigdata.flink.udf.Top2WithRetract

/**
 * desc:
 *
 * @author reese
 * @date 2022/02/24
 *
 */
object UATF {

  def main(args: Array[String]): Unit = {

    val env = StreamExecutionEnvironment.getExecutionEnvironment

    val settings = EnvironmentSettings.newInstance()
      .useBlinkPlanner()
      .inStreamingMode()
      .build()

    val tEnv = StreamTableEnvironment.create(env, settings)

    tEnv.executeSql(
      s"""
         |CREATE TABLE topic_products (
         | id BIGINT,
         | name STRING,
         | price int,
         | ts bigint
         |) WITH (
         |'connector' = 'kafka',
         |'topic' = 'maxwell',
         |'properties.bootstrap.servers' = 'localhost:9092',
         |'properties.group.id' = 'testGroup',
         |'format' = 'json')
       """.stripMargin)

    tEnv.executeSql(
      s"""
         |CREATE TABLE sink_table (
         | name STRING,
         | money int,
         | cnt bigint
         |) WITH (
         |'connector' = 'print'
         |)
       """.stripMargin)


    tEnv.executeSql(
      s"""
         |create view price_view as
         |select id, name,price
         |from (
         |select *, ROW_NUMBER() OVER (partition by id ORDER by ts asc) as rk
         |from topic_products
         |) a
         |where rk=1
       """.stripMargin)

    val table = tEnv.from("price_view").groupBy($"name")
      .flatAggregate(call(classOf[Top2WithRetract], $"price").as("price", "rk"))
      .select($"name", $"price", $"rk")

    tEnv.createTemporaryView("top_view", table)

    println(tEnv.explainSql(
      s"""
         |insert into sink_table
         |select
         | *
         |from top_view
       """.stripMargin, ExplainDetail.JSON_EXECUTION_PLAN))

  }

}
