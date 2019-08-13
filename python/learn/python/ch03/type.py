#!/usr/bin/env python
# -*- coding:utf-8 -*-
"""
数据类型：
    不可变类型：int,float,str,bool,tup,可以hash；value 一旦改变，id 也改变相当于开辟新空间
    可变类型：list,set,dict，不可以hash；id 不变的情况下，value 可以变
    可变不可变的区别在于运算后其id 会不会发生变化，显然容器类型不会变化
    copy：
        浅拷贝：只有对象内一个可变类型元素内的元素修改，才会同步
        深拷贝：递归拷贝，拷贝结束后，两者毫无相关
    容器：
        namedtuple('p',['x','y'])
        deque 列表
        ChainMap 链映射
        Counter：类似字典，但value 必须是int，但可运行加、减
"""

import copy
import collections


def fun_copy():
    """
    浅拷贝测试
    :return:
    """
    list1 = [1,2,[3,4],[5,[6,7]]]
    print('list1: ',id(list1))
    # list1 中四个元素都有自己的地址空间
    print([id(i) for i in list1])
    list2 = copy.copy(list1)
    print('list2: ',id(list2))
    print([id(i) for i in list2])
    list3 = list1
    print('list3: ',id(list3))
    print([id(i) for i in list3])
    print("**********************copy 只开辟当前变量的空间，没有开辟变量内的变量的空间*********************")


    list1[0] = -1
    # 此时，list1 有变化，但list2 没有变化
    print(list1)
    print(list2)
    # 只有对象内一个可变类型元素内的元素修改，才会同步，看下面
    list2[-1][0] = 9
    # 两个list 都变化了
    print(list1)
    print(list2)

    # 新增元素不会同步，因为两个list 是不同的地址空间
    list1.append([10])
    print(list1)
    print(list2)

def fun_dict():
    l1 = {
        'name':'alber',
        'age':18,
        'gender':'male'
    }
    # 取l1 的key，'I m Albert' 当成所有key 的value
    a = l1.fromkeys(l1,'I m Albert')
    print(a)
    # {'n': None, 'a': None, 'm': None, 'e': None}
    b = dict.fromkeys('name')
    print(b)

def fun_namedtuple():
    """
    命名元组
    :return:
    """
    # p 为名称，x,y 代表内容
    point = collections.namedtuple('p',['x','y'])
    xp = point(1,2)
    print(xp)
    print(xp.x)
    print(xp.y)

def fun_ChainMap():
    """
    两个字典通过chain 相连，组成一个大的字典
    :return:
    """
    dict1 = {'name':'al','age':48}
    dict2 = {'weight':65,'height':152}
    res = list(collections.ChainMap(dict1,dict2))
    print(collections.ChainMap(dict1,dict2))
    print(collections.ChainMap(dict1,dict2)['name'])
    print(res)

if __name__ =="__main__":
    print("begin")
    # fun_copy()
    # fun_dict()
    fun_namedtuple()
    fun_ChainMap()