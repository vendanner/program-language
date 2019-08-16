#!/usr/bin/env python
# -*- coding:utf-8 -*-

"""
数据相关操作
"""

import os
import json
import sys
sys.path.append('../')
from conf import settings

user_path = '../db/db.json'

def load_user_info():
    """
    获取用户信息
    :return:dict 账号信息：{name:{info}，}
    """
    account_list = {}
    if not os.path.exists(user_path):
        print(user_path,'not exists')
        return

    with open(user_path,'r') as f:
        for account_info in json.load(f):
            account_list[account_info[settings.NAME_STR]] = account_info

    return account_list
def save_user_info(account_list):
    """
    保存用户信息
    :param account_list:
    :return:
    """
    with open(user_path,'w') as f:
        json.dump(list(account_list.values()),f)

