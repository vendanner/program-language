#!/usr/bin/env python
# -*- coding:utf-8 -*-

import copy
import random
import hashlib
import re

def fun_1():
    """

    :return:
    """
    content_arr = []
    with open('a','r',encoding='utf-8') as f:
        for line in f:
            words = line.strip().split(' ')
            if len(words) != 4:
                continue
            try:
                name, sex, age, salary = words[0], words[1], int(words[2]), int(words[3])
                content_arr.append(dict(zip(['name','sex','age','salary'],[name,sex,age,salary])))
            except ValueError as e:
                print(e)
                pass
    return content_arr

def get_top(content_arr):
    """
    获取薪资最高的人
    :param content_arr:
    :return:
    """
    index = -1
    max_salary = 0
    for ind,content in enumerate(content_arr):
        if content['salary'] > max_salary:
            index = ind
            max_salary = content['salary']
    print('max salary:')
    print(content_arr[index])

def get_young(content_arr):
    """

    :param content_arr:
    :return:
    """
    index = -1
    age = 200
    for ind,content in enumerate(content_arr):
        if content['age'] < age:
            index = ind
            age = content['age']
    print('young:')
    print(content_arr[index])

def get_name_upper(content_arrs):
    """
    名字首字母大写
    :return:
    """
    content_arr = copy.deepcopy(content_arrs)
    for ind, content in enumerate(content_arr):
        content_arr[ind]['name'] = content['name'][0].upper()+content['name'][1:]

    print('upper:')
    print(content_arr)

def del_name(content_arr):
    """

    :param content_arr:
    :return:
    """
    for ind, content in enumerate(content_arr):
        if content['name'].startswith('a'):
            content_arr.remove(content)
    print('del a:')
    print(content_arr)

def print_fib(n):
    """
    打印斐波那契数列
    :param n:
    :return:
    """
    def fib(num):
        if num == 0:
            return 0
        elif num == 1:
            return 1
        else:
            return fib(num - 1) + fib(num - 2)

    print('打印斐波那契数列:')
    for i in range(n):
        print(fib(i))

def get_code():
    """
    随机生成8 位验证码，包含数字和字母
    :return:
    """
    code_list  = []
    # 0-9
    code_list.extend([str(i) for i in range(10)])
    # A-Z
    code_list.extend([chr(i) for i in range(65,91)])
    code = ''.join(random.sample(code_list,8))
    print('code :')
    print(code)

def get_md5():
    """
    模拟撞库
    :return:
    """
    md5 = hashlib.md5()
    md5.update('318445'.encode('utf-8'))
    curr_md5 = md5.hexdigest()

    for i in range(9999999):
        num_md5 = hashlib.md5()
        num_md5.update(str(i).encode('utf-8'))
        if curr_md5 == num_md5.hexdigest():
            print('succ code = ',str(i))
            break
    else:
        print('fail')

def re_phone():
    """
    电话号码校验函数
    :return:
    """
    phone_list = ['11151054608','13813234424','22623423765','13324523342']

    for phone in phone_list:
        content = re.findall('1[3|4|5|7|8]\d{9}',phone)
        if content:
            print(content)

if __name__ == '__main__':
    print('begin')
    content_arr = fun_1()
    get_top(content_arr)
    get_young(content_arr)
    get_name_upper(content_arr)
    del_name(content_arr)
    print_fib(10)
    get_code()
    get_md5()
    re_phone()