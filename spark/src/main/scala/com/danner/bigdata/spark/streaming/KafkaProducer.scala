package com.danner.bigdata.spark.streaming

import java.util.Properties

import org.apache.kafka.clients.producer.KafkaProducer

object KafkaProducer {

    def getProducer:KafkaProducer[String,String] ={
        /**
          * kafka 生产者参数：send(record) 是把record 放到 producer buffer中
          */
        val props = new Properties();
        props.put("bootstrap.servers", "danner002:9092,danner003:9092");
        props.put("acks", "all");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        // 数据只写一次，若失败重复100次；保证数据仅消费一次
        props.put("retries", "100");
        props.put("max.in.flight.requests.per.connection", "1");
        // batch.size 是字节数针对每个分区，若大于则producer 把所有record 发送到 kafka
        props.put("batch.size","100")
        // 针对 producer 开辟的 buffer 大小
        props.put("buffer.memory","100000")
        // snappy 压缩
//        props.put("compression.type","snappy")
        // timeout 设置

        new KafkaProducer[String,String](props)
    }
}
