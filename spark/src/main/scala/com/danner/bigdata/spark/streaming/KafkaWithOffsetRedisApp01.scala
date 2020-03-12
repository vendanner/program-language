package com.danner.bigdata.spark.streaming

import com.danner.bigdata.spark.danner.{MySQLOffsetManager, RedisOffsetManager}
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.{HasOffsetRanges, KafkaUtils, OffsetRange}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, TaskContext}

/**
  * https://spark.apache.org/docs/latest/streaming-kafka-0-10-integration.html
  * offset 自己存储和管理 redis key: field : value = topic+groupid : partition : offset
  */
object KafkaWithOffsetRedisApp01 {
    def main(args: Array[String]): Unit = {
        val topic = "wc"
        val groupId = "sscOffset"
        val conf = new SparkConf().setMaster("local[2]").setAppName(WcApp.getClass.getSimpleName)
        val ssc = new StreamingContext(conf,Seconds(10))

        // http://kafka.apache.org/documentation.html#consumerconfigs 参数说明
        val kafkaParams = Map[String, Object](
            "bootstrap.servers" -> "hadoop002:9092,hadoop004:9092",
            "key.deserializer" -> classOf[StringDeserializer],
            "value.deserializer" -> classOf[StringDeserializer],
            "group.id" -> groupId,
            "auto.offset.reset" -> "latest",
            "enable.auto.commit" -> (false: java.lang.Boolean)
        )
        val topics = Array(topic)
        val record= KafkaUtils.createDirectStream[String, String](
            ssc,
            PreferConsistent,
            // 创建kafka 流时传入 offset
            Subscribe[String, String](topics, kafkaParams,MySQLOffsetManager.obtainOffsets(topic,groupId)))

        record.foreachRDD(rdd =>{
            if(!rdd.isEmpty()){
                // 此处需要事务：业务结果以及 offset 保存
                // 业务逻辑处理


                // offset 存 redis
                val offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
                rdd.foreachPartition { _ =>
                    // 0-10 版本，kafka 和 ssc 的分区数相同
                    val o: OffsetRange = offsetRanges(TaskContext.get.partitionId)
                    println(s"${o.topic} ${o.partition} ${o.fromOffset} ${o.untilOffset}")
                    MySQLOffsetManager.storeOffsets(o.topic,groupId,o.partition,
                        o.untilOffset)
                }

            }else{
                println("当前批次没有数据")
            }
            println("----------------------------------------------")
        })

        ssc.start()
        ssc.awaitTermination()
    }
}
