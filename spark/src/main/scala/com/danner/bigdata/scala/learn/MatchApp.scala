package com.danner.bigdata.scala.learn

import scala.util.Random
import scala.util.matching.Regex.Match

/**
  * match ：内容、类型、集合、case class、异常
  */
object MatchApp {

    abstract class an
    case class Person(name:String) extends an

    case class Dog(name:String) extends an

    def main(args: Array[String]): Unit = {

        // 匹配字符串
//        val names = Array("li","wang","zhang")
//        var name = names(Random.nextInt(names.length))
//        name match {
//            case "li" => println("name is li")
//            case "wang" => println("name is wang")
//            case "zhang" => println("name is zhang")
//            case _ => println("no name")
//        }

        // 匹配类型
//        val dataList = Array(1,"aa",2f)
//        val data = dataList(Random.nextInt(dataList.length))
//        data match {
//            case i:Int => println("Int data")
//            case s:String => println("string")
//            case f:Float => println("float")
//            case _ => println("no type")
//        }

        // 匹配集合(还可以是 List、Tup)
//        val stringList = Array("a","b","c","music")
//        stringList match {
//            case Array("math",x,y) => println("starWith math")
//            case Array(z,x,y) => println(s"thress ele: $z,$x,$y")
//            case Array(_,_,_,"music")  => println("endWith music") // 四个元素且结尾是 music
//            case _ => println("no")
//        }

        // case class
        var caseClass = Person("")
        caseClass match {
            case Person("li") => println("li person")  // 精确到类对象
            case p:Person => println("Person")          // 只匹配类
//            case Dog("john") => println("dog john")
//            case d:Dog => println("Doh")
            case _ => println("no ...")

        }

    }


}
