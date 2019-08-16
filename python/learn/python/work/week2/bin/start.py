#!/usr/bin/env python
# -*- coding:utf-8 -*-

"""
程序启动

"""

import json
import sys
sys.path.append('../')
from conf import settings
from core import src,account

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
    status = None
    name = ''
    src.init_evn()
    action_dict = {'1': src.login_fun, '2': src.register_fun}
    while True:
        print('1 => 登陆，2 => 注册,q =>退出')
        action = input('请选择：')
        if action in action_dict:
            status,name = action_dict[action]()
        elif settings.QUIT_STR == action:
            src.exit()
            break
        else:
            print('输出错误，请重试')
            continue

        if status == settings.SUCC_STR:
            # 登陆成功
            buyed_item = {}
            user_action = src.goto_action(name,buyed_item)
            if user_action == settings.QUIT_STR:
                show_buyed_item(buyed_item)
                print('您的可用额度：{} 元'.format(account.get_account_money(name)))
                src.exit()
                break




