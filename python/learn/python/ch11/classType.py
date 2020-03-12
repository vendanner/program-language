#!/usr/bin/env python
# -*- coding:utf-8 -*-

"""
面向过程：
    优点：
        复杂度的问题流程化，进而简单化(复杂问题分解成一个个小问题去解决)
    缺点：
        一套流水线只能解决一个问题，改一个组件，牵一发而动全身
    一旦完成基本很少修改的场景
面向对象：
    优点：
        解决程序的扩展性。对某个对象单独修改不会涉及到其他对象
    缺点：
        编程复杂度远高于面向过程
        无法向面向过程的程序设计流水线式的可以很准确的预测问题的处理流程与结果
    需求经常变化的软件

"""

class DeepshareStudent:
    """
    学生类
    """
    # 公共属性
    school = 'deepshare'

    def __init__(self,name,age,gender):
        print('DeepshareStudent init...')
        # 私有属性
        self.name = name
        self.age = age
        self.gender = gender
    def learn(self):
        """
        learning
        :return:
        """
        print('is learning')

    def eat(self):
        """
        eating
        :return:
        """
        print('is eating')

    def sleep(self):
        """
        sleeping
        :return:
        """
        print('is sleeping')

    # 类加载的时候执行，比 __init__ 更早
    print('------------------------------------------')


def watch_class_attrs(class_name):
    """
    查看类的属性和函数,__dict__
    {
        '__module__': '__main__',
        '__doc__': '\n    学生类\n    ',
        'school': 'deepshare',
        'learn': <function DeepshareStudent.learn at 0x00000290B7C28730>,
        'eat': <function DeepshareStudent.eat at 0x00000290B7C287B8>,
        'sleep': <function DeepshareStudent.sleep at 0x00000290B7C28840>,
        '__dict__': <attribute '__dict__' of 'DeepshareStudent' objects>,
        '__weakref__': <attribute '__weakref__' of 'DeepshareStudent' objects>
    }
    :param class_name:
    :return:
    """
    print(class_name.__dict__)
    print(class_name.school)

if __name__ == '__main__':
    print('begin')
    watch_class_attrs(DeepshareStudent)
    student = DeepshareStudent('jj',23,'male')
    student.eat()
    student.name = 'll'
    # 下面这行代码不会修改 student.name 值，因为name 是私有属性，必须对象对象修改
    DeepshareStudent.name = '12'
    print(student.name)
