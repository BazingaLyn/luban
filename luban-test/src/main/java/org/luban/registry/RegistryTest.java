package org.luban.registry;

import org.junit.Test;
import org.luban.core.LubanRpcServer;

public class RegistryTest {


    @Test
    public void test01() throws InterruptedException {

        LubanRpcServer lubanRpcServer = new LubanRpcServer(10086,"192.168.1.58:2181");

        lubanRpcServer.publishService(new RegistryDemoSimpleService());

        System.out.println("registry success");

        Thread.sleep(Integer.MAX_VALUE);
    }


    @Test
    public void test02() throws InterruptedException {

        LubanRpcServer lubanRpcServer = new LubanRpcServer(10087,"192.168.1.58:2181");

        lubanRpcServer.publishService(new RegistryDemoSimpleService());

        System.out.println("registry success");

        Thread.sleep(Integer.MAX_VALUE);
    }
}
