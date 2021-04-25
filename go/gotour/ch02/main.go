package main

import (
	"fmt"
	"strconv"
	"strings"
)

/**
数据类型
一：零值就是数据类型的初始值
二：指针对应的是变量在内存中的存储位置，符号 &
三：常量描述符 const
四：类型转换 strconv
五：字符串包 strings
*/
func main () {

	var str = "123"
	str += "456"
	fmt.Println(str)

	// 指针，输出 str 内存地址
	var point = &str
	fmt.Println(point)
	// 输出指针指向的内容 ==> str
	fmt.Println(*point)

	// 数字/字符串转换
	i2s, err := strconv.Atoi(str)
	if err == nil {
		fmt.Println(i2s)
		// 字符串转数字
		fmt.Println(strconv.Itoa(i2s))
	}

	// Strings
	fmt.Println(strings.HasPrefix("hello", "h"))
	fmt.Println(strings.Index("world", "l"))
}