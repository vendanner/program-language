package main

import (
	"fmt"
	"unicode/utf8"
)

/**
集合类型
  数组（array）
	固定长度、相同类型的数据
	长度也是数组类型的一部分
	下标从 0 开始
  切片（slice）= 动态数组(可以理解为指针)
	基于数组实现的，它的底层就是一个数组
	数组的指针 data，长度 len 和容量 cap
  映射（map）
	无序的 K-V 键值对集合
	map 是没有容量的，它只有长度
  string
	注意字符编码问题
 */
func main()  {
	array := [5]string{"1", "2", "3", "4", "5"}
	for i:=0;i< len(array);i++ {
		fmt.Printf("数组索引:%d,对应值:%s\n", i, array[i])
	}
	// range
	fmt.Println("********** range **********")
	for i, v := range array {
		fmt.Printf("数组索引:%d,对应值:%s\n", i, v)
	}

	// slice
	fmt.Println("********** slice **********")
	slice := array[2:5]
	slice[1] = "9"
	// array[3] 被改为9，说明 slice 底层还是原先的 array
	// 改变 slice 会改变 array
	fmt.Println(array)
	slice1 := append(slice, "6")
	fmt.Println(slice1)
	slice1[1] = "8"
	// append 时，如果原先的 slice 容量不够会新生成数组，在新数组上分片
	// slice1 改变某个值，但不影响到 array 值
	fmt.Println(array)

	// map
	fmt.Println("********** map **********")
	nameAgeMap := make(map[string]int)
	// 相当于 upsert 语法，没有则新增，有则更新
	nameAgeMap["张三"] = 18
	age, ok := nameAgeMap["张三"]
	if ok {
		// map get返回两个值，value 和 status,status正常才表示有 value
		fmt.Println(age)
	}
	delete(nameAgeMap, "李四")

	// 字符编码
	fmt.Println("********** string **********")
	s := "李四123"
	// 转换为字节切片，[]
	bytes := []byte(s)
	// 长度都为9，UTF8 下每个汉子占三个字节
	// len 是以字节为单位
	fmt.Printf("bytes len=%d\n", len(bytes))
	fmt.Printf("s len=%d\n", len(s))
	fmt.Printf("s 字符长度=%d\n", utf8.RuneCountInString(s))
	// for range 时，自动隐式解码 unicode 字符串
	for i, tmp_char := range s {
		fmt.Println(i, string(tmp_char))
	}
	// 转为 rune 切片，解决中文问题
	name := []rune(s)
	fmt.Println(string(name[:2]))
}
