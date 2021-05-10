package main

import (
	"fmt"
	"sync"
	"time"
)

/**
同步
	sync.Mutex 互斥锁
		lock/unlock
	sync.RWMutex 读写锁
	sync.WaitGroup 协调，等待多个协程执行结束
	sync.Once	代码只执行一次
	sync.Cond 	唤醒执行
	sync.Map	线程安全 Map
 */
// 共享资源
var sum = 0
//var mutex sync.Mutex
var mutex sync.RWMutex

func add(i int)  {
	// 加锁 线程安全
	mutex.Lock()
	// defer 防止忘记 unlock
	defer mutex.Unlock()
	sum += i
}

func readSum () int {
	// 读操作，加读锁
	mutex.RLock()
	defer mutex.RUnlock()
	b := sum
	return b
}

/**
只会执行一次
 */
func doOnce()  {
	var once sync.Once
	onceBody := func() {
		fmt.Println("once")
	}
	for i:=0; i<10; i++ {
		go func() {
			once.Do(onceBody)
		}()
	}

}

/**
10个人赛跑，1个裁判发号施令
 */
func race(){
	cond :=sync.NewCond(&sync.Mutex{})
	var wg sync.WaitGroup
	wg.Add(11)
	for i:=0;i<10; i++ {
		go func(num int) {
			defer  wg.Done()
			fmt.Println(num,"号已经就位")
			cond.L.Lock()
			cond.Wait()//等待发令枪响
			fmt.Println(num,"号开始跑……")
			cond.L.Unlock()
		}(i)
	}
	//等待所有goroutine都进入wait状态
	time.Sleep(2*time.Second)
	go func() {
		defer  wg.Done()
		fmt.Println("裁判已经就位，准备发令枪")
		fmt.Println("比赛开始，大家准备跑")
		cond.Broadcast()//发令枪响
	}()
	//防止函数提前返回退出
	wg.Wait()
}

func main() {
	// sync.Once
	doOnce()

	// 表示有110个协程需要同步
	var wg sync.WaitGroup
	wg.Add(110)

	for i := 0; i<100; i++ {
		// 100个 gotoutine 竞争
		go func() {
			// wg 减一
			defer wg.Done()
			add(10)
		}()
	}
	for i := 0; i<10; i++ {
		go func() {
			defer wg.Done()
			fmt.Println(readSum())
		}()
	}
	// 直到等于0 ==> 上面所有协程都执行结束
	wg.Wait()
	fmt.Println(sum)

	race()
}
