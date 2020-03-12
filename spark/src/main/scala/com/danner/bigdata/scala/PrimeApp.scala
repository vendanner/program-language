package com.danner.bigdata.scala

/**
  * 输出 100 以内的素数
  */
object PrimeApp  extends  App {

    override def main(args: Array[String]): Unit = {
        println("prime number:")
        for (i <- 1.to(100)){
            if (isPrime(i) == 0){
                println(i)
            }
        }
    }
    /**
      * 判断素数，是返回0，否则返回-1
      * @param num
      * @return
      */
    def isPrime(num:Int):Int={
        var flag = true
        var index = 2
        val size = math.sqrt(num) +1
        while ( flag && (index < size) ){
            if((num % index) == 0){
                flag = false
            }
            index += 1
        }
        if(flag) 0 else -1
    }
}
