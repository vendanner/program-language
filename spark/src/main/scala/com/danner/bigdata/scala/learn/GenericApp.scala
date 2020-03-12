package com.danner.bigdata.scala.learn

object GenericApp {
    def main(args: Array[String]): Unit = {

        // 只能是子类
//        testUpperBounds(new Child)
//        testUpperBounds(new User)
//        // scala 2.11.8 是可以传 Child
//        testLowerBounds(new Person)
//        testLowerBounds(new Child)
//        testLowerBounds(new User)

//        val child = new Test[Child]
//        println(child)
        val person = new Test[Person]
        println(person)
    }
    // 上界：约束 T 是 User 或子类
    def testUpperBounds[T <: User](t:T): Unit ={
        println(t)
    }
    // 下界：约束 T 是 User 或父类?不同版本表现不同，
    def testLowerBounds[T >: User](t:T): Unit ={
        println(t)
    }
}
class Person
class User extends Person
class Child extends User
// 协变
//class Test[+User]
class Test[-User]