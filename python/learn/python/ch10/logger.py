#!/usr/bin/env python
# -*- coding:utf-8 -*-

"""
logging 练习
    模块：
        debug、info、warning、error、critical
        模块功能由组件组合来实现
    组件：
        Logger：日志器 需要通过处理器（handler）将日志信息输出到目标位置;
                       可以设置多个处理器（handler）将同一条日志记录输出到不同的位置
        Handler：处理器 不同的处理器（handler）可以将日志输出到不同的位置
                        可以设置自己的过滤器（filter）实现日志过滤，从而只保留感兴趣的日志
                        设置自己的格式器（formatter）实现同一条日志以不同的格式输出到不同的地方
        Filter：过滤器
        Formatter：格式器
        简单点说就是：日志器（logger）是入口，真正干活儿的是处理器（handler），
    处理器（handler）还可以通过过滤器（filter）和格式器（formatter）对要输出的日志内容做过滤和格式化等处理操作。
"""

import logging

path = 'access.log'
# asctime 时间，levelname 日志级别，message 信息
LOG_FORMAT = "%(asctime)s - %(levelname)s - %(message)s"
# 日志输出到文件
logging.basicConfig(
    filename=path,
    level=logging.INFO,
    format=LOG_FORMAT
    )



if __name__ == '__main__':
    print('begin')
    logging.info("info")
    logging.warning('warning')
    logging.error("error")