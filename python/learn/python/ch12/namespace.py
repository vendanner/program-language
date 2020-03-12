#!/usr/bin/env python
# -*- coding:utf-8 -*-

"""
名称空间
    类和对象都有自己的名称空间，如果在对象的名称空间找不到则会去类的名称空间找，还找不到则报错
    属性：
        公共属性是类的名称空间，所有对象共用
        私有属性是在对象的内存空间，每个对象都不同
    方法：
        方法在类中定义后，创建对象时，绑定到对象，即所有对象的方法也是不同
    类和类型是在 Python 是同一个概念
"""

class DeepshareStudent:
    """

    """

    school = 'deep'

    def __init__(self,name,age,gender):
        self.name = name
        self.age = age
        self.gender = gender

    def eat(self):
        print('eating')

    def sleep(self):
        print('sleeping')


def test_namespace(stu1,stu2):
    """
    每个对象的 __dict__ 内存空间都是独立的
    对象与类之间的内存空间也是独立的
    :param stu1:
    :param stu2:
    :return:
    """
    # 每个输出 __dict__ 都不同
    print(id(DeepshareStudent.__dict__))
    print(id(stu1.__dict__))
    print(id(stu2.__dict__))


def test_attr_and_fun(stu1,stu2):
    """
    方法是绑定到各自对象，id 都不同
    公共属性大家都相同
    :param stu1:
    :param stu2:
    :return:
    """
    print(DeepshareStudent.sleep)
    print(stu1.sleep)
    print(stu2.sleep)
    print(id(DeepshareStudent.school))
    print(id(stu1.school))
    print(id(stu2.school))

def test_type(stu1,stu2):
    """
    stu1 的类是 __main__.DeepshareStudent，说明是在 __main__ 下的 DeepshareStudent
    :param stu1:
    :param stu2:
    :return:
    """
    print(stu1)
    print(type(stu1))
    print(id(stu1))


if __name__ == '__main__':
    print('begin')
    stu1 = DeepshareStudent('li',25,'male')
    stu2 = DeepshareStudent('john',28,'female')
    test_namespace(stu1,stu2)
    print('--------------------------------')
    test_attr_and_fun(stu1, stu2)
    print('--------------------------------')
    test_type(stu1,stu2)