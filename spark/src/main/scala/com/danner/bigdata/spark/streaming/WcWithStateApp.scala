package com.danner.bigdata.spark.streaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, State, StateSpec, StreamingContext}

/**
  * spark streaming wc
  * updateStateByKey ：
  *             重点在于 update 状态
  *             不管有没有数据都会出去触发更新操作
  *
  * mapWithState :
  *             有两个值：mappingdata，statedata
  *             结果就是 mappingFunction 返回值 mappingdata ，可以改变数据
  *             stateSnapshots 可以获取(key,statedata)，用来记录状态
  *             没有数据到来不会触发更新操作
  *
  * 两者都必须设置 checkpoint，但 checkpoint 只能在代码不变的情况下才能读取，很鸡肋
  */
object WcWithStateApp {
    def main(args: Array[String]): Unit = {
        val HOSTNAME = "192.168.22.147"
        val PORT = 9999
        val KEY = "WcApp_key"

        val conf = new SparkConf().setMaster("local[2]").setAppName(WcApp.getClass.getSimpleName)
        val ssc = new StreamingContext(conf,Seconds(10))
        ssc.checkpoint(".")

        val dStream = ssc.socketTextStream(HOSTNAME,PORT)

        val spec = StateSpec.function(mappingFunction _).numPartitions(ssc.sparkContext.defaultParallelism)
        val result = dStream.flatMap(_.split(",")).map((_,1))
                // 能实现key 累计(redis 的hincrby 其实也能实现)
//                .updateStateByKey[Int](updateFunction _)
                .mapWithState(spec)
                // stateSnapshots 获取 DStream[(KeyType, StateType)]
                .stateSnapshots()

        result.print()

        ssc.start()
        ssc.awaitTermination()

    }

    /**
      * 更新状态函数
      * @param newValues 当前key值 [1,1,1,1,1]
      * @param preCount 之前累加值
      * @return
      */
    def updateFunction(newValues: Seq[Int], preCount: Option[Int]): Option[Int] = {
        val currCount = newValues.sum
        val sumCount = currCount + preCount.getOrElse(0)
        Some(sumCount)
    }

    /**
      *
      * @param key
      * @param value 当前值
      * @param state 历史状态
      * @return mapWithState 函数最终返回的格式
      */
    def mappingFunction(key: String, value: Option[Int], state: State[Int]) = {
        // Use state.exists(), state.get(), state.update() and state.remove()
        // to manage state, and return the necessary string
        val sum = value.getOrElse(0) + state.getOption().getOrElse(0)
        // 更新 state
        state.update(sum)
        // 返回 mappingdata
        Some(key)
     }
}
