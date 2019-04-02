package org.luban.core;

import org.luban.rpc.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liguolin
 * 2019-04-02 10:49:17
 */
public class RpcInvokeFutures {

    private Logger logger = LoggerFactory.getLogger(RpcInvokeFutures.class);

    private Map<Long,RpcInvokeFuture> longRpcInvokeFutureMap = new ConcurrentHashMap<>();

    public void put(long requestId,RpcInvokeFuture rpcInvokeFuture){
        longRpcInvokeFutureMap.put(requestId,rpcInvokeFuture);
    }

    public void putResult(long requestId, RpcResponse rpcResponse){
        RpcInvokeFuture rpcInvokeFuture = longRpcInvokeFutureMap.remove(requestId);

        if(null == rpcInvokeFuture){
            logger.warn("requestId {} not found future",requestId);
            return;
        }
        rpcInvokeFuture.putRpcResponse(rpcResponse);
    }

    public void removeInvokeFuture(long requestId) {
        longRpcInvokeFutureMap.remove(requestId);
    }
}
