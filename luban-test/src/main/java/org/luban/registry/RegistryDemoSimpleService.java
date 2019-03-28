package org.luban.registry;


import org.luban.annotation.RpcService;

@RpcService(version = "2.0.0",group = "test",weight = 90)
public class RegistryDemoSimpleService implements RegistryDemoService,RegistryParentDemoService {


    public String sum(int i, int j) {
        return ( i + j ) +"";
    }

    public String hello(String hello, String world) {
        return hello + world;
    }
}
