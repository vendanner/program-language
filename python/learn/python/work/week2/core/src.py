#!/usr/bin/env python
# -*- coding:utf-8 -*-
"""
核心逻辑:
    用户登陆、注册
    用户提现、消费、还款、提额
"""
import sys
import logging

sys.path.append('../')
from conf import settings
from lib import common
from lib import logger

def init_evn(path):
    """
    初始化环境
    :param path:
    :return:
    """
    logger.init(path, logging.INFO)

def login_fun(account_list):
    """
    账号登陆过程
    :param account_list: 所有用户的账号记录
    :return: 登录状态,用户名:
                0 成功
                -1 账户异常
                1 登陆失败
    """
    for i in range(3):
        name = input('请输入账号：')
        if name in account_list:
            logger.set_username(name)
            if account_list[name][settings.STATUS_STR] == settings.NO_USE:
                logger.print_logging(logging.INFO,'账号锁定，无法使用...')
                print('账号锁定，无法使用...')
                return -1,''
            break
        else:
            print('无此账号')
    else:
        return 1,''

    for i in range(3):
        passwd = input('请输入密码：')
        if account_list[name][settings.PASSWD_STR] == passwd:
            logger.print_logging(logging.INFO, '登陆成功')
            return settings.SUCC_STR,name
        else:
            print('密码错误')
    else:
        account_list[name][settings.STATUS_STR] = settings.NO_USE
        logger.print_logging(logging.INFO, '三次密码输入错误，账号锁定，无法使用...')
        print('三次密码输入错误，账号锁定，无法使用...')
        return -1,''

def register_fun(account_list):
    """
    账号注册过程
    :param account_list:账号信息
    :return: 登录状态:
                0 成功
                -1 账户异常
                1 登陆失败
    """
    # 输入账号
    for i in range(3):
        name = input('请输入账号:')
        logger.set_username(name)
        if name in account_list:
            logger.print_logging(logging.INFO, '该账号已注册')
            print('该账号已注册')
        else:
            break
    else:
        return 1,''
    # 设置密码
    for i in range(3):
        print('提示：密码只可用数字和字母')
        passwd = input('请输入至少6位密码:')
        if common.check_passwd(passwd):
            break

        print('密码只允许是数字和字母')
    else:
        logger.print_logging(logging.INFO, '注册失败')
        return 1,''
    # 重新输入密码
    for i in range(3):
        passwd_again = input('重新输入密码:')
        if passwd_again == passwd:
            # 新增用户
            money = common.init_money()
            account_list[name] = {settings.NAME_STR:name,settings.PASSWD_STR:passwd_again,settings.MONEY_STR:money,
                                  settings.LIMIT_STR: money,settings.STATUS_STR: 0}
            print("注册成功")
            logger.print_logging(logging.INFO, '注册成功')
            return 0,name
        print('两次密码不一致...')
    else:
        logger.print_logging(logging.INFO, '注册失败')
        return 1,''

def exit():
    """
    用户退出，做清理工作
    :return:
    """
    logger.print_logging(logging.INFO,'exit')
    logger.set_username(None)

def get_money(name, account_list,buyed_item):
    """
    提现：只能提取可用额度的一半
    :param name:
    :param account_list:
    :param buyed_item:
    :return:str ;
            q:退出
            str(value):还款的金额
    """
    logger.print_logging(logging.INFO, '正在提现...')
    while True:
        print('您当前可取现金{} 元'.format( account_list[name][settings.MONEY_STR]/2))
        value = input('请输入要取的现金：')
        try:
            if value == settings.QUIT_STR:
                logger.print_logging(logging.INFO, '退出提现')
                return settings.QUIT_STR
            value = float(value)
            if value > 0 and value < account_list[name][settings.MONEY_STR]/2:
                account_list[name][settings.MONEY_STR] -= value
                print('提现成功，当前可用额度{}元'.format(account_list[name][settings.MONEY_STR]))
                logger.print_logging(logging.INFO, '成功提现 {}元'.format(value))
                return str(value)
        except ValueError as e:
            pass
        print('请输入正确的金额...')

def payment(name, account_list,buyed_item):
    """
    购物
    :param name:
    :param account_list:
    :param buyed_item:dict {{item:[item,count]}，{}}
    :return:str ;
                q:退出
    """
    logger.print_logging(logging.INFO, '正在购物...')
    while True:
        print('商城可购物品如下，请选择需要购买的商品:')
        for ind,item_info in settings.ITEM_DICT.items():
            print('{}\t{}\t{}'.format(ind,item_info[0],item_info[1]))
        try:
            index = input('请选择要购买的商品:')
            if index == settings.QUIT_STR:
                logger.print_logging(logging.INFO, '退出购物')
                return settings.QUIT_STR

            if index not in settings.ITEM_DICT:
                print('请输入正确的商品序号...')
                continue

            count = int(input('请输入要购买的数量:'))
            if count < 1:
                print('购物数量至少一件')
                continue

            money = count * settings.ITEM_DICT[index][1]
            if money > account_list[name][settings.MONEY_STR] :
                print('额度不足，请先提额或还款')
                continue
            account_list[name][settings.MONEY_STR] -= money
            if settings.ITEM_DICT[index][0] not in buyed_item:
                buyed_item[settings.ITEM_DICT[index][0]] = dict(zip([settings.NAME_STR,settings.COUNT_STR],[settings.ITEM_DICT[index][0],count]))
            else:
                buyed_item[settings.ITEM_DICT[index][0]][settings.COUNT_STR] += count
            print('成功购买{}件{}'.format(count,settings.ITEM_DICT[index][0]))
            logger.print_logging(logging.INFO, '购买{}件{}'.format(count,settings.ITEM_DICT[index][0]))
            continue
        except Exception as e:
            print(e)
            pass
        print('输入错误，重新输入')

def repayment(name, account_list,buyed_item):
    """
    还款
    :param name:
    :param account_list:
    :param buyed_item:
    :return:str ;
                q:退出
                str(value):还款的金额
    """
    logger.print_logging(logging.INFO, '正在还款...')
    while True:
        print('您当前还需还款{} 元'.format(account_list[name][settings.LIMIT_STR] -
                                   account_list[name][settings.MONEY_STR]))
        value = input('请输入要还款的金额：')
        try:
            if value == settings.QUIT_STR:
                logger.print_logging(logging.INFO, '退出还款')
                return settings.QUIT_STR
            value = float(value)
            if value > 0:
                account_list[name][settings.MONEY_STR] += value
                logger.print_logging(logging.INFO, '还款{}元'.format(value))
                return str(value)
        except ValueError as e:
            pass
        print('请输入正确的金额...')

def increased(name, account_list,buyed_item):
    """
    提额
    :param name:
    :param account_list:
    :param buyed_item:
    :return:str
                q:退出
                str(money_limit)：提额
    """
    logger.print_logging(logging.INFO, '正在提额...')
    while True:
        print('您当前额度为{} 元'.format(account_list[name][settings.LIMIT_STR]))
        print('最高额度为{} 元'.format(settings.MAX_LIMIT))
        money_limit = input('输入你要提升的额度：')
        try:
            if money_limit == settings.QUIT_STR:
                logger.print_logging(logging.INFO, '退出提额...')
                return settings.QUIT_STR
            money_limit = float(money_limit)
            # 提额要小于最大值，且大于当前额度
            if not (money_limit > settings.MAX_LIMIT or money_limit < account_list[name][settings.LIMIT_STR]):
                account_list[name][settings.MONEY_STR] += money_limit -  account_list[name][settings.LIMIT_STR]
                account_list[name][settings.LIMIT_STR] = money_limit
                print('提额成功，您当前总额度 {}元'.format(money_limit))
                logger.print_logging(logging.INFO, '提额至 {}元'.format(money_limit))
                return str(money_limit)
        except ValueError as e:
            pass
        print('请输入正确的额度...')

def goto_action(name, account_list,buyed_item):
    """
    登录成功后做提现，消费，还款，提额行为
    :param name:
    :param account_list:
    :param buyed_item:
    :return:str:
            settings.QUIT_STR：退出
    """
    action_dict = {'1':get_money,'2':payment,'3':repayment,'4':increased}
    while True:
        print('1 => 提现，2 => 消费,3 => 还款，4 => 提额,q =>退出')
        action = input('请选择：')
        if action == settings.QUIT_STR:
            return settings.QUIT_STR
        # 具体操作
        op_fun = action_dict.get(action,None)
        if op_fun == None:
            continue
        op_fun(name, account_list, buyed_item)
        # if op_fun(name, account_list,buyed_item) == settings.QUIT_STR:
        #     return settings.QUIT_STR
