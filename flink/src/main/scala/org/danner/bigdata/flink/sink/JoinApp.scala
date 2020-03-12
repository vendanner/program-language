package org.danner.bigdata.flink.sink

import java.util.Properties

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.functions.source.{RichSourceFunction, SourceFunction}
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.danner.bigdata.flink.utils.ScaLikeJDBCUtil
import scalikejdbc.WrappedResultSet
import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.functions.co.CoFlatMapFunction
import org.apache.flink.streaming.api.scala.function.ProcessAllWindowFunction
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

/**
  * 实现类似 mapJoin 的功能
  * 从kafka 读取数据 a-f ...，kstream
  * 从 mysql 读取数据 a-f 以及对应数字
  * 然后 为kstream 对应字幕添加对应的数字
  */
object JoinApp {

    def main(args: Array[String]): Unit = {
        val topicName = "wc"
        val env = StreamExecutionEnvironment.getExecutionEnvironment

        env.setParallelism(4)

        // mysql 读取数据  mutable.HashMap[String,String]
        val mysqlStream = env.addSource(new SourceFunction[Map[String,String]]{
            override def run(ctx: SourceFunction.SourceContext[Map[String,String]]): Unit = {
                val sql = "select * from word_int"
                val function = (rs: WrappedResultSet) => {
                    val word = rs.string("word")
                    val traffic = rs.long("traffic")
                    (word,traffic.toString)
                }
                val map: Map[String, String] = ScaLikeJDBCUtil.query(sql,function).toMap
                if(map.nonEmpty){
                    ctx.collect(map)
                }
            }
            override def cancel(): Unit = {
            }
        }).broadcast                // 广播变量，会自动给每个task 复制一份
        //        mysqlStream.print()

        // kafka source
        val properties = new Properties()
        properties.setProperty("bootstrap.servers", "hadoop002:9092,hadoop003:9092,hadoop004:9092")
        properties.setProperty("group.id", topicName)
        import scala.collection.JavaConversions._
        val topics = List(topicName)
        val consumer = new FlinkKafkaConsumer(topics,new SimpleStringSchema(),properties)
        // 读取最后
        consumer.setStartFromEarliest()

        val kStream = env.addSource(consumer)
        kStream.print()

        val result: DataStream[String] = kStream.timeWindowAll(Time.seconds(5)).process(new ProcessAllWindowFunction[String, String, TimeWindow]() {
            override def process(context: Context, elements: Iterable[String], out: Collector[String]): Unit = {
                for (content <- elements) {
                    out.collect(content)
                }
            }
        })

        // 合并
        kStream.connect(mysqlStream).flatMap(MyCoFlatMapFunction()).print()

        env.execute(this.getClass.getSimpleName)
    }
}
