#!/usr/bin/env python
# -*- coding:utf-8 -*-
"""
字符编码：
    不同国家规定的编码就是一张表:unicode,UTF-8 都是通用格式与国家无关，但UTF-8 省空间
    python3 默认展示是  unicode
    unicode --> 编码encode -->  GBK/UTF-8
    GBK/UTF-8 --> 解码 decode --> unicode
"""

def gbk_utf():
    """

    :return:
    """
    x = "上"
    print(x)
    code_gbk = x.encode('gbk')
    code_utf = x.encode('utf-8')

    # code_gbk 此时是 gbk 编码，如果是按 'utf-8' 格式转成 unicode 会出错或乱码
    # print(code_gbk.decode('utf-8'))
    print(code_utf.decode('utf-8'))
    #   以'gbk' 格式转成 unicode 才是正确的
    print(code_gbk.decode('gbk'))

if __name__ == "__main__":
    print("unicode begin")
    gbk_utf()