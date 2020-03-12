package com.danner.bigdata.scala.learn

import scala.collection.immutable.StringOps


object ImplicitApp {

    def say(implicit word:String) = println(s"hello $word")

    def main(args: Array[String]): Unit = {

        // 为现有 Man 类添加 fly 函数
//        implicit def man2superman(man: Man) = new Superman(man.name)
//        val man = new Man("danner")
//        man.fly()

        implicit def str2Int(string: String):Int = new StringOps(string).toInt
        // 无声息地将字符串转化为 Int
        println(math.max("2",3))

        implicit val word = "danner"
        say

        implicit class EnMan(man:Man){
            def fly(): Unit ={
                println(s"EnMan ${man.name} can fly ...")
            }
        }

        val john = new Man("john")
        john.fly()

    }
}

class Man(val name:String)

class Superman(val name:String) {
    def fly(): Unit = {
        println(s"$name fly ...")
    }
}