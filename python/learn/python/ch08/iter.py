#!/usr/bin/env python
# -*- coding:utf-8 -*-
"""
迭代器：
    可迭代对象：内置有 __iter__ 方法的对象
        str、list、tup、dict、set、file
    迭代器对象：内置 __next__ 方法对象 ==> 可迭代对象执行 __iter__ 返回 迭代器对象
        缺点：
            只能从前往后取，并且是一次性的
            无法使用 len() 获取长度
    for ...in ...:
        原理：实质是在 in 后直接获取对象的 __iter__ 得到一个迭代器对象，然后 一次循环，一个 __next__
    生成器：生成器本质是迭代器，是我们自己制造的迭代器
        yield：可以把函数暂停住; 和 return 一样，yield 后面跟着的值返回


"""

def test_yield():
    """
    含有 yield 调用函数不会执行，只返回 迭代器对象
    :return:
    """
    print('=========> first')
    yield 1
    print('=========> second')
    yield 2
    print('=========> third')
    yield 3

def eat(name):
    """

    :param name:
    :return:
    """
    print('{} ready eat'.format(name))
    food_list = []

    while True:
        food = yield food_list
        print('{} eat {}'.format(name,food))
        food_list.append(food)
        print('{} eat list {}'.format(name, food_list))

if __name__ == '__main__':
    print('begin')
    res = test_yield()      # 此时不执行函数代码，返回生成器对象
    print(res)
    print(res.__iter__() is res)
    print(res.__next__())
    print(res.__next__())
    print(res.__next__())

    person = eat('ll')
    # send 可以给 yield 赋值，并且执行 __next__ 操作
    # 第一次必须是 None
    person.send(None)
    # person.__next__()
    person.send('as')
    person.send('d')
    person.send('gfd')
    person.send('gd')
    person.close()