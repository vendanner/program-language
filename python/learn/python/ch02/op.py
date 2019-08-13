#!/usr/bin/env python
# -*- coding:utf-8 -*-

"""
运算操作
//  --> 整除
is --> 比较变量的id 是否相同
== --> 比较变量的 value
"""

a = 20
b = 3
print(a / b)
print(a // b)

# 本程序太过简单，导致a,b 共用同个地址空间，所有 is 返回True
# 当程序复杂后，a,b 不共用空间，is 返回 False
a = '123456789aasafdfdfgfgjkjlk;klkjtdhdhfggbhfgbdgerer/。，，ijsdijdkfjsjdiosdoisdidodshiosh'
b = '123456789aasafdfdfgfgjkjlk;klkjtdhdhfggbhfgbdgerer/。，，ijsdijdkfjsjdiosdoisdidodshiosh'
print(id(a))
print(id(b))
print(a == b)
print(a is b)