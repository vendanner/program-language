package org.danner.bigdata.flink.utils

import java.util.Properties

import org.apache.commons.logging.LogFactory
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

import scala.util.Random

/**
  *  kafka 生成数据
  */
object GeneratorKafka {
    private  val logger = LogFactory.getLog(this.getClass.getSimpleName)

    def main(args: Array[String]): Unit = {

        val TOPICNAME = "wc"
        val props = new Properties();
        props.put("bootstrap.servers", "hadoop002:9092,hadoop003:9092");
        props.put("acks", "all");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        val producer = new KafkaProducer[String,String](props)

        for(i <- 0 until 5){
            Thread.sleep(100)
            val word = String.valueOf(( new Random().nextInt(6) + 'a').toChar)
            val record = new ProducerRecord[String,String](TOPICNAME,i%3,"",word)
            // send 只是将 record 放到producer 池中并立即返回
            producer.send(record)
            logger.error(word)
        }

        producer.close()
    }
}
