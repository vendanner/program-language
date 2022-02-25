//package org.danner.bigdata.flink.sql.udf
//
//import org.apache.flink.api.java.tuple.Tuple2
//import org.apache.flink.table.functions.TableAggregateFunction
//import org.apache.flink.table.functions.TableAggregateFunction.RetractableCollector
//import org.apache.flink.util.Collector
///**
// * desc:
// *
// * @author reese
// * @date 2022/02/24
// *
// */
//object Top2WithRetract {
//
//}
//
//case class Top2WithRetractAccumulator(
//                                       var first: Integer,
//                                       var second: Integer,
//                                       var oldFirst: Integer,
//                                       var oldSecond: Integer
//                                     )
//
//class Top2WithRetract
//  extends TableAggregateFunction[Tuple2[Integer, Integer], Top2WithRetractAccumulator] {
//
//  override def createAccumulator(): Top2WithRetractAccumulator = {
//    Top2WithRetractAccumulator(
//      Integer.MIN_VALUE,
//      Integer.MIN_VALUE,
//      Integer.MIN_VALUE,
//      Integer.MIN_VALUE
//    )
//  }
//
//  def accumulate(acc: Top2WithRetractAccumulator, value: Integer): Unit = {
//    if (value > acc.first) {
//      acc.second = acc.first
//      acc.first = value
//    } else if (value > acc.second) {
//      acc.second = value
//    }
//  }
//  def emitValue(acc: Top2WithRetractAccumulator, out: Collector[Tuple2[Integer, Integer]]): Unit = {
//    // emit the value and rank
//    if (acc.first != Integer.MIN_VALUE) {
//      out.collect(Tuple2.of(acc.first, 1))
//    }
//    if (acc.second != Integer.MIN_VALUE) {
//      out.collect(Tuple2.of(acc.second, 2))
//    }
//  }
//
//  def emitUpdateWithRetract(
//                             acc: Top2WithRetractAccumulator,
//                             out: RetractableCollector[Tuple2[Integer, Integer]])
//  : Unit = {
//    if (!acc.first.equals(acc.oldFirst)) {
//      // if there is an update, retract the old value then emit a new value
//      if (acc.oldFirst != Integer.MIN_VALUE) {
//        out.retract(Tuple2.of(acc.oldFirst, 1))
//      }
//      out.collect(Tuple2.of(acc.first, 1))
//      acc.oldFirst = acc.first
//    }
//    if (!acc.second.equals(acc.oldSecond)) {
//      // if there is an update, retract the old value then emit a new value
//      if (acc.oldSecond != Integer.MIN_VALUE) {
//        out.retract(Tuple2.of(acc.oldSecond, 2))
//      }
//      out.collect(Tuple2.of(acc.second, 2))
//      acc.oldSecond = acc.second
//    }
//  }
//
//  def retract(
//               acc: Top2WithRetractAccumulator,
//               value: Integer)
//  : Unit = {}
//}