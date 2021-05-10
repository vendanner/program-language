package main

import (
	"fmt"
	"time"
)

/**
并发
	Go 语言中没有线程的概念，只有协程
	并发是由 Go 自己所调度
	go func 开启一个协程去执行 func
Channel：线程安全的队列
	goroutine 之间通信
	无缓冲 channel 的发送和接收操作是同时进行的，它也可以称为同步 channel
	有缓冲 channel 类似一个可阻塞的队列，内部的元素先进先出
		发送操作是向队列的尾部插入元素，如果队列已满，则阻塞等待，直到另一个 goroutine 执行，接收操作释放队列的空间
		接收操作是从队列的头部获取元素并把它从队列中删除，如果队列为空，则阻塞等待，直到另一个 goroutine 执行，发送操作插入新的元素
		channel 被关闭了，就不能向里面发送数据了，如果发送的话，会引起 painc 异常。但是还可以接收 channel 里的数据，如果 channel 里没有数据的话，接收的数据是元素类型的零值。
select
	多路复用
 */

func downloadFile(chanName string) string {
	//模拟下载文件,可以自己随机time.Sleep点时间试试
	time.Sleep(time.Second)
	return chanName+":filePath"
}

func main() {
	// chan 表示 channel
	ch := make(chan string)

	go func() {
		fmt.Println("张三")
		// 给 channel 发送数据
		ch <- "goroutine 完成"
	}()

	fmt.Println("main goroutine")

	// 从 channel 接收数据
	// 阻塞直到 channel 有数据
	v := <-ch
	fmt.Println(v)

	// 有缓冲队列
	cacheCH := make(chan int, 5)
	cacheCH <- 2
	cacheCH <- 3
	fmt.Println("cacheCh 容量为:", cap(cacheCH), ",元素个数为:", len(cacheCH))

	// 单向队列
	//onlySend := make(chan <- int)
	//onlyReceive := make(<-chan int)

	firstCH := make(chan string)
	secondCH := make(chan string)
	thirdCH := make(chan string)
	go func() {
		firstCH <- downloadFile("firstCH")
	}()
	go func() {
		secondCH <- downloadFile("secondCH")
	}()
	go func() {
		thirdCH <- downloadFile("thirdCH")
	}()

	// 多路复用
	// 哪个 download 先完成就用哪个
	select {
	case filePath := <- firstCH:
		fmt.Println(filePath)
	case filePath := <- secondCH:
		fmt.Println(filePath)
	case filePath := <- thirdCH:
		fmt.Println(filePath)
	}
}
