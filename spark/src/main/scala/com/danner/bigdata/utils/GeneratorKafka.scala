package com.danner.bigdata.utils

import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.log4j.Logger
import org.apache.spark.internal.Logging

import scala.util.Random

/**
  * kafka 生成数据
  */
object GeneratorKafka extends Logging{

    def main(args: Array[String]): Unit = {

        val TOPICNAME = "wc"
        val props = new Properties();
        props.put("bootstrap.servers", "danner002:9092,danner003:9092");
        props.put("acks", "all");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        val producer = new KafkaProducer[String,String](props)

        for(i <- 0 until 1000){
//            Thread.sleep(100)
            val word = String.valueOf(( new Random().nextInt(6) + 'a').toChar)
            val record = new ProducerRecord[String,String](TOPICNAME,i%3,"",word)
            // send 只是将 record 放到producer 池中并立即返回
            producer.send(record)
            logError(word)
        }

        producer.close()
    }
}
