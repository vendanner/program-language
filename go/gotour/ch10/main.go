package main

import (
	"context"
	"fmt"
	"sync"
	"time"
)

/**
Context
	主要用于控制多个协程之间的协作，
	func
		Dealine		设置当前 Context 的截止时间
		Done		只读 Channel，如果读到内容意味 Context 已取消
		Err			返回取消的错误原因
		Value		Context 上绑定的值，是一个键值对
	Context 类型
		可取消的 Context (context.WithCancel())
		可定时取消的 Context (context.WithDealine())
		可超时取消的 Context (context.WithTimeout())
		可携带 key-value 的Context (context.WithValue)
		通过 With 函数创造 Context 层级关系，当父 Context取消，其子Context都会取消(递归)
	使用规范
		Context 不要放在结构体中，要以参数的方式传递。
		Context 作为函数的参数时，要放在第一位，也就是第一个参数。
		要使用 context.Background 函数生成根节点的 Context，也就是最顶层的 Context。
		Context 传值要传递必须的值，而且要尽可能地少，不要什么都传。
		Context 多协程安全，可以在多个协程中放心使用。
 */

/**
开门狗，context 控制是否退出
context.WithCancel(context.Background()) 函数生成一个可以取消的 Context
 */
func watchDog(ctx context.Context, name string) {
	for {
		select {
		// 感知 context 退出
		case <- ctx.Done():
			fmt.Println(name,"收到停止指令，退出")
			return
		default:
			fmt.Println("正在监控")
		}
		time.Sleep(1 * time.Second)
	}
}

/**

 */
func getUser(ctx context.Context) {
	for  {
		select {
		case <- ctx.Done():
			fmt.Println("获取用户", "协程退出")
			return
		default:
			userId := ctx.Value("userId")
			fmt.Println("获取用户", "id = ", userId)
			time.Sleep(1 * time.Second)
		}
	}
}

func main()  {
	var wg sync.WaitGroup
	wg.Add(1)

	ctx, cancelFunc := context.WithCancel(context.Background())
	go func() {
		defer wg.Done()
		watchDog(ctx, "监控狗")
	}()
	// 带 value 的 Context，父类 ctx 取消，此 Context 自动取消
	valContext := context.WithValue(ctx, "userId", 2)
	go func() {
		getUser(valContext)
	}()

	time.Sleep(5 * time.Second)
	cancelFunc()
	wg.Wait()

}
