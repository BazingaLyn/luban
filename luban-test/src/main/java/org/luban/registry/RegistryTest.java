package org.luban.registry;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.luban.core.LubanRpcClient;
import org.luban.core.LubanRpcServer;

import java.util.List;

public class RegistryTest {


    @Test
    public void test01() throws InterruptedException {

        LubanRpcServer lubanRpcServer = new LubanRpcServer(10086,"192.168.1.58:2181");

        lubanRpcServer.publishService(new RegistryDemoSimpleService());

        System.out.println("registry success");

        Thread.sleep(Integer.MAX_VALUE);
    }

    @Test
    public void test03() throws InterruptedException {

        LubanRpcServer lubanRpcServer = new LubanRpcServer(10087,"192.168.1.58:2181");

        lubanRpcServer.publishService(new RegistryDemoSimpleService());

        System.out.println("registry success");

        Thread.sleep(Integer.MAX_VALUE);
    }


    @Test
    public void test02() throws Exception {

        LubanRpcClient lubanRpcClient = new LubanRpcClient("192.168.1.58:2181");
        RegistryService registryService = lubanRpcClient.getRegistryService();
        SubscribeMeta subscribeMeta = new SubscribeMeta();
        subscribeMeta.setGroup("test");
        subscribeMeta.setVersion("2.0.0");
        subscribeMeta.setServiceName("org.luban.registry.RegistryDemoService");
        registryService.subscribeService(subscribeMeta);

        List<SubscribeResult> subscribeResult = registryService.getSubscribeResult(subscribeMeta);

        System.out.println(JSON.toJSONString(subscribeResult));


        while(true){
            Thread.sleep(2000L);
            System.out.println(JSON.toJSONString(registryService.getSubscribeResult(subscribeMeta)));
        }


    }
}
