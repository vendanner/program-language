#!/usr/bin/env python
# -*- coding:utf-8 -*-
"""
程序启动

"""
import json
import os
import sys
import logging
sys.path.append('../')
from conf import settings
from core import src

user_path = '../db/db.json'
access_log_path = '../log/access.log'

def get_user_info(path):
    """
    获取用户信息
    :param path:
    :return:dict 账号信息：{name:{info}，}
    """
    account_list = {}
    if not os.path.exists(user_path):
        print(path,'not exists')
        return

    with open(path,'r') as f:
        for account_info in json.load(f):
            account_list[account_info[settings.NAME_STR]] = account_info
    return account_list

def save_user_info(path,account_list):
    """
    保存用户信息
    :param path:
    :param account_list:
    :return:
    """
    with open(path,'w') as f:
        json.dump(list(account_list.values()),f)
    src.exit()

def show_buyed_item(buyed_item):
    """
    打印用户已购商品
    :param buyed_item: 购买清单
    :return:
    """
    print('您购买的商品如下:')
    for _,item in buyed_item.items():
        print('{} 购买 {} 件'.format(item[settings.NAME_STR],item[settings.COUNT_STR]))

if __name__ == '__main__':
    print('begin')
    account_list = get_user_info(user_path)
    status = None
    name = ''
    src.init_evn(access_log_path)
    while True:
        print('1 => 登陆，2 => 注册,q =>退出')
        action = input('请选择：')
        if action == settings.LOGIN_STR:
            status ,name = src.login_fun(account_list)
        elif action == settings.REGISTER_STR:
            status ,name = src.register_fun(account_list)
        elif action == settings.QUIT_STR:
            save_user_info(user_path,account_list)
            break
        else:
            print('输出错误，请重试')
            continue

        if status == settings.SUCC_STR:
            # 登陆成功
            buyed_item = {}
            user_action = src.goto_action(name, account_list,buyed_item)
            if user_action == settings.QUIT_STR:
                show_buyed_item(buyed_item)
                print('您的可用额度：{} 元'.format(account_list[name][settings.MONEY_STR]))
                save_user_info(user_path, account_list)
                break




