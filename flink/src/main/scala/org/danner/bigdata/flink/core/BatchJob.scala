package org.danner.bigdata.flink.core

import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment}
import org.danner.bigdata.flink.bean.misc.Access
import org.apache.flink.api.scala._

/**
  * 批处理
  * flink 对于 sql 支持很弱，blink 对flink 在 sql 方面增强
  *
  */
object BatchJob {

    def main(args: Array[String]): Unit = {

        val env = ExecutionEnvironment.getExecutionEnvironment
        env.setParallelism(2)

        val value: DataSet[String] = env.readTextFile("data/traffics.txt")
        value.map(x => {
            val splits = x.split(",")
            Access(splits(0).toLong, splits(1), splits(2).toLong)
        }).groupBy(1).sum(2).print()

    }

}
