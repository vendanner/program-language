package com.danner.bigdata.scala

/**
  * scala 类学习
  * 执行顺序：父类、辅助构造器this或主构造器，静态代码，构造函数中剩余代码
  */
object PersonApp {

    def main(args: Array[String]): Unit = {
//        val person = new Person("li",30)
//        person.eat("apple")

        // 类名() 实质是调用 object.apply 方法,apply 一般是去创建 class 对象
        val student = Student("li",18,200)
        student.learn("math")
        // 对象() 实质是调用class.applay，
        student()
    }
}

/**
  *
  *  主构造的形参就是类的属性，默认是private，加val/var 修饰后变为 public
  * @param name
  * @param age
  */
class Person(var name:String,var age:Int){

    private var gender:String = _
    val weight = "11"
    println("Person class init start ...")

    /**
      * 辅助构造器函数名 this，代码第一行必须先调用主构造器或者其他辅助构造器
      *  辅助构造器的形参默认不是类的属性，除非是已在住构造器中定义;形参不能用 val/var 修饰
      * @param name
      * @param age
      * @param gender
      */
    def this(name:String,age:Int,gender:String){
        this(name,age)
        this.gender = gender
        println("Person init fun")
    }
    def eat(food:String): Unit ={
        println(name + " " +  " eat " + food)
    }

    println("Person class init end ...")
}

/**
  * 子类继承父类时必须选择一个父类的构造器
  * 只有父类的val 属性才能重载
  * @param name
  * @param age
  * @param id
  */
class Student(name:String,age:Int,id:Int) extends  Person(name,age){

    println("Studen class init start ...")

    var school = ""
    override val weight: String = "fsd"

    def apply():Unit = {
        println("class Student apply")
    }

    def this(name:String,age:Int,id:Int,school:String){
        this(name,age,id)
        this.school = school
        println("Student init fun")
    }
    def learn(classes:String): Unit ={
        println(name + " is learn " + classes + " " + id)
    }

    println("Studen class init end ...")
}

object Student{
    def apply(name: String, age: Int, id: Int): Student ={
        println("object Student apply")
        new Student(name, age, id)
    }
}