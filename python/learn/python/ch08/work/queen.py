#!/usr/bin/env python
# -*- coding:utf-8 -*-
"""
8 皇后问题：
    在 8*8 的棋局上，放置8个皇后，但必须保证皇后之间不在同一行，同一列，同一斜线上
    1、初始化
    2、递归摆放皇后
    3、最后输出结果
"""

import numpy as np

MAX_INDEX = 8

def init():
    """
    初始化棋局，刚开始都为0，之后有放皇后的位置放1
    :return:
    """
    return np.zeros((8,8))

def check_board(board,x,y):
    """
    皇后落地是否正确检查
    落点(x,y) 后，其行、列、斜线都不能有皇后
    :param board:
    :param x:
    :param y:
    :return:
    """
    if board[x][y] != 0:
        # 回溯点不能放
        return False
    if board[x,:].sum() > 0:
        # 当前行不能有皇后
        return False
    if board[:,y].sum() > 0:
        # 当前列不能有皇后
        return False
    x_start = max(x-y,0)
    y_start = max(y-x,0)
    while x_start < MAX_INDEX and y_start < MAX_INDEX:
        if (board[x_start][y_start] !=0):
            return False
        x_start += 1
        y_start += 1

    x_start = min(x+y,7)
    y_start = max(x+y+1-MAX_INDEX,0)
    while x_start > -1 and y_start < MAX_INDEX:
        if board[x_start][y_start] != 0:
            return False
        x_start -= 1
        y_start += 1

    return True

def sett(board,i):
    """
    递归是每次操作都一样，这里使用列循环，因为每次放置皇后，行号是不同的
    :param board:
    :param i:第几列，
    :return:
    """
    if i == MAX_INDEX:
        return True

    # 这是 列循环，很巧妙的做法
    # column_index 移动包含回溯思想
    for column_index in range(MAX_INDEX):
        board[i] = 0
        if check_board(board,i,column_index):
            board[i][column_index] = 1
            if sett(board,i+1):
                return True
    return False

if __name__ == '__main__':
    print('begin')
    board = init()
    sett(board,0)
    print(board)