package main

import (
	"fmt"
)

/**
结构体
	type struct

接口
	type interface
	实现接口中的所有的函数，就是实现了接口
	以值类型接收者实现接口的时候，不管是类型本身，还是该类型的指针类型，都实现了该接口
	以指针类型接收者实现接口的时候，只有对应的指针类型才被认为实现了该接口。
类型断言
	类型断言用来判断一个接口的值 是否为实现该接口的某个具体类型
*/

// 定义 addr 结构体
type address struct {
	province string
	city string
}

// 定义 person 结构体
// 结构体中包含结构体
// person 拥有 String() 就相当于实现 Stringer 接口(这么随意会不会不好管理)
type person struct {
	name string
	age uint8
	addr address
}

// 为 person 定义方法
func (p person) String() string {
	return fmt.Sprintf("the name is %s,age is %d",p.name,p.age)
}
func (addr address) String()  string{
	return fmt.Sprintf("the addr is %s%s",addr.province,addr.city)
}

/**
参数是 fmt.Stringer 接口
 */
func printString(s fmt.Stringer) {
	fmt.Println(s.String())
}

func main() {
	// 声明结构体变量，按顺序全部成员初始化时可省略字段名
	p := person{name:"张三", age:18}
	fmt.Println(p.name, p.age)

	p1 := person{
		name: "",
		age: 18,
		addr: address{
			province: "北京市",
			city: "北京",
		},
	}
	fmt.Println(p1.addr.province, p1.addr.city)

	// person 实现了 Stringer 接口
	printString(p)

	printString(&p)

	var s fmt.Stringer
	s = p
	// 断言 s 是否为 person 类型，否则报错
	p2 := s.(person)
	fmt.Println(p2)
	// 报错，因为 s 是 person 而不是 address
	//a := s.(address)
	//fmt.Println(a)
	a, ok := s.(address)
	if ok {
		fmt.Println(a)
	} else {
		fmt.Println("s 不是一个 address")
	}

}
