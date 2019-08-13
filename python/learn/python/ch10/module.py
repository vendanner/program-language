#!/usr/bin/env python
# -*- coding:utf-8 -*-
"""
模块：
    首次导入：
        创建模块的命名空间
        执行模块对应文件，产生的名字存放于模块文件所对应的命名空间
        在当前执行文件中拿到模块名，该模块名指向模块文件所对应的命名空间
    查找：
        内存中已经加载的模块
        内置模块
        sys.path 路径中包含的模块

包：
    一个特殊的模块；一个文件夹就是一个包；包内必须有初始文件 __init__.py
    导入：
        导入带点时，点的左边必须是一个包
        包内模块直接导入使用： from ... import ...
        相对导入：当前执行文件以参照点导入
        绝对导入：当前执行文件所在的文件夹为参照点导入
"""

import time
import datetime
import logging

def time_fun():
    """
    time 相关函数
    :return:
    """
    # 时间戳，s;带小数点
    print(time.time())

    print(time.strftime('%Y-%m-%d %H:%M:%S %p'))
    print(time.strftime('%Y-%m-%d %X %p'))

    print(time.localtime())
    print(time.localtime().tm_year)
    print(time.gmtime())
    # 时间戳，s;
    print(time.mktime(time.localtime()))

def datetime_fun():
    """
    datetime
    :return:
    """
    print(datetime.datetime.now().now())
    # timedelta 时间加减
    print(datetime.datetime.now() + datetime.timedelta(hours=3))

def logging_fun():
    """
    logging
    :return:
    """
    logging.info('debug')
    logging.error('error')


if __name__ == '__main__':
    print('begin')
    print(time)
    time_fun()
    datetime_fun()
    logging_fun()