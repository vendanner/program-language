package com.danner.bigdata.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class ObserApp {
    private static final String ZOOKEEPER_URL = "192.168.22.147:2181";

    public static void main(String[] args) throws Exception {
        ExponentialBackoffRetry retry = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient(ZOOKEEPER_URL, retry);
        client.start();

        // 连接 监听事件
        client.getConnectionStateListenable().addListener(new ConnectionStateListener() {
            @Override
            public void stateChanged(CuratorFramework client, ConnectionState newState) {
                System.out.println("Listener ==> " + newState.name());
            }
        });

        // 路径监听：子节点新增、删除、更新
        PathChildrenCache cache = new PathChildrenCache(client, "/tmp/win", true);
        cache.start(true);
        cache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework,
                                   PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                System.out.println("child state ==> "+ pathChildrenCacheEvent.getData().getPath() + " "+
                        pathChildrenCacheEvent.getType());
            }
        });

        Thread.sleep(10000000);

    }
}
