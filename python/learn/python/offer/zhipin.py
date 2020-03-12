#!/usr/bin/env python
# -*- coding:utf-8 -*-


from kcrawler import Boss
from bs4 import BeautifulSoup
import requests

boss = Boss()
hotcities = boss.hotcity()
'''
{'id': 101210100, 'name': '杭州'}
'''
result = boss.queryjob(query='数据开发', city=101210100)
print(result)
for content in result:
    print(content['id'])
print("dsd")

url = 'https://www.zhipin.com/job_detail/?query=python&city=101010100'
res = requests.get(url, headers=header).text
print(res)
