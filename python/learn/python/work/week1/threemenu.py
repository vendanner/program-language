#!/usr/bin/env python
# -*- coding:utf-8 -*-
"""
打印三级菜单
"""

dict_menu = {'汽车':{'轿车':{'宝马':{'宝马760':{},'宝马m':{}},'奔驰':{'奔驰18':{},'奔驰260':{}}},
                   '越野车':{'保时捷':{'保时捷':{}},'路虎':{}},'卡车':{},'公交车':{}},
             '飞机':{'大飞机':{'大1':{'大xxx':{}}},'小飞机':{'小1':{'小xxx':{}}},'直升机':{}},
             '大炮':{}}

if __name__ == "__main__":
    print('three menu begin:')
    index_menu = '1'
    first_menu =[]
    second_menu = {}
    third_menu = {}
    fourth_menu = {}
    index = 0
    # 解析菜单字典
    for menu in dict_menu:
        first_menu.append(menu)
        second_menu[index] = list(dict_menu[menu].keys())
        index1 = len(third_menu)
        for menu1 in dict_menu[menu]:
            third_menu[index1] = list(dict_menu[menu][menu1].keys())
            index2 = len(fourth_menu)
            for menu2 in dict_menu[menu][menu1]:
                fourth_menu[index2] = list(dict_menu[menu][menu1][menu2].keys())
                index2 += 1
            index1 += 1
        index += 1

    while True:
        index1 = 0
        if 1 == len(index_menu):
            list_menu = {0:first_menu}
        elif len(index_menu) == 2:
            index1 = int(index_menu[-1])
            list_menu = second_menu
        elif len(index_menu) == 3:
            index1 = int(index_menu[-1])
            list_menu = third_menu
        elif len(index_menu) == 4:
            index1 = int(index_menu[-1])
            list_menu = fourth_menu
        else:
            list_menu = {}

        # 输出菜单
        print("菜单：")
        if list_menu:
            for menu in list_menu[index1]:
                print(menu)

        action = input("选择菜单：")
        if 'b'== action:
            # 返回
            index_menu = index_menu[:len(index_menu) - 1]
            if not index_menu:
                index_menu = '1'
        elif 'q' == action:
            break
        elif action in list_menu[index1]:
            list_all = []
            # print(list_menu)
            for key,value in list_menu.items():
                if value:
                    list_all.extend(value)
            ind = list_all.index(action)
            # print(ind)
            index_menu += str(ind)
        else:
            print('操作错误，重新操作')