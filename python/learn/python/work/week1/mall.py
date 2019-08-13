#!/usr/bin/env python
# -*- coding:utf-8 -*-

import os

if __name__ == '__main__':
    print('begin')

    path = 'account'
    dict_item = {1:['iPhone',6000],2:['macbook',15000],3:['bike',1000]}
    list_item_buy = []

    # 用户是否有注册
    if not os.path.exists(path):
        # 注册流程
        print('新用户先注册')
        name = input('请输入用户名:')
        while True:
            passwd = input('请输入密码:')
            passwd1 = input('请重新输入密码:')
            if passwd != passwd1:
                print('两次密码不一致，请重新输入')
            else:
                break

        while True:
            try:
                money = int(input('请输入充值金额:'))
                break
            except ValueError as e:
                print('输入正确的金额数字')
                pass

        print('注册成功...')

    else:
        with open(path, 'r',encoding='utf-8') as f:
            lists = f.read().split('|')
            name,passwd,money = lists[0],lists[1],int(lists[2])
            print('开始登陆...')

    # 开始购物
    print('开始购物')
