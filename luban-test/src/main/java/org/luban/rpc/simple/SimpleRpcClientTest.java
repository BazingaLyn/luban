package org.luban.rpc.simple;

import org.luban.DefaultRpcProxy;
import org.luban.core.LubanRpcClient;
import org.luban.core.RpcInvokeContext;
import org.luban.rpc.DemoResult;
import org.luban.rpc.DemoService;

public class SimpleRpcClientTest {

    public static void main(String[] args) {
        LubanRpcClient lubanRpcClient = new LubanRpcClient("192.168.1.58:2181");
        RpcInvokeContext rpcInvokeContext = new RpcInvokeContext();
        DemoService demoService = DefaultRpcProxy.factory(DemoService.class).rpcClient(lubanRpcClient).rpcInvokeContext(rpcInvokeContext).newInstance();
        DemoResult demoResult = demoService.hello("hello", 21);
        System.out.println(demoResult);
    }
}
