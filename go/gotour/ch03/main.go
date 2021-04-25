package main

import "fmt"

/**
流程控制：
	if
	switch
		case 后自带 break
		fallthrough 关键字，允许继续往下执行(类似其他语言 case 后没有 break)
	for
		go 中没有 while 循环
		同样支持使用 continue、break
 */
func main ()  {
	if i:=6; i > 10 {
		fmt.Println("i>10")
	} else if i>5 && i<10 {
		fmt.Println("i>5 && i<10")
	} else {
		fmt.Println("i<5")
	}

	switch i := 6;i {
	case 1:
		fallthrough
	case 2:
		fmt.Println("1")
	default:
		fmt.Println("没有匹配")
	}

	sum := 0
	i := 1
	for {
		sum += i
		i++
		if i > 10 {
			break
		}
	}
	fmt.Println("sum=", sum)
}
