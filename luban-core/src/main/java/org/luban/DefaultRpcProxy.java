package org.luban;

import org.luban.core.LubanRpcClient;
import org.luban.core.RpcInvoke;
import org.luban.core.RpcInvokeContext;

/**
 * @author liguolin
 * @param <I>
 */
public class DefaultRpcProxy<I> implements RpcProxy<I> {


    private Class<I> interfaceClass;

    private LubanRpcClient lubanRpcClient;

    private RpcInvokeContext rpcInvokeContext;


    public DefaultRpcProxy(Class<I> interfaceClass){
        this.interfaceClass = interfaceClass;
    }

    public static <I> DefaultRpcProxy<I> factory(Class<I> interfaceClass){
        DefaultRpcProxy<I> factory = new DefaultRpcProxy<>(interfaceClass);
        return factory;
    }

    public DefaultRpcProxy<I> rpcClient(LubanRpcClient lubanRpcClient){
        this.lubanRpcClient = lubanRpcClient;
        return this;
    }

    public DefaultRpcProxy<I> rpcInvokeContext(RpcInvokeContext rpcInvokeContext){
        this.rpcInvokeContext = rpcInvokeContext;
        return this;
    }

    @Override
    public Class<I> getDefaultClass() {
        return interfaceClass;
    }

    @Override
    public Object createHandler() {
        return new RpcInvoke(lubanRpcClient,rpcInvokeContext);
    }

}
