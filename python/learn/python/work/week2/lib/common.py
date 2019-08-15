#!/usr/bin/env python
# -*- coding:utf-8 -*-

"""
公用方法
logger：https://www.cnblogs.com/yyds/p/6901864.html
"""

import random
import re
import logging

def init_money():
    """
    随机 money
    :return: float money
    """
    return random.randint(0, 10) * 10000 + 10000.0

def check_passwd(passwd):
    """
    校验密码，只允许数字和字母
    :param passwd:
    :return: boolean True or False
    """
    if len(passwd) < 6:
        return False

    pattern = re.compile('^[a-z0-9A-Z]+')
    match = pattern.findall(passwd)
    if match and len(passwd) == len(match[0]) :
        return True

    return False