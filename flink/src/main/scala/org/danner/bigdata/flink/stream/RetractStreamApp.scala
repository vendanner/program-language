package org.danner.bigdata.flink.stream

import java.util.Properties
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.flink.table.api.scala.StreamTableEnvironment
import org.apache.flink.table.api.{EnvironmentSettings, TableConfig, TableEnvironment}
import org.apache.flink.types.Row
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.scala
import org.apache.flink.table.api.scala._
import org.danner.bigdata.flink.bean.misc


/**
  * RetractStreamTableSink
  * https://mp.weixin.qq.com/s?__biz=MzU5MTc1NDUyOA==&mid=2247483877&idx=1&sn=c722beb68ae27e3d1ae757c68a6842cc&chksm=fe2b65aac95cecbc278412a50495fc101d7cfecdbe15d7f6a3e04a5dc184dba87a983789949b&token=1090913763&lang=zh_CN#rd
  * */
object RetractStreamApp {
  def main(args: Array[String]): Unit = {
    val timeStamp = misc.getLongTime("2020-03-05 00:00:00")

    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val bsSettings = EnvironmentSettings.newInstance.useBlinkPlanner.inStreamingMode.build
    val tabEnv = StreamTableEnvironment.create(env,bsSettings)
    val kafkaConfig = new Properties()
    kafkaConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    kafkaConfig.put(ConsumerConfig.GROUP_ID_CONFIG, "test")
    val consumer = new FlinkKafkaConsumer[String]("wc", new SimpleStringSchema, kafkaConfig)
      .setStartFromTimestamp(timeStamp)

    val ds = env.addSource(consumer).map(x => {
      val splits = x.split(",")
      if(splits.length > 2) {
        (splits(0),splits(1),splits(2).toInt)
      }else if(splits.length > 1) {
        (splits(0),splits(1),0)
      }else{
        (x,"",0)
      }
    })
    tabEnv.createTemporaryView("wcTable", ds,'word,'tt,'ct)
    val rsTable = tabEnv.sqlQuery(
      s"""
         |select word,ti,sum(ct) from (
         |  select word,
         |  max(tt) as ti,
         |  max(ct) as ct
         |  from wcTable
         |  group by word
         |  ) t
         |group by word,ti
       """.stripMargin)

    val result: scala.DataStream[(Boolean, Row)] = tabEnv.toRetractStream(rsTable)
    result.print()
    /* 撤回：kafka 数据 b,a,b,a,e
      false 就是需要撤回的数据
      b,c;b,a;b,d;c,c;c,b
      最终结果是 (e,1),(a,2),(b,2)
      (true,e,1)
      (true,b,1)
      (true,a,1)
      (false,a,1)
      (true,a,2)
      (false,b,1)
      (true,b,2)
     */
    env.execute()
  }
}
