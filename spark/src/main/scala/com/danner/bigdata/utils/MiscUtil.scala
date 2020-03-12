package com.danner.bigdata.utils

import java.security.MessageDigest

object MiscUtil {

    def hashMD5 (content:String):String={
        val md5 = MessageDigest.getInstance("MD5")
        md5.update(content.getBytes())
        md5.digest.map("%02x".format(_)).mkString
    }

    def main(args: Array[String]): Unit = {
        println(hashMD5("123"))
    }
}
