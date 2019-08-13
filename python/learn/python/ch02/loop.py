#!/usr/bin/env python
# -*- coding:utf-8 -*-
"""
循环控制：
    while(条件)：False，0，'',{},(),[] 都为 False
        while
            ...
        else
            while 不满足条件退出，才会执行 else 代码，break 退出不会执行
    for 迭代
        for
            ...
        else
            类同 while ... else
"""

def fun_while():
    """
    会执行 else 代码
    :return:
    """
    count = 0
    while count < 5:
        count += 1
    else:
        print("count >= 5")

def fun_for():
    """
    类同while...else
    :return:
    """
    count = 0
    for i in range(10):
        count += i
        print(count)
        if count > 7:
            break
    else:
        print("ending")

if __name__ == "__main__":
    fun_while()
    fun_for()