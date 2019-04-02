package org.luban.core;

import org.luban.rpc.RpcResponse;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


/**
 * @author liguolin
 * 2019-04-02 09:45:55
 */
public class RpcInvokeFuture {

    private long requestId;

    private final CountDownLatch countDownLatch = new CountDownLatch(1);

    private volatile RpcResponse rpcResponse;

    public RpcInvokeFuture(long requestId) {
        this.requestId = requestId;
    }

    public RpcResponse waitResponse(long timeoutMillis) throws InterruptedException {
        countDownLatch.await(timeoutMillis, TimeUnit.MILLISECONDS);
        return rpcResponse;
    }

    public RpcResponse waitResponse() throws InterruptedException {
        countDownLatch.await();
        return rpcResponse;
    }

    public void putRpcResponse(RpcResponse rpcResponse){
        this.rpcResponse = rpcResponse;
        this.countDownLatch.countDown();
    }

    public long getRequestId() {
        return requestId;
    }
}
