#!/usr/bin/env python
# -*- coding:utf-8 -*-

"""
用户账号相关操作
"""

import sys
sys.path.append('../')
from conf import settings
from lib import common
from db import db

account_list ={}

def load_user_info():
    """
    加载用户信息
    :return:
    """
    global account_list
    account_list = db.load_user_info()

def get_account_list():
    """
    获取用户列表
    :return: account_list
    """
    if not account_list:
        load_user_info()
    return account_list

def save_user_info():
    """
    保存用户信息
    :return:
    """
    db.save_user_info(account_list)

def check_account_exists(name):
    """
    查看是否有该用户
    :param name:
    :return: boolean
    """
    return name in account_list

def add_account(**kwargs):
    """
    新增用户
    :param kwargs:
    :return:
    """
    account_list[kwargs[settings.NAME_STR]] = kwargs

def get_account_passwd(name):
    """
    获取用户密码
    :param name:
    :return: password
    """
    return account_list[name][settings.PASSWD_STR]

def get_account_money(name):
    """
    获取用户可用额度
    :param name:
    :return: password
    """
    return account_list[name][settings.MONEY_STR]

def get_account_limit(name):
    """
    获取用户信用额度
    :param name:
    :return: password
    """
    return account_list[name][settings.LIMIT_STR]

def get_account_status(name):
    """
    获取用户账号状态
    :param name:
    :return: status
    """
    return account_list[name][settings.STATUS_STR]

def set_account_passwd(name,passwd):
    """
    设置用户密码
    :param name:
    """
    account_list[name][settings.PASSWD_STR] = passwd

def set_account_money(name,money):
    """
    设置用户可用额度
    :param name:
    """
    account_list[name][settings.MONEY_STR] = money

def set_account_limit(name,limit):
    """
    设置用户信用额度
    :param name:
    """
    account_list[name][settings.LIMIT_STR] = limit

def set_account_status(name,status):
    """
    设置用户账号状态
    :param name:
    """
    account_list[name][settings.STATUS_STR] = status