#!/usr/bin/env python
# -*- coding:utf-8 -*-

"""
日志管理
"""

import logging

access_log_path = '../log/access.log'
logger = None
filter = None

class  ContextFilter(logging.Filter):
    username = None

    def filter(self, record):
        record.username = self.username
        return True

def set_username(name):
    """
    设置操作日志 username
    :param name:
    :return:
    """
    filter.username = name

def print_logging(level,msg):
    """
    日志输出
    :param level:当前日志等级
    :param msg: 当前日志消息
    :return:
    """
    if filter.username:
        logger.log(level, msg)


def init(level=logging.DEBUG):
    """
    初始化 logging 配置
    :param path: 日志输出的文件路径
    :return:
    """
    global logger,filter
    logging.basicConfig(filename=access_log_path,level=level,
                        format='%(asctime)s\t%(name)s\t%(levelname)s\t%(username)s\t%(message)s')
    logger = logging.getLogger('credit card')
    filter = ContextFilter()
    logger.addFilter(filter)
    format = logging.Formatter('%(asctime)s\t%(name)s\t%(levelname)s\t%(username)s\t%(message)s')
    # handle = logging.FileHandler(path,encoding='utf-8')
    # handle.setLevel(level)
    # handle.setFormatter(format)
    # logger.addHandler(handle)

