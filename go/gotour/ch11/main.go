package main

import (
	"fmt"
	"sync"
	"time"
)

/**
并发模式
	select timeout 模式
		time.After(5 * time.Second) 设置超时时间防止 select 无止尽等待
	pipeline
		迭代之前产生的 chan
	扇出和扇入
		一对多的关系
	Futures：
		主协程不用等待子协程返回的结果，可以先去做其他事情，等未来需要子协程结果的时候再来取，
		如果子协程还没有返回结果，就一直等待
 */

/**
购买配件
 */
func buy(n int) <-chan string {
	out := make(chan string)
	go func() {
		defer close(out)
		for i:=1; i<11; i++ {
			out <- fmt.Sprint("配件(",i, ")")
		}
	}()
	return out
}

/**
组装配件
 */
func build(in <-chan string) <-chan string {
	out := make(chan string)
	go func() {
		defer close(out)
		for c := range in {
			out <- fmt.Sprint("组装(", c, ")")
		}
	}()
	return out
}

/**
打包
 */
func pack(in <-chan string) <-chan string {
	out := make(chan string)
	go func() {
		defer close(out)
		for c := range in {
			out <- fmt.Sprint("打包(", c, ")")
		}
	}()
	return out
}

/**
合并
 */
func merge (ins ...<-chan string) <-chan string {
	var wg sync.WaitGroup
	out := make(chan string)
	fun := func(in <-chan string) {
		defer wg.Done()
		for c := range in {
			out <- c
		}
	}
	wg.Add(len(ins))
	// 开始工作
	for _, cs := range ins {
		go fun(cs)
	}
	// 等待所有退出
	go func() {
		wg.Wait()
		close(out)
	}()

	return out
}

func washVegetables() <-chan string {
	vegetables := make(chan string)
	go func() {
		time.Sleep(5 * time.Second)
		vegetables <- "洗好的菜"
	}()
	return vegetables
}

func boilWater() <-chan string {
	water := make(chan string)
	go func() {
		time.Sleep(5 * time.Second)
		water <- "烧开的水"
	}()
	return water
}

func main() {
	buyChan := buy(10)
	buildChan := build(buyChan)
	packs := pack(buildChan)
	for p:= range packs {
		fmt.Println(p)
	}


	// 异步
	// 启动 washVegetables 和 boilWater，先不理会结果
	vegetablesCh := washVegetables()
	waterCh := boilWater()
	fmt.Println("已经安排洗菜和烧水了，我先眯一会")
	time.Sleep(2 * time.Second)

	// 需要用到，等待 washVegetables 和 boilWater 结果
	fmt.Println("要做火锅了，看看菜和水好了吗")
	vegetables := <- vegetablesCh
	water := <- waterCh
	fmt.Println("准备好了，可以做火锅了:",vegetables,water)
}
