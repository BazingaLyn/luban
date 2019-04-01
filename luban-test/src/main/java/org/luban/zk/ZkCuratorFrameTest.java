package org.luban.zk;

import com.alibaba.fastjson.JSON;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent.Type.CHILD_ADDED;
import static org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent.Type.CHILD_REMOVED;
import static org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent.Type.CHILD_UPDATED;

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


    @Test
    public void test04() throws Exception {
//        curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/bazinga/smile");
//        curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/bazinga/smile/192.168.1.35/50");
//        curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/bazinga/smile/192.168.1.36/50");
//        curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/bazinga/smile/192.168.1.37/50");

        PathChildrenCache childrenCache = new PathChildrenCache(curatorFramework, "/bazinga/smile", true);
        childrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        childrenCache.getListenable().addListener(
                (client, event) -> {
                    switch (event.getType()) {
                        case CHILD_ADDED:
                            System.out.println("CHILD_ADDED: " + event.getData().getPath());
                            break;
                        case CHILD_REMOVED:
                            System.out.println("CHILD_REMOVED: " + event.getData().getPath());
                            break;
                        case CHILD_UPDATED:
                            System.out.println("CHILD_UPDATED: " + event.getData().getPath());
                            break;
                        default:
                            break;
                    }
                });

        Thread.sleep(Integer.MAX_VALUE);
    }


    @Test
    public void test05() throws Exception {
        curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/bazinga/smile/192.168.1.35/50");
        curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/bazinga/smile/192.168.1.36/50");
        curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/bazinga/smile/192.168.1.37/50");

        List<String> stringList = curatorFramework.getChildren().forPath("/bazinga/smile");
        System.out.println(JSON.toJSONString(stringList));

    }



}
