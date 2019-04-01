package org.luban.core;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import org.luban.rpc.RpcRequest;
import org.luban.utils.CommonIdGenerator;

import java.lang.reflect.Method;

import static org.luban.constant.LubanCommon.DEFAULT_GROUP;

/**
 * @author liguolin
 * 2019-04-01 09:50:43
 */
public class RpcInvoke {

    private LubanRpcClient lubanRpcClient;


    public RpcInvoke(LubanRpcClient lubanRpcClient){
        this.lubanRpcClient = lubanRpcClient;
    }

    @RuntimeType
    public Object invoke(@Origin Method method, @Origin Class clazz, @AllArguments @RuntimeType Object[] args) throws Throwable {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setRequestId(CommonIdGenerator.nextId());
        rpcRequest.setMethodName(method.getName());
        rpcRequest.setGroup(DEFAULT_GROUP);
        rpcRequest.setServiceName(clazz.getName());
        rpcRequest.setParameterClass(method.getParameterTypes());
        rpcRequest.setParameters(args);

        return lubanRpcClient.entranceInvoke(rpcRequest);
    }
}
