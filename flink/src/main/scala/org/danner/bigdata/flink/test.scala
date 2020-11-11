package org.danner.bigdata.flink

import java.util.Collections

import com.google.common.collect.Lists

import scala.collection.JavaConversions



/**
  * TODO
  *
  * @Created by ztocwst
  * @Date 2020-08-22 12:13
  */
object test {

  @transient protected lazy val buffer = Collections.synchronizedList[Seq[Any]](Lists.newArrayListWithCapacity(10))

  def main(args: Array[String]): Unit = {
    this.buffer.add(Seq("1","2"))
    this.buffer.add(Seq("2","1"))
    this.buffer.add(Seq("3","2"))
    this.buffer.add(Seq("1","2","3"))
    this.buffer.add(Seq("1","2"))
    this.buffer.add(Seq("a","1","b"))
    this.buffer.add(Seq("1","2","4"))
    this.buffer.add(Seq("1","2","5"))
    executeBatch(JavaConversions.asScalaBuffer(this.buffer))


  }

  def executeBatch(paramsList: Seq[Seq[Any]] = null): Unit ={
    paramsList.foreach(params => {
      var i = 1
      params.foreach(param => {
       println(param)
      })
      i = i + 1
      println(i + "*****************")
    })
  }
}
