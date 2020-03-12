package org.danner.bigdata.flink.source

import org.apache.flink.streaming.api.functions.source.{RichSourceFunction, SourceFunction}
import org.danner.bigdata.flink.bean.misc.Access

import scala.util.Random

/**
  * 自定义 source 并行度固定位 1
  */
class AccessSource  extends SourceFunction[Access]{
    var running = true
    override def cancel(): Unit = {
        running = false
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
