package org.luban.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import org.luban.registry.ServiceMeta;
import org.luban.rpc.RpcRequest;
import org.luban.rpc.RpcResponse;
import org.luban.utils.CommonIdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

import static org.luban.constant.LubanCommon.DEFAULT_GROUP;
import static org.luban.constant.LubanCommon.declareMethodName;

/**
 * @author liguolin
 * 2019-04-01 09:50:43
 */
public class RpcInvoke {

    private Logger logger = LoggerFactory.getLogger(RpcInvoke.class);

    private LubanRpcClient lubanRpcClient;

    private RpcInvokeContext rpcInvokeContext;


    public RpcInvoke(LubanRpcClient lubanRpcClient, RpcInvokeContext rpcInvokeContext){
        this.lubanRpcClient = lubanRpcClient;
        this.rpcInvokeContext = rpcInvokeContext;
    }

    @RuntimeType
    public Object invoke(@Origin Method method, @AllArguments @RuntimeType Object[] args) throws Throwable {

        logger.info("rpc invoke method name is {} args is {}",method.getName(), JSON.toJSON(args));

        String serviceName = method.getDeclaringClass().getName();
        String methodName = method.getName();
        ServiceMeta serviceMeta = getCurrentMethodMeta(serviceName,methodName);

        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setRequestId(CommonIdGenerator.nextId());
        rpcRequest.setMethodName(methodName);
        rpcRequest.setGroup(serviceMeta.getGroup());
        rpcRequest.setVersion(serviceMeta.getVersion());
        rpcRequest.setServiceName(serviceName);
        rpcRequest.setParameterClass(method.getParameterTypes());
        rpcRequest.setParameters(args);

        RpcResponse rpcResponse = lubanRpcClient.entranceInvoke(rpcRequest);

        if(rpcResponse == null){
            return null;
        }

        if(rpcResponse.getThrowable() != null){
            throw rpcResponse.getThrowable();
        }

        JSONObject jsonObject = (JSONObject)rpcResponse.getResult();

        return rpcResponse.getResult();
    }

    private ServiceMeta getCurrentMethodMeta(String serviceName, String methodName) {
        ServiceMeta serviceMeta = rpcInvokeContext.getServiceMethodNameMeta(declareMethodName(serviceName, methodName));
        if(null == serviceMeta){
            serviceMeta = rpcInvokeContext.getServiceMeta(serviceName);
        }
        return serviceMeta;
    }
}
