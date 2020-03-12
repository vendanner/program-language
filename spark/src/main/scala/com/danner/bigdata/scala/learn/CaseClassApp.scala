package com.danner.bigdata.scala.learn

object CaseClassApp {
    def main(args: Array[String]): Unit = {
//        println(Dog("john").name)
        val dog1 = Dog("john")
        val dog2 = Dog("john")
        println(dog1 == dog2) // true
    }

    case class Dog(name:String)
}
