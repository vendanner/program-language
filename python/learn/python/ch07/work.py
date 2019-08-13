#!/usr/bin/env python
# -*- coding:utf-8 -*-
"""
装饰器实现登陆验证和执行日志
"""
import functools
import time

path_log = 'log'
current_user = {
    'username':None,
    'session_time':10,  # session 有效时间20 s
    'session_begin':0
}

with open('account','r',encoding='utf-8') as f:
    account = eval(f.read().strip())

def auth(func):
    @functools.wraps(func)
    def wrapper(*args,**kwargs):
        if current_user['username'] and (int(time.time()) - current_user['session_begin']) < current_user['session_time']:
            res = func(*args,**kwargs)
            return res
        else:
            name = input('请输入用户名:')
            passwd = input('请输入密码:')
            if name == account['name'] and passwd == account['passwd']:
                print('登录成功')
                current_user['username'] = name
                current_user['session_begin'] = int(time.time())
                res = func(*args,**kwargs)
                return res
            else:
                print('用户名或密码输入错误')
    return wrapper

def logger(func):
    @functools.wraps(func)
    def wrapper(*args,**kwargs):
        with open(path_log,'a+',encoding='utf-8') as f:
            f.write(time.strftime('%Y-%m-%d %H:%M:%S',time.localtime())+'\t'+
                    func.__name__  +' function executing...\n')
        res = func(* args, * kwargs)
        return res
    return wrapper

@logger
@auth
def shopping():
    time.sleep(5)
    print('shopping...')

@logger
@auth
def buy():
    time.sleep(10)
    print('buy...')

@logger
def test():
    print('testing ...')
if __name__ == '__main__':
    print('begin')
    shopping()
    buy()
    shopping()
    test()
