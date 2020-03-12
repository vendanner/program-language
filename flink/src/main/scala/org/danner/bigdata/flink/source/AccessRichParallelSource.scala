package org.danner.bigdata.flink.source

import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.source.{ParallelSourceFunction, RichParallelSourceFunction, SourceFunction}
import org.danner.bigdata.flink.bean.misc.Access

import scala.util.Random

/**
  * 自定义 RichParallelSource ，并行度可自行设置，并有生命周期
  */
class AccessRichParallelSource  extends RichParallelSourceFunction[Access]{

    var running = true
    override def cancel(): Unit = {
        running = false
    }

    /**
      * 并行度
      * @param parameters
      */
    override def open(parameters: Configuration): Unit = {
        super.open(parameters)
        println("-------------- AccessRichParallelSource open ------------------")
    }

    override def close(): Unit = {
        super.close()
        println("-------------- AccessRichParallelSource close -----------------")
    }
    /**
      * 获取数据源
      * @param ctx
      */
    override def run(ctx: SourceFunction.SourceContext[Access]): Unit = {
        val random = new Random()
        val array = Array("www","html","http")

        while (running){
            // 输出数据
            val timeStamp = System.currentTimeMillis()
            0.to(5).map(x => {
                ctx.collect(Access(timeStamp,array(random.nextInt(array.length)),random.nextInt(1000).toLong))
            })

            Thread.sleep(5000)
        }
    }

}
