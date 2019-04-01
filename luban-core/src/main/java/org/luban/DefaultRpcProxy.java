package org.luban;

import org.luban.core.LubanRpcClient;
import org.luban.core.RpcInvoke;
import org.luban.transports.LubanRpcNettyClient;

/**
 * @author liguolin
 * @param <I>
 */
public class DefaultRpcProxy<I> implements RpcProxy<I> {



    private Class<I> interfaceClass;

    private LubanRpcClient lubanRpcClient;

    public DefaultRpcProxy(Class<I> interfaceClass){
        this.interfaceClass = interfaceClass;
    }

    public static <I> DefaultRpcProxy<I> factory(Class<I> interfaceClass){
        DefaultRpcProxy<I> factory = new DefaultRpcProxy<>(interfaceClass);
        return factory;
    }

    public DefaultRpcProxy rpcClient(LubanRpcClient lubanRpcClient){
        this.lubanRpcClient = lubanRpcClient;
        return this;
    }


    @Override
    public Class<I> getDefaultClass() {
        return interfaceClass;
    }

    @Override
    public Object createHandler() {
        return new RpcInvoke(lubanRpcClient);
    }


//    private static class DefaultRpcProxyInstance {
//        private static final DefaultRpcProxy instance = new DefaultRpcProxy();
//    }

//    public static DefaultRpcProxy getInstance(){
//        return DefaultRpcProxyInstance.instance;
//    }

}
