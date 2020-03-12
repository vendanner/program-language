#!/usr/bin/env python
# -*- coding:utf-8 -*-
"""
函数：
    多个返回值时用元组类型组装
    参数：
        默认形参应该是不可变类型：函数定义时参数有默认值 def fun(x,y=1)；下面有案例讲解
        可变参数(形参的角度)："*"：函数会将剩下的参数以元组的形式接收，"*"后的形参都必须以关键字参数传递 x = fun(z = 2)
                                "**"：函数会将剩下的参数以字典的形式接收，但必须保证关键字参数
        打散(实参的角度)："*"、"**"：实参传递带"*" 意味着打散传递;"**" 是字典，其他用"*"
"""

def register(name,action,l = []):
    """
    当默认形参是可变类型时，每次调用都相当于使用同一个变量，变量的改变会被累加
    默认形参是可变类型时，可以认为是定义在函数里的 static 变量(C/C++)
    :param name:
    :param action:
    :param l:
    :return:
    """
    l.append(action)
    print(name,l)

def foo(x,y,z,*args):
    """
    args 接收剩下所有参数
    :param x:
    :param y:
    :param z:
    :param args:
    :return:
    """
    print(x,y,z)
    print(args)

def fun_mutable(x,y,z,**args):
    """
    args 以字典形式接收所有参数，所以必须保证参数是以关键字的形式传入(a=2)
    :param x:
    :param y:
    :param z:
    :param args:
    :return:
    """
    print(x,y,z)
    print(args)

def fun_dis(x,y,z,t):
    print(x,y,z,t)

def test_register():
    """
    tom['read']
    john['read', 'play']
    li['read', 'play', 'run']
    :return:
    """
    register("tom",'read')
    register('john','play')
    register('li','run')

def test_mutable_args():
    """
    可变参数函数测试
    :return:
    """
    foo(1,2,3,4,5,6)
    fun_mutable(1,2,3,a=4,c=5,b=6,d=6,e=7,f=9)

def test_dis():
    """
    实参打散测试
    :return:
    """
    fun_dis(1,*[2,3,4])
    # 注意打散后的参数个数必须与形参个数相同
    # fun_dis(1, *[2, 3, 4,5])

if __name__ == "__main__":
    print("fun begin")
    # test_register()
    test_mutable_args()
    test_dis()