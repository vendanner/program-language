package com.danner.bigdata.spark.streaming

import java.util

import com.danner.bigdata.utils.RedisUtil
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.{ HasOffsetRanges, KafkaUtils, OffsetRange}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, TaskContext}

/**
  * https://spark.apache.org/docs/latest/streaming-kafka-0-10-integration.html
  * offset 自己存储和管理 redis key: field : value = topic+groupid : partition : offset
  */
object KafkaWithOffsetRedisApp {
    def main(args: Array[String]): Unit = {
        val TOPICNAME = "wc"
        val GROUPID = "sscOffset"
        val REDISKEY = String.valueOf(TOPICNAME + ":"+GROUPID)
        val conf = new SparkConf().setMaster("local[2]").setAppName(WcApp.getClass.getSimpleName)
        val ssc = new StreamingContext(conf,Seconds(10))

        // 获取 offset
        import scala.collection.JavaConversions._
        val jedis = RedisUtil.getJedis()
        val offsets: util.Map[String, String] = jedis.hgetAll(REDISKEY)
        val fromOffsets = offsets.toMap.map(x =>{
            new TopicPartition(TOPICNAME, x._1.toInt) -> x._2.toLong
        })

        // http://kafka.apache.org/documentation.html#consumerconfigs 参数说明
        val kafkaParams = Map[String, Object](
            "bootstrap.servers" -> "danner001:9092,danner002:9092",
            "key.deserializer" -> classOf[StringDeserializer],
            "value.deserializer" -> classOf[StringDeserializer],
            "group.id" -> GROUPID,
            "auto.offset.reset" -> "earliest",
            "enable.auto.commit" -> (false: java.lang.Boolean)
        )
        val topics = Array(TOPICNAME)
        val record= KafkaUtils.createDirectStream[String, String](
            ssc,
            PreferConsistent,
            // 创建kafka 流时传入 offset
            Subscribe[String, String](topics, kafkaParams,fromOffsets))

        record.foreachRDD(rdd =>{
            if(!rdd.isEmpty()){
                // 业务逻辑处理

                // offset 存 redis
                val offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
                rdd.foreachPartition { iter =>
                    val jedis = RedisUtil.getJedis()
                    // 0-10 版本，kafka 和 ssc 的分区数相同
                    val o: OffsetRange = offsetRanges(TaskContext.get.partitionId)
                    println(s"${o.topic} ${o.partition} ${o.fromOffset} ${o.untilOffset}")

                    jedis.hset(REDISKEY,String.valueOf(o.partition),String.valueOf(o.untilOffset))
                    jedis.close()
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
