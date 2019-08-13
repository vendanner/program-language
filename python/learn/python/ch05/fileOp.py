#!/usr/bin/env python
# -*- coding:utf-8 -*-
"""
文件操作：
    open(file,mode='',encoding='编码')，默认以'rt' 文本只读模式打开
        操作模式：
            r:  读
            w:  写
            a:  追加
        操作大小：
            t:  文本，以 word 单位
            b:  二进制 以 byte 单位，不能单独使用，需搭配'rb','wb','ab'，
            文本可以以二进制读取，但图片、音频等流文件不能以文本形式读取
        seek(偏移量，起始位置) 移动光标
            起始位置:
            0:  文件开头
            1:  当前位置
            2:  文件末尾
"""

def file_format():
    """

    :return:
    """
    # 文件以 utf-8 存储,以 'utf-8' 模型读
    with open('test','r',encoding='utf-8') as f:
        data = f.read()
        print(data)

if __name__ == "__main__":
    print("file operation begin")
    file_format()