package org.danner.bigdata.flink.ververica


import org.apache.flink.api.common.functions.FoldFunction
import org.apache.flink.api.java.functions.KeySelector
import org.apache.flink.streaming.api.functions.source.{RichParallelSourceFunction, SourceFunction}
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.api.scala._
import org.apache.flink.contrib.streaming.state.RocksDBStateBackend
import org.apache.flink.runtime.state.filesystem.FsStateBackend
import org.apache.flink.streaming.api.functions.sink.SinkFunction

import scala.collection.mutable
import scala.util.Random

/**
  * https://ververica.cn/developers/apache-flink-basic-zero-iii-datastream-api-programming/
  * 有一个数据源，它监控系统中订单的情况，当有新订单时，
  *  输出订单中商品的类型和交易额。
  * 然后，我们希望实时统计每个类别的交易额，以及实时统计全部类别的交易额。
  */
object DataStreamApi {

    /**
      * 新建数据源
      * 输出(类别，金额)
      */
    class DataSource extends RichParallelSourceFunction[(String,Int)] {

        var isRunning = true

        override def run(ctx: SourceFunction.SourceContext[(String, Int)]): Unit = {
            val random = new Random()
            while (isRunning){
                Thread.sleep( (getRuntimeContext.getIndexOfThisSubtask + 1) * 1000 * 3)
                val index = 'A'+ random.nextInt(3)
                val key = "类别" + index.toChar
                val value = 1 + random.nextInt(10)
                println(s"Emits\t($key,$value)")
                ctx.collect((key,value))
            }
        }

        override def cancel(): Unit = {
            isRunning = false
        }
    }

    def main(args: Array[String]): Unit = {
        // spi.scala
        val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
        env.enableCheckpointing(1000)
        env.getCheckpointConfig.setMaxConcurrentCheckpoints(1)
//        env.setStateBackend(new MemoryStateBackend());  //设置堆内存存储
        env.setStateBackend(new FsStateBackend("file:///E:/big_data/ruoze/workSpace/flink/checkpoint"));   //设置文件存储
//        env.setStateBackend(new RocksDBStateBackend("hdfs://192.168.22.147:9000/flink/checkpoint"));  //设置 RocksDB 存储
//        env.setStateBackend(new RocksDBStateBackend("file:///E:/big_data/ruoze/workSpace/flink/checkpoint"));
//        env.setStateBackend(new FsStateBackend("hdfs://namenode:40010/flink/checkpoints"))
        env.setParallelism(3)

        val ds = env.addSource(new DataSource())
        // 统计出每个类别的金额总计
        val keyedSum = ds.map(x =>Order(x._1,x._2)).keyBy(_.sku).sum("value")
//        keyedSum.print()

        // 统计总的：将所有数据迁移到同个 task，生产中不这样使用(用高级的 SQL 语法)
            keyedSum.keyBy(new KeySelector[Order,String]() {
            // 所有数据都赋予相同的 key ""
            override def getKey(value: Order): String = {
                ""
            }
        }).fold(mutable.Map[String,Int](),new FoldFunction[Order,mutable.Map[String,Int]] {
            // 收集成 map
            override def fold(accumulator: mutable.Map[String, Int], value: Order): mutable.Map[String, Int] = {
                accumulator.put(value.sku,value.value)
                accumulator
            }
        }).addSink(new SinkFunction[mutable.Map[String, Int]] {
            override def invoke(value: mutable.Map[String, Int], context: SinkFunction.Context[_]): Unit = {
                // 类别
                println(value)
                // 总
                val sumValue = value.values.sum
                println(s"总的金额:$sumValue")
            }
        })

        env.execute()
    }

    case class Order(sku:String,value:Int)
}
