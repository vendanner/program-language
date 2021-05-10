package main

import (
	"errors"
	"fmt"
	"strconv"
)

/**
错误处理
	defer 保证函数返回前，一定执行，多用于资源释放
	panic 会让程序中断运行，使程序崩溃
		不影响程序运行的错误，不要使用 panic，使用普通错误 error 即可
 */

func add(a, b int) (int, error) {
	if a<0 || b<0 {
		// errors.New 是 error 的工厂函数
		return 0, errors.New("a或者b不能为负数")
	} else {
		return a+b, nil
	}
}

/**
后进先出，栈
 */
func moreDefer() {
	defer  fmt.Println("First defer")
	defer  fmt.Println("Second defer")
	defer  fmt.Println("Three defer")
	fmt.Println("函数自身代码")
}

func main()  {
	// err 返回错误类型
	i, err := strconv.Atoi("a")
	if err != nil {
		fmt.Println(err)
	} else {
		fmt.Println(i)
	}

	// 嵌套 error：error 中包含另一个 error
	e := errors.New("原始错误e")
	// fmt.Errorf
	w := fmt.Errorf("wrap了一个错误:%w", e)
	fmt.Println(w)
	// error 解套：得到原始错误
	fmt.Println(errors.Unwrap(w))
	// 判断 error 是否相等：w 等于 e/w 包含 e，返回 true
	fmt.Println(errors.Is(w, e))

	// defer
	// f.close
	moreDefer()

	// panic
}
