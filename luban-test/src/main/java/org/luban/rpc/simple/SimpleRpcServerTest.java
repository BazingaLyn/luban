package org.luban.rpc.simple;

import org.luban.core.LubanRpcServer;
import org.luban.rpc.DemoServiceImpl;

public class SimpleRpcServerTest {

    public static void main(String[] args) {
        LubanRpcServer lubanRpcServer = new LubanRpcServer(10086,"192.168.1.58:2181");
        lubanRpcServer.publishService(new DemoServiceImpl());
        lubanRpcServer.start();
    }
}
