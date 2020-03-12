package org.danner.bigdata.flink.source

import java.util.Properties

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.danner.bigdata.flink.bean.misc
import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.danner.bigdata.flink.bean.misc.Access

/**
  * 自定义 source
  */
object SourceDemo {
    def main(args: Array[String]): Unit = {

        val topicName = "wc"
        val env = StreamExecutionEnvironment.getExecutionEnvironment
        // 设置线程数
        env.setParallelism(2)
        // 获取自定义 source ，map 转化后可以增大并行度
//        env.addSource(new AccessSource).map((_,1)).setParallelism(2).print()

        env.addSource(new MySQLSource()).print()
        val value: DataStream[Access] = env.addSource(new AccessRichParallelSource())

//        val properties = new Properties()
//        properties.setProperty("bootstrap.servers", "hadoop002:9092,hadoop003:9092,hadoop004:9092")
//        properties.setProperty("group.id", topicName)
//        import scala.collection.JavaConversions._
//        val topics = List(topicName)
//        val consumer = new FlinkKafkaConsumer(topics,new SimpleStringSchema(),properties)
//        // 最后读取
//        consumer.setStartFromLatest()
//
//        env.addSource(consumer).print()

        env.execute(this.getClass.getSimpleName)
    }
}
