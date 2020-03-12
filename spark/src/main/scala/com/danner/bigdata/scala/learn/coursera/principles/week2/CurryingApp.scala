package com.danner.bigdata.scala.learn.coursera.principles.week2

/**
  * currying
  */
object CurryingApp {
    def main(args: Array[String]): Unit = {

        // 只传入一个参数，返回的是以新的参数，这就是 currying
        val function = curryFun(x => x)_
        println(function(2,5))
    }
    def curryFun(f:Int => Int)(a:Int,b:Int):Int = {
        if (a > b) 1 else f(a) * curryFun(f)(a+1,b)
    }
}
