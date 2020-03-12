package org.danner.bigdata.flink.sink

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.danner.bigdata.flink.bean.misc.Access
import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.functions.sink.PrintSinkFunction
import org.apache.flink.streaming.connectors.redis.RedisSink
import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisPoolConfig

/**
  * sink  测试
  */
object SinkDemo {
    def main(args: Array[String]): Unit = {
        val redisHost = "192.168.22.147"
        val env = StreamExecutionEnvironment.getExecutionEnvironment
        env.setParallelism(3)

        val stream = env.readTextFile("data/traffics.txt")

        val value = stream.map(x => {
            val splits = x.split(",")
            Access(splits(0).toLong, splits(1), splits(2).toLong)
        }).keyBy(_.domain).sum("traffics")
        value.addSink(new PrintSinkFunction[Access])

        // 写 redis
//        val redisValue = value.map(x => (x.domain, x.traffics))
//        val conf = new FlinkJedisPoolConfig.Builder().setHost(redisHost).build()
//        redisValue.addSink(new RedisSink[(String, Long)](conf, new DannerRedisSink))

        // 写 mysql
//        val mysqlValue = value.map(x => (x.time,x.domain,x.traffics))
//        mysqlValue.addSink(new MySQLSink)


        env.execute(this.getClass.getSimpleName)
    }
}
