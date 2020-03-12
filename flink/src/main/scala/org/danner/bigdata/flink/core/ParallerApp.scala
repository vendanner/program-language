package org.danner.bigdata.flink.core

import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.danner.bigdata.flink.bean.misc.WC
import org.apache.flink.api.scala._

/**
  * setParallelism 并行度
  * 自定义 function
  *
  */
object ParallerApp {

    def main(args: Array[String]): Unit = {
        val host = "192.168.22.147"
        val port = 7777
        val env = StreamExecutionEnvironment.getExecutionEnvironment
        env.setParallelism(2)

        val dStream = env.socketTextStream(host,port)
        dStream.flatMap(_.split(","))
                .map(new MyMapFunction)
                .keyBy(_.word).sum("count").print()


        env.execute(this.getClass.getSimpleName)
    }

}

/**
  * 自定义function
  */
class MyMapFunction extends RichMapFunction[String,WC]{
    override def map(value: String): WC = WC(value, 1)


    /**
      * 初始化
      * @param parameters
      */
    override def open(parameters: Configuration): Unit = {
        super.open(parameters)
        println("-------------open---------------")

    }

    override def close(): Unit = {
        super.close()
        println("-----------close--------------")
    }
}
