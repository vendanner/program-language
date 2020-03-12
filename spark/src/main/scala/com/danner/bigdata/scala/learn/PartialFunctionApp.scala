package com.danner.bigdata.scala.learn

import scala.util.Random

object PartialFunctionApp {
    def main(args: Array[String]): Unit = {

        val names = Array("li","wang","zhang")
        val name = names(new Random().nextInt(names.length))

        def say:PartialFunction[String,String] = {
            case "li" => "name is  li"
            case "wang" => "name is wang"
            case "zhang" => "name is zhang"
            case _ => "no"
        }
        println(say(name))
    }
}
