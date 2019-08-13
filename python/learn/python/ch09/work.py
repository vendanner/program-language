#!/usr/bin/env python
# -*- coding:utf-8 -*-

def upper_fun():
    """
    全部变大写
    :return:
    """
    name = ['albert','james','kobe','hd']
    print([word.upper() for word in name])

def del_fun(word):
    """
    删除包含word 的name
    :return:
    """
    names = ['albert', 'jr_shenjing', 'kobe', 'hd']
    print([name for name in names if not name.endswith(word)])

def get_max_line():
    """

    :return:
    """
    with open('a.txt','r',encoding='utf-8') as f:
        print(max([len(line.strip()) for line in f]))

def print_shopping():
    """

    :return:
    """
    sum = 0
    item = []
    item_max = []
    with open('shopping.txt','r') as f:
        for line in f:
            name,price,count = line.strip().split(',')
            sum +=  int(price)*int(count)
            item.append(dict(zip(['name','price','count'],[name,int(price),int(count)])))
            if int(price) > 10000:
                item_max.append(dict(zip(['name','price','count'],[name,int(price),int(count)])))
    print(sum)
    print(item)
    print(item_max)

if __name__ == '__main__':
    print('begin')
    upper_fun()
    del_fun('shenjing')
    get_max_line()
    print_shopping()