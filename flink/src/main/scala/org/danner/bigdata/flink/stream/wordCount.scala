package org.danner.bigdata.flink.stream

import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment

/**
 * flink run -c xxx.class
 */
object wordCount {
  def main(args: Array[String]): Unit = {
//    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI()
    val socketStream = env.socketTextStream("127.0.0.1", 9999)
    env.setParallelism(1)

    val countStream = socketStream.flatMap(x => {
      val splits = x.split(",")
      splits
    }).setParallelism(5).map((_, 1)).setParallelism(8).keyBy(_._1).sum(1)

    countStream.print()
    println(env.getExecutionPlan)
    env.execute("wordCount")
  }

}
