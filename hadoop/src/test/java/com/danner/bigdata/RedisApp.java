package com.danner.bigdata;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;

public class RedisApp {

    private static String HOST = "192.168.22.147";
    private static int PORT = 6379;
    Jedis jedis = null;

    @Before
    public void setUp(){
        jedis = new Jedis(HOST,PORT);
    }
    @After
    public void tearDown(){
        if (jedis != null){
            jedis.close();
        }
    }

    @Test
    public void get(){
        System.out.println(jedis.lrange("listkey",0,-1));
    }
}
