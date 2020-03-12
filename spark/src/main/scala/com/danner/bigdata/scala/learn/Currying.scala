package com.danner.bigdata.scala.learn

object Currying {
    def main(args: Array[String]): Unit = {

        // 参数必须分开写
        def sum(a:Int)(b:Int)=a+b

        // 柯里化产生新的函数 sum9
        // 此函数功能：形参 + 9
        val sum9 = sum(9)_  //还有参数必须加 "_"

        println(sum9(5))    // 14
        println(sum9(10))   // 19
    }
}
