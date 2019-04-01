package org.luban.core;

import org.luban.Endpoint;
import org.luban.registry.RegistryService;
import org.luban.registry.SubscribeMeta;
import org.luban.registry.zookeeper.ZookeeperRegistryService;
import org.luban.rpc.RpcRequest;
import org.luban.transports.LubanRpcNettyClient;


/**
 * @author liguolin
 * 2019-03-29 10:27:44
 */
public class LubanRpcClient implements Endpoint {


    private LubanRpcNettyClient lubanRpcNettyClient;

    private RegistryService registryService;

    private String registryAddress;

    public LubanRpcClient(String registryAddress){
        this.registryAddress = registryAddress;
        init();
    }


    public void init() {
        lubanRpcNettyClient = new LubanRpcNettyClient();
        this.registryService = new ZookeeperRegistryService(registryAddress);
    }

    public void start() {

    }

    public Object entranceInvoke(RpcRequest rpcRequest) throws Exception {

        SubscribeMeta subscribeMeta = buildSubscribeMeta(rpcRequest);

        registryService.subscribeService(subscribeMeta);

        registryService.getSubscribeResult(subscribeMeta);

        return null;

    }

    public Object invokeSync(String host,int port,RpcRequest request,int timeoutMillis){
        return lubanRpcNettyClient.invokeSync(host,port,request,timeoutMillis);
    }


    public void shutdown() {
        lubanRpcNettyClient.shutdown();
    }



    private SubscribeMeta buildSubscribeMeta(RpcRequest rpcRequest) {
        SubscribeMeta subscribeMeta = new SubscribeMeta();
        subscribeMeta.setGroup(rpcRequest.getGroup());
        subscribeMeta.setServiceName(rpcRequest.getServiceName());
        subscribeMeta.setVersion(rpcRequest.getVersion());
        return subscribeMeta;
    }

    public RegistryService getRegistryService() {
        return registryService;
    }

}
