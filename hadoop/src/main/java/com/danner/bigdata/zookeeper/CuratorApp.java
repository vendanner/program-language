package com.danner.bigdata.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.List;

/**
 * curator 访问 zookeeper
 */
public class CuratorApp {

    private static final String ZOOKEEPER_URL = "192.168.22.147:2181";

    public static void main(String[] args) throws Exception {
        ExponentialBackoffRetry retry = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient(ZOOKEEPER_URL, retry );
        client.start();

        if(createNode(client,"/tmp/win","win")){
            System.out.println("create node succ");
        }

//        createNodeWithMode(client,"/tmp/emp/11/12","emp",CreateMode.PERSISTENT,true);
//        Thread.sleep(1000000);

//        createNodeInBackground(client,"/tmp/background","background");
//        Thread.sleep(1000000);

//        System.out.println("content ==> " + getNodeContent(client,"/tmp/background"));

//        setNodeContent(client,"/tmp/background","setdata",1);

//        checkNode(client,"/tmp/background1");

//        getChild(client,"/tmp/emp");
        deleteNode(client,"/tmp/emp");
        client.close();

    }

    /**
     * forPath 正确的返回值为 path
     * @param client
     * @param path
     * @return
     * @throws Exception
     */
    public static boolean createNode(CuratorFramework client,String path,String data) throws Exception {

        String result = client.create().forPath(path, data.getBytes());

        return result.equals(path);
    }

    /**
     * 增加 mode 控制
     * @param client
     * @param path
     * @param data
     * @param mode
     * @param recur 递归创建，若父类不存在
     * @throws Exception
     */
    public static void createNodeWithMode(CuratorFramework client, String path, String data,
                                          CreateMode mode,boolean recur) throws Exception {
        String result;
        if (recur){
            result = client.create().creatingParentsIfNeeded().forPath(path, data.getBytes());
        }else{
            result = client.create().withMode(mode).forPath(path, data.getBytes());
        }

        System.out.println("res = " + result);
    }

    /**
     * 异步创建节点
     * @param client
     * @param path
     * @param data
     * @return
     * @throws Exception
     */
    public static void createNodeInBackground(CuratorFramework client,String path,String data)
            throws Exception {
        // result 异步是返回 null
        String result = client.create().inBackground(new BackgroundCallback(){
            @Override
            public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
                System.out.println(event.getPath() + " " +event.getType().toString());
            }
        }).forPath(path, data.getBytes());

    }

    /**
     * 获取节点内容
     * @param client
     * @param path
     * @return
     * @throws Exception
     */
    public static String getNodeContent(CuratorFramework client,String path) throws Exception {

        byte[] bytes = client.getData().forPath(path);

        return new String(bytes);
    }

    /**
     * 更新 node 内容，默认 version 为-1，所有版本都可以更新
     * 如果 version 不设置为 -1，那么必须是当前的node 版本号，否则报 BadVersion
     * @param client
     * @param path
     * @param data
     * @param version
     * @throws Exception
     */
    public static void setNodeContent(CuratorFramework client,String path,String data,
                                      int version) throws Exception {

        /**
         * stat 是key 的值：35,45,1567433798372,1567434572029,2,0,0,0,7,0,35
         cZxid = 0x23
         mZxid = 0x2d
         ctime = Mon Sep 02 07:16:38 PDT 2019
         mtime = Mon Sep 02 07:29:32 PDT 2019
         dataVersion = 2
         cversion = 0
         aclVersion = 0
         ephemeralOwner = 0x0
         dataLength = 7
         numChildren = 0
         pZxid = 0x23
         */
        Stat stat = client.setData().withVersion(version).forPath(path, data.getBytes());

        System.out.println(stat.toString());
    }

    /**
     * 判断node 是否存在
     * 存在返回 stat 则 null
     * @param client
     * @param path
     * @throws Exception
     */
    public static boolean checkNode(CuratorFramework client,String path) throws Exception {

        Stat stat = client.checkExists().forPath(path);

        System.out.println(stat);
        return stat != null;
    }

    /**
     * 删除节点
     * 若当前节点下有子节点无法直接删除，需先删除子节点
     * @param client
     * @param path
     * @throws Exception
     */
    public static void deleteNode(CuratorFramework client,String path) throws Exception {

        List<String> strings = client.getChildren().forPath(path);
        for (String str:strings){
            deleteNode(client,path + "/" + str);
        }
        client.delete().forPath(path);
    }

    public static void getChild(CuratorFramework client,String path) throws Exception {
        List<String> strings = client.getChildren().forPath(path);

        for (String str:strings){
            System.out.println(str);
        }
    }
}
