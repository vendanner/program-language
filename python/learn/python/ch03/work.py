#!/usr/bin/env python
# -*- coding:utf-8 -*-

def fun1():
    """
    str 操作
    :return:
    """
    name = ' alberT'
    print(name.strip())
    print(name.startswith('al'))
    print(name.endswith('T'))
    print(name.replace('l','p'))
    print(name.split('l'))
    print(name.upper())
    print(name.lower())
    print(name[1:2])
    print(name[:3])
    print(name[-2:])
    print(name.index('e'))

def fun2():
    """
    list 操作
    :return:
    """
    data =['albert',18,[2000,1,1]]
    name = data[0]
    age = data[1]
    year = data[2][0]
    month = data[2][1]
    day = data[2][2]
    print(name,age,year,month,day)

def fun3():
    """
    dict operation
    :return:
    """
    data = [11,22,33,44,55,66,77,88,99,90]
    dict1 = {'k1':[],'k2':[]}

    for i in data:
        if i > 66:
            dict1['k1'].append(i)
        elif i< 66:
            dict1['k2'].append(i)

    print(dict1)

def fun4():
    """
    list and dict op
    :return:
    """
    # 去重后输出
    l = ['a','b',1,'a','a']
    l =list(set(l))
    print(l)

    L = [{'name':'al','age':18,'sex':'male'},
         {'name': 'aal', 'age': 11, 'sex': 'male'},
         {'name': 'al', 'age': 18, 'sex': 'male'}]

    hash_list = []
    for l in L:
        hash_item = ''
        for key,value in l.items():
            hash_item += str(hash(key))+str(hash(value))
        if hash_item in hash_list:
            L.remove(l)
        else:
            hash_list.append(hash_item)
    # 去重输出，维持原先顺序不变
    print(L)

def count_word():
    """
    统计单词个数
    :return:
    """
    s = 'hello albert albert say hello world world'
    list_word = s.split(' ')
    dict1 = {}
    for word in list_word:
        if word in dict1:
            dict1[word] += 1
        else:
            dict1[word] = 1
    print(dict1)

    dict2 = {}
    for word in set(list_word):
        dict2[word] = s.count(word)
    print(dict2)

def shop_show():
    """

    :return:
    """
    dict_item = {'apple':10,'mac':10000,'iphone':8000,'lenovo':30000,'chicken':10}
    dict_car = {}
    while True:
        print('可选商品：')
        for item,value in dict_item.items():
            print(item + '\t'+str(value))

        item_name = input('输入要购买商品:')
        if (not item_name) or (item_name not in dict_item):
            print('商品不存在，重新输入')
            continue
        while True:
            try:
                count = int(input('输入要购买个数:'))
                if count > 0 :
                    break
            except ValueError as e:
                pass
            print('输入错误，请输入数字')

        if item_name in dict_car:
            dict_car[item_name][1] += count
        else:
            dict_car[item_name] = [dict_item[item_name],count]

        print('当前购物车商品:')
        print(dict_car)

if __name__ == '__main__':
    fun1()
    fun2()
    fun3()
    fun4()
    count_word()
    shop_show()
