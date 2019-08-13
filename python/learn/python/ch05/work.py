#!/usr/bin/env python
# -*- coding:utf-8 -*-

import sys
import os

def fun1(path):
    """
    保持文件顺序不变下去重
    :return:
    """
    # 输出文件原有的内容
    with open(path,'r',encoding='utf-8') as f:
        print(f.read())

    list_hash = []
    content = ''
    with open(path,'r',encoding='utf-8') as f:
        for line in f:
            hash_line = hash(line.strip())
            if hash_line not in list_hash :
                list_hash.append(hash_line)
                content += line
    with open(path,'w',encoding='utf-8') as f:
        f.write(content)

    print('*********************分隔符**************************')
    # 去重后输出
    with open(path,'r',encoding='utf-8') as f:
        print(f.read())

def copy_file(argv):
    """

    :param argv:
    :return:
    """
    if len(argv) == 3:
        src_path = argv[1]
        de_path = argv[2]
        if os.path.exists(src_path) :
            with open(src_path, 'rb') as f:
                with open(de_path,'wb') as fp:
                    fp.write(f.read())

def modify_file():
    """
    seek 修改文件
    :return:
    """
    with open('txt','r+',encoding='utf-8') as f:
        content = f.read()
        # 这里是utf-8 长度
        f.seek(9)
        f.truncate(9)
        f.write('[al]')
        # 这里字符串长度
        f.write(content[3:])

if __name__ == "__main__":
    print('begin')
    path = 'test'
    # fun1(path)
    copy_file(sys.argv)
    # modify_file()
