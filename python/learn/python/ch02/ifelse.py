#!/usr/bin/env python
# -*- coding:utf-8 -*-

"""
分支语句：

"""
score = input("请输入分数：")

try:
    score = int(score)
    if score > 90:
        print("优秀")
    elif score > 80:
        print("良好")
    elif score > 70:
        print("一般")
    elif score > 60:
        print("及格")
    else:
        print("不及格")
except ValueError as e:
    print("输入正确的数字")