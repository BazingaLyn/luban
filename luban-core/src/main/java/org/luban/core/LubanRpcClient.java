package org.luban.core;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import org.luban.Endpoint;
import org.luban.exception.NoFoundRpcServiceProviderException;
import org.luban.exception.RpcInvokeTimeoutException;
import org.luban.loadbalance.DefaultLoadBalance;
import org.luban.loadbalance.LoadBalance;
import org.luban.registry.RegistryService;
import org.luban.registry.SubscribeMeta;
import org.luban.registry.SubscribeResult;
import org.luban.registry.zookeeper.ZookeeperRegistryService;
import org.luban.rpc.RpcRequest;
import org.luban.rpc.RpcResponse;
import org.luban.transports.LubanRpcNettyClient;
import org.luban.transports.config.DefaultNettyTransportConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.luban.constant.LubanCommon.DEFAULT_INVOKE_TIME_OUT;


/**
 * @author liguolin
 * 2019-03-29 10:27:44
 */
public class LubanRpcClient implements Endpoint {


    private Logger logger = LoggerFactory.getLogger(LubanRpcClient.class);

    private LubanRpcNettyClient lubanRpcNettyClient;

    private RegistryService registryService;

    private String registryAddress;

    private long timeoutMillis = DEFAULT_INVOKE_TIME_OUT;

    public LubanRpcClient(String registryAddress){
        this.registryAddress = registryAddress;
        init();
    }


    public void init() {
        lubanRpcNettyClient = new LubanRpcNettyClient(new DefaultNettyTransportConfig());
        this.registryService = new ZookeeperRegistryService(registryAddress);
    }

    public void start() {

    }

    public RpcResponse entranceInvoke(RpcRequest rpcRequest) throws Exception {

        SubscribeMeta subscribeMeta = buildSubscribeMeta(rpcRequest);

        registryService.subscribeService(subscribeMeta);

        List<SubscribeResult> subscribeResult = registryService.getSubscribeResult(subscribeMeta);
        if(subscribeResult == null || subscribeResult.size() == 0){
            throw new NoFoundRpcServiceProviderException();
        }

        List<SubscribeResult.ServiceEndpoint> serviceEndpoints = subscribeResult.stream().map(eachSubscribeResult -> eachSubscribeResult.getServiceEndpoint()).collect(toList());

        LoadBalance loadBalance = new DefaultLoadBalance();

        String url = loadBalance.select(serviceEndpoints);

        RpcInvokeFuture rpcInvokeFuture = new RpcInvokeFuture(rpcRequest.getRequestId());
        lubanRpcNettyClient.putCurrentInvokeFuture(rpcInvokeFuture.getRequestId(),rpcInvokeFuture);

        Channel availableChannel = lubanRpcNettyClient.getAvailableChannel(url);
        availableChannel.writeAndFlush(rpcRequest).addListener((ChannelFutureListener) channelFuture -> {
            if(!channelFuture.isSuccess()){
                logger.error("requestId {} send failed",rpcInvokeFuture.getRequestId(),channelFuture.cause());
                lubanRpcNettyClient.removeCurrentInvokeFuture(rpcInvokeFuture.getRequestId());
                rpcInvokeFuture.putRpcResponse(RpcResponse.errorResponse(rpcInvokeFuture.getRequestId(),channelFuture.cause()));
            }
        });

        RpcResponse rpcResponse = rpcInvokeFuture.waitResponse(timeoutMillis);
        if(null == rpcResponse){
            logger.warn("requestId {} request invoke timeout",rpcInvokeFuture.getRequestId());
            lubanRpcNettyClient.removeCurrentInvokeFuture(rpcInvokeFuture.getRequestId());
            rpcResponse = RpcResponse.errorResponse(rpcInvokeFuture.getRequestId(),new RpcInvokeTimeoutException());
        }

        return rpcResponse;
    }

    public void setTimeoutMillis(long timeoutMillis) {
        this.timeoutMillis = timeoutMillis;
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
