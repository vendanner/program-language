#!/usr/bin/env python
# -*- coding:utf-8 -*-
"""
python3 input 接收用户输入，都存成字符串类型，后续自行转换
"""

age = input('请输入年龄：')

try:
    # int() 将原先input 的字符串转换成 int
    age = int(age)
    print(age)
except ValueError as e:
    # 考虑输入不是数字的情况
    print("请输入正确的数字")