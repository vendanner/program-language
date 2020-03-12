#!/usr/bin/env python
# -*- coding:utf-8 -*-

import random
import time


domain_name_list = ["www.baidu.com","www.google.com","www.360.com",
                    "www.qq.com","www.taobao.com","www.jd.com"]
ip_dict = {
    "www.baidu.com":"121.22.15.15",
    "www.google.com":"121.22.15.16",
    "www.360.com":"121.22.15.17",
    "www.qq.com":"121.22.15.18",
    "www.taobao.com":"121.22.15.19",
    "www.jd.com":"121.22.15.20",
}
num_list = [0,1,2,3,4,5,6,7,8,9,'q']

def get_domain_name():
    """

    :return: domain_name
    """
    return random.sample(domain_name_list,1)[0]

def get_ip(domain_name):
    """

    :return:
    """
    return ip_dict.get(domain_name,"0.0.0.0")

def get_time():
    """

    :return: str
    """
    return time.strftime("%m/%d/%Y %H:%M:%S %z")

def get_flow():
    """

    :return:
    """
    return "".join(str(val) for val in random.sample(num_list, 4))

def genate():
    """
    随机生成日志
    :return:
    """
    time_str = get_time()
    domain_name = get_domain_name()
    ip = get_ip(domain_name)
    flow = get_flow()

    return "{}\t{}\t{}\t{}".format(domain_name, time_str,flow,ip)


if __name__ == "__main__":
    with open("danner.log",'w',encoding ='utf-8') as f:
        for i in range(10000):
            content = genate()
            print(content)
            f.write(content+"\n")
            time.sleep(0.11)