package org.luban.zk;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.junit.Before;
import org.junit.Test;

public class ZkCuratorFrameTest {


    private CuratorFramework curatorFramework;



    @Before
    public void ready(){
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 5);

        curatorFramework = CuratorFrameworkFactory.builder()
                .connectString("192.168.1.58:2181").sessionTimeoutMs(5000)
                .retryPolicy(retryPolicy)
                .namespace("luban")
                .build();

        curatorFramework.start();




    }


    @Test
    public void test01() throws Exception {
        String s = curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/aegis/hello-world");
        System.out.println(s);
    }



    @Test
    public void test02() throws Exception {
        curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/aegis/smile-hello","hello-bazinga".getBytes());
    }


    @Test
    public void test03() throws Exception {
        byte[] bytes = curatorFramework.getData().forPath("/smile-hello");
        System.out.println(new String(bytes));
    }


}
