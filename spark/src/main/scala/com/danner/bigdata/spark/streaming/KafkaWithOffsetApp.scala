package com.danner.bigdata.spark.streaming

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.{CanCommitOffsets, HasOffsetRanges, KafkaUtils, OffsetRange}
import org.apache.spark.streaming.{Duration, Seconds, StreamingContext}
import org.apache.spark.{SparkConf, TaskContext}

/**
  * https://spark.apache.org/docs/latest/streaming-kafka-0-10-integration.html
  * 利用Kafka 自身的offset 自动存储 _consumer_offset
  *
  * 获取 wc topic 的offset
  * ./kafka-run-class.sh kafka.tools.GetOffsetShell --broker-list danner002:9092 --topic wc
  *
  * streaming 反压防止夯住：大数据量来时，只接收部分数据
  * 1、设置固定最大值，每次接收有上限
  *     spark.streaming.kafka.maxRatePerPartition 针对每秒每个分区数据量
  *     spark.streaming.receiver.maxRate 针对receiver 每秒每个分区数据量
  * 2、动态调整每次接收最大值(根据上次数据量)
  *     spark.streaming.backpressure.enabled 开启动态调节，
  *         但最大值不应该超过上面maxRatePerPartition or maxRate
  *     spark.streaming.backpressure.initialRate
  *         若没设置，则按最大值去获取
  *         初始值既第一次获取时的值 = receiver 个数 * Seconds(10) * initialRate
  *                             kafka = 1 * 10 * 10 = 100 但很奇怪界面显示是 99
  */
object KafkaWithOffsetApp {
    def main(args: Array[String]): Unit = {
        val TOPICNAME = "wc"
        val conf = new SparkConf().setMaster("local[2]").setAppName(WcApp.getClass.getSimpleName)
        // 设置最大接收数据量，防止一下子接收太多数据无法接收
        // 最大数据量 = maxRatePerPartition * Seconds(10) * 分区数
        conf.set("spark.streaming.kafka.maxRatePerPartition","20")
        conf.set("spark.streaming.backpressure.enabled","true")
        conf.set("spark.streaming.backpressure.initialRate","10")
        val ssc = new StreamingContext(conf,Seconds(10))

        // http://kafka.apache.org/documentation.html#consumerconfigs 参数说明
        val kafkaParams = Map[String, Object](
            "bootstrap.servers" -> "danner001:9092,danner002:9092",
            "key.deserializer" -> classOf[StringDeserializer],
            "value.deserializer" -> classOf[StringDeserializer],
            "group.id" -> "use_a_separate_group_id_for_each_stream",
            "auto.offset.reset" -> "earliest",
            "enable.auto.commit" -> (false: java.lang.Boolean)
        )
        val topics = Array(TOPICNAME)
        val record: InputDStream[ConsumerRecord[String, String]] = KafkaUtils.createDirectStream[String, String](ssc,
            PreferConsistent,
            Subscribe[String, String](topics, kafkaParams))

        // 只获取 value
        val result = record.map(_.value()).flatMap(_.split(",")).map((_,1)).reduceByKey(_+_)
//        result.print()

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

                val producer = KafkaProducer.getProducer
                iter.foreach(record => {
                    producer.send(new ProducerRecord[String,String](
                        "wc_producer",o.partition,"",record.value()))
                })
                producer.close()
            }
            println("----------------------------------------------")

            // 业务逻辑处理

            // 自动提交 offset;下次处理就不是从零开始
            record.asInstanceOf[CanCommitOffsets].commitAsync(offsetRanges)
        })


        

        ssc.start()
        ssc.awaitTermination()
    }
    case class Info(name:String)
}
