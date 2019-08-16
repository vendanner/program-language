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
from lib import common,logger
from core import account

def init_evn():
    """
    初始化环境
    :return:
    """
    logger.init(logging.INFO)
    account.load_user_info()

def exit():
    """
    用户退出，做清理工作
    :return:
    """
    account.save_user_info()
    logger.print_logging(logging.INFO,'exit')
    logger.set_username(None)

def login_fun():
    """
    账号登陆过程
    :return: 登录状态,用户名:
                0 成功
                -1 账户异常
                1 登陆失败
    """
    for i in range(3):
        name = input('请输入账号：')
        if account.check_account_exists(name):
            logger.set_username(name)
            if account.get_account_status(name) == settings.NO_USE:
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
        if account.get_account_passwd(name) == passwd:
            print('登陆成功')
            logger.print_logging(logging.INFO, '登陆成功')
            return settings.SUCC_STR,name
        else:
            print('密码错误')
    else:
        account.set_account_status(name,settings.NO_USE)
        logger.print_logging(logging.INFO, '三次密码输入错误，账号锁定，无法使用...')
        print('三次密码输入错误，账号锁定，无法使用...')
        return -1,''

def register_fun():
    """
    账号注册过程
    :return: 登录状态:
                0 成功
                -1 账户异常
                1 登陆失败
    """
    # 输入账号
    for i in range(3):
        name = input('请输入账号:')
        logger.set_username(name)
        if name == settings.QUIT_STR:
            return settings.QUIT_STR
        elif account.check_account_exists(name):
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
            account.add_account(name=name,passwd=passwd_again,money=money,limit=money,status=0)

            print("注册成功")
            logger.print_logging(logging.INFO, '注册成功')
            return 0,name
        print('两次密码不一致...')
    else:
        logger.print_logging(logging.INFO, '注册失败')
        return 1,''

def get_money(name,buyed_item):
    """
    提现：只能提取可用额度的一半
    :param name:
    :param buyed_item:
    :return:str ;
            q:退出
            str(value):还款的金额
    """
    logger.print_logging(logging.INFO, '正在提现...')
    while True:
        print('您当前可取现金{} 元'.format(account.get_account_money(name)/2))
        value = input('请输入要取的现金：')
        try:
            if value == settings.QUIT_STR:
                logger.print_logging(logging.INFO, '退出提现')
                return settings.QUIT_STR
            value = float(value)
            if value > 0 and value < account.get_account_money(name)/2:
                account.set_account_money(name,account.get_account_money(name) - value)
                print('提现成功，当前可用额度{}元'.format(account.get_account_money(name)))
                logger.print_logging(logging.INFO, '成功提现 {}元'.format(value))
                return str(value)
        except ValueError as e:
            pass
        print('请输入正确的金额...')

def payment(name,buyed_item):
    """
    购物
    :param name:
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
            if money > account.get_account_money(name) :
                print('额度不足，请先提额或还款')
                continue
            account.set_account_money(name, account.get_account_money(name) - money)
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

def repayment(name,buyed_item):
    """
    还款
    :param name:
    :param buyed_item:
    :return:str ;
                q:退出
                str(value):还款的金额
    """
    logger.print_logging(logging.INFO, '正在还款...')
    while True:
        print('您当前还需还款{} 元'.format(account.get_account_limit(name) -
                                   account.get_account_money(name)))
        value = input('请输入要还款的金额：')
        try:
            if value == settings.QUIT_STR:
                logger.print_logging(logging.INFO, '退出还款')
                return settings.QUIT_STR
            value = float(value)
            if value > 0:
                account.set_account_money(name, account.get_account_money(name) +value)
                print( '成功还款{}元'.format(value))
                logger.print_logging(logging.INFO, '还款{}元'.format(value))
                return str(value)
        except ValueError as e:
            pass
        print('请输入正确的金额...')

def increased(name,buyed_item):
    """
    提额
    :param name:
    :param buyed_item:
    :return:str
                q:退出
                str(money_limit)：提额
    """
    logger.print_logging(logging.INFO, '正在提额...')
    while True:
        print('您当前额度为{} 元'.format(account.get_account_limit(name)))
        print('最高额度为{} 元'.format(settings.MAX_LIMIT))
        money_limit = input('输入你要提升的额度：')
        try:
            if money_limit == settings.QUIT_STR:
                logger.print_logging(logging.INFO, '退出提额...')
                return settings.QUIT_STR
            money_limit = float(money_limit)
            # 提额要小于最大值，且大于当前额度
            if not (money_limit > settings.MAX_LIMIT or money_limit < account.get_account_limit(name)):
                account.set_account_money(name,account.get_account_money(name) + money_limit -
                                          account.get_account_limit(name))
                account.set_account_limit(name, money_limit)
                print('提额成功，您当前总额度 {}元'.format(money_limit))
                logger.print_logging(logging.INFO, '提额至 {}元'.format(money_limit))
                return str(money_limit)
        except ValueError as e:
            pass
        print('请输入正确的额度...')

def goto_action(name,buyed_item):
    """
    登录成功后做提现，消费，还款，提额行为
    :param name:
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
        if action not in action_dict:
            continue

        action_dict[action](name, buyed_item)
        # if op_fun(name, account_list,buyed_item) == settings.QUIT_STR:
        #     return settings.QUIT_STR
