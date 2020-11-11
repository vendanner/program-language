package com.danner.bigdata.spark.core

import org.apache.spark.rdd.RDD

import scala.reflect.ClassTag

object JoinApp {

  /**
    * scoreRDD 中会有重复的 key，需求是取最大的 Double
    * 这种方式有 bug，重复 key 太多会导致数据倾斜，
    * 应该在 join 前先筛选出最大 Doule，减少shuffle 数据量和避免笛卡尔积
    * @param scoreRDD
    * @param addressRDD
    * @return
    */
  def joinScoresWithAddress1(scoreRDD: RDD[(Long, Double)],
                             addressRDD: RDD[(Long, String)]): RDD[(Long, (Double, String))] = {
    val joinedRDD = scoreRDD.join(addressRDD)
    joinedRDD.reduceByKey((x, y) => if (x._1 > y._1) x else y)
  }


  /**
    * 不同于 joinScoresWithAddress1，join 前先 filter
    * @param scoreRDD
    * @param addressRDD
    * @return
    */
  def joinScoresWithAddress2(scoreRDD : RDD[(Long, Double)],
                             addressRDD: RDD[(Long, String)]) : RDD[(Long, (Double, String))]= {
    val bestScoreData = scoreRDD.reduceByKey((x, y) => if(x > y) x else y)
    bestScoreData.join(addressRDD)
  }

  /**
    * core 层实现广播变量 join
    * @param bigRDD
    * @param smallRDD
    * @tparam K
    * @tparam V1
    * @tparam V2
    * @return
    */
  def manualBroadCastHashJoin[K : Ordering : ClassTag, V1 : ClassTag,
  V2 : ClassTag](bigRDD : RDD[(K, V1)],smallRDD : RDD[(K, V2)])= {
    val smallRDDLocal: collection.Map[K, V2] = smallRDD.collectAsMap()
    bigRDD.sparkContext.broadcast(smallRDDLocal)
    // smallRDD map side
    bigRDD.mapPartitions(iter => {
      iter.flatMap{
        case (k,v1 ) =>
          smallRDDLocal.get(k) match {
            case None => Seq.empty[(K, (V1, V2))]
            case Some(v2) => Seq((k, (v1, v2)))
          }
      }
    }, preservesPartitioning = true)
    //end:coreBroadCast[]
  }

  def main(args: Array[String]): Unit = {

  }
}
