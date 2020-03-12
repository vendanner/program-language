package org.danner.bigdata.flink.core

import org.apache.flink.streaming.api.scala._
import org.danner.bigdata.flink.bean.misc.WC

/**
  * flink 流处理，event 事件
  * 优点：
  *     带状态
  *     精准一次
  *     event time
  */
object StreamingJob {
    def main(args: Array[String]): Unit = {

        val host = "192.168.22.147"
        val port = 7777
        val env = StreamExecutionEnvironment.getExecutionEnvironment
        env.setParallelism(2)

        val dStream: DataStream[String] = env.socketTextStream(host,port)
        dStream.flatMap(_.split(","))
                .map(WC(_, 1))
                .keyBy(_.word).
                sum("count").print()


        env.execute(this.getClass.getSimpleName)
    }

}
