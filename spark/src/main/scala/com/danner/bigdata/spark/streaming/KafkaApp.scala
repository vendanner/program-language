package com.danner.bigdata.spark.streaming

import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.{SparkConf, TaskContext}
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka010.{HasOffsetRanges, KafkaUtils, OffsetRange}
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.{Seconds, StreamingContext}

object KafkaApp {
    def main(args: Array[String]): Unit = {
        val TOPICNAME = "wc"
        val conf = new SparkConf().setMaster("local[2]").setAppName(WcApp.getClass.getSimpleName)
        val ssc = new StreamingContext(conf,Seconds(10))

        // http://kafka.apache.org/documentation.html#consumerconfigs 参数说明
        val kafkaParams = Map[String, Object](
            "bootstrap.servers" -> "danner001:9092,danner002:9092",
            "key.deserializer" -> classOf[StringDeserializer],
            "value.deserializer" -> classOf[StringDeserializer],
            "group.id" -> "use_a_separate_group_id_for_each_stream",
            "auto.offset.reset" -> "latest",
            "enable.auto.commit" -> (false: java.lang.Boolean)
        )
        val topics = Array(TOPICNAME)
        val record = KafkaUtils.createDirectStream[String, String](ssc,
            PreferConsistent,
            Subscribe[String, String](topics, kafkaParams))

        // 只获取 value
        val result = record.map(_.value()).flatMap(_.split(",")).map((_,1)).reduceByKey(_+_)
        result.print()

//        result.foreachRDD(rdd => {
//            if(!rdd.isEmpty()){
//                rdd.foreachPartition(partition => {
//                    val jedis = RedisUtil.getJedis()
//                    partition.foreach(pair => {
//                        jedis.hincrBy("ssc:wc",pair._1,pair._2)
//                    })
//                    jedis.close()
//                })
//            }
//        })

        // 输出offset
        record.foreachRDD(rdd =>{
            val offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
            rdd.foreachPartition { iter =>
                val o: OffsetRange = offsetRanges(TaskContext.get.partitionId)
                println(s"${o.topic} ${o.partition} ${o.fromOffset} ${o.untilOffset}")
            }
            println("----------------------------------------------")
        })


        

        ssc.start()
        ssc.awaitTermination()
    }
}
