package com.danner.bigdata.spark.streaming.homework

import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.{Duration, Seconds, StreamingContext}

object PreWarningTest1 {
    def main(args: Array[String]): Unit = {

        val interval = 5
        val windowLength = 5
        val topic = "PREWARNING"
        val groupId = "ruozedata"
        val spark = SparkSession.builder()
                .master("local[2]")
                .appName("PreWarningTest")
                .getOrCreate()
        val conf = spark.sparkContext
        val ssc = new StreamingContext(conf,Seconds(interval))

        val kafkaParams = Map[String, Object](
            "bootstrap.servers" -> "hadoop002:9092,hadoop004:9092",
            "key.deserializer" -> classOf[StringDeserializer],
            "value.deserializer" -> classOf[StringDeserializer],
            "group.id" -> groupId,
            "auto.offset.reset" -> "latest",
            "enable.auto.commit" -> (false: java.lang.Boolean)
        )

//        val dStream = KafkaUtils.createDirectStream(
//            ssc,
//            PreferConsistent,
//            Subscribe[String, String](Array(topic), kafkaParams, RedisOffsetManager.obtainOffsets(topic, groupId)))

        val dStream = KafkaUtils.createDirectStream[String, String](ssc,
            PreferConsistent,
            Subscribe[String, String](Array(topic), kafkaParams))

        val isValidCHDLog = (content:String) => {
            if (content.contains("INFO") ||
                    content.contains("WARN")  ||
                    content.contains("ERROR") ||
                    content.contains("DEBUG")||
                    content.contains("FATAL")) {
                true
            }else{
                false
            }
        }

        val log = dStream.map(_.value()).filter(isValidCHDLog)
                .window(Duration(windowLength*1000),Duration(interval*1000))

        log.foreachRDD(rdd => {
            if(!rdd.isEmpty()){
                import  spark.implicits._
                // RDD[String] 先转换为 DataSet
                // DataSet 再转化为 DataFrame
                val ds = spark.createDataset(rdd)
                val df = spark.read.json(ds)
                df.show(false)
            }else{
                println("no cdh role logs in this time interval")
            }
        })



        ssc.start()
        ssc.awaitTermination()
    }
//    case class CDHRoleLog(hostName:String,serviceName:String,time:String,logType:String,content:String)
}

