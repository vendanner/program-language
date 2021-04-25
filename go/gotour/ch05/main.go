package main

import (
	"errors"
	"fmt"
)

/**
函数
	多值返回
	函数的返回值也可以有名称
	定义可变参数，只要在参数类型前加三个点
函数名称首字母小写代表私有函数，只有在同一个包中才可以被调用
函数名称首字母大写代表公有函数，不同的包也可以调用
任何一个函数都会从属于一个包

方法
	方法必须要有一个接收者，这个接收者是一个类型，称为这个类型的方法
 */

// Age 定义一个类型
type Age uint

func sum(a int, b int) (sum int, err error) {
	if a<0 || b<0 {
		return 0, errors.New("a或者b不能是负数")
	}
	// 直接对返回变量赋值
	sum = a + b
	err = nil
	return
}


/**
可变参数
 */
func sumAll(params ...int) (sum int) {
	sum = 0
	for _, i := range params {
		sum += i
	}
	return
}


/**
func() int 是闭包函数，可以使用外部的变量 i
 */
func colsure() (func() int) {
	i:=0
	return func() int {
		i++
		return i
	}
}


// 定义方法 String()
// (age Age) 为 String 方法绑定 Age 这个接收者
func (age Age) String()  {
	fmt.Println("the age is",age)
}

// Modify 接收者为指针类型
func (age *Age) Modify() {
	*age = Age(10)
}


func main() {
	value, err := sum(-1, 2)
	if err == nil {
		fmt.Println(value)
	} else {
		fmt.Println(err)
	}

	fmt.Println(sumAll(1, 2, 3, 4, 5))

	age := Age(25)
	age.String()
	age.Modify()
	// 被改为 10
	age.String()
}
