#!/usr/bin/env python
# -*- coding:utf-8 -*-
"""
闭包函数：定义在函数内部的函数
装饰器：接口不变，增加功能 ==> 修改函数内部的函数代码，这样功能增加但调用函数的接口没变
"""
import time
import functools

len = 0

def f1():
    """
    变量查找顺序，离调用最近的优先级最高，print(len)
    本例中按 len 所赋的值就是 变量的优先级
    :return:
    """
    len = 1
    def f2():
        len = 3
        print(len)
    len = 2
    f2()

def outer():
    """
    返回内部函数 inner
    :return:
    """
    name = 'john'

    def inner():
        print(name)

    return inner()

def deco(func):
    def wrapper(*args,**kwargs):
        return func(*args,**kwargs)
    return wrapper

@deco
def test_decorator():
    """
    :return:
    """
    print('from test_decorator')

def timer(func):
    # 加下面的装饰器，表示会把func 所有属性都拷贝到 wrapper
    # 这样 装饰后的函数name 还是原函数，不然函数名是 wrapper
    @functools.wraps(func)
    def wrapper(* args, ** kwargs):
        start_time = time.time()
        res = func(*args,**kwargs)
        print(time.time() - start_time)
        return res
    # 返回的是函数名，没有括号
    return wrapper

# 将上面的 timer 函数装饰上
@timer
def index():
    time.sleep(1)
    print('index output')
    return 2

if __name__ == '__main__':
    # f1()
    # f = outer()
    # # 此 name 不能改变 f 函数的值，
    # # f 中name 在 outer 返回时已固定
    # name = 'li'
    # f
    # test_decorator()
    print(index())