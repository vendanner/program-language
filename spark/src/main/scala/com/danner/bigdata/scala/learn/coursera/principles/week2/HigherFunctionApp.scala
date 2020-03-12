package com.danner.bigdata.scala.learn.coursera.principles.week2

object HigherFunctionApp {
    def main(args: Array[String]): Unit = {

        // 区间内连乘
        // x => x 函数当实参
        println(sum(x => x,2,5))
    }
    // 形参中包含函数
    def sum(f:Int => Int,a:Int,b:Int):Int ={
        if ( a > b) 1 else f(a) * sum(f, a + 1,b)
    }
}
