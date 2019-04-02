package org.luban.transports.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.luban.core.RpcProviderService;
import org.luban.exception.ProviderNotFoundInstanceException;
import org.luban.rpc.RpcRequest;
import org.luban.rpc.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

import static org.luban.constant.LubanCommon.completeServiceName;

/**
 * @author liguolin
 * @create 2018-11-21 19:29
 **/
public class RpcServerHandler extends SimpleChannelInboundHandler<RpcRequest> {


    private Logger logger = LoggerFactory.getLogger(RpcServerHandler.class);


    private RpcProviderService rpcProviderService;

    public RpcServerHandler(RpcProviderService rpcProviderService) {
        this.rpcProviderService = rpcProviderService;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcRequest rpcRequest) throws Exception {

        RpcResponse rpcResponse = new RpcResponse();
        Channel channel = channelHandlerContext.channel();

        long id = rpcRequest.getRequestId();
        try{
            rpcResponse.setId(id);
            String group = rpcRequest.getGroup();
            String serviceName = rpcRequest.getServiceName();
            String version = rpcRequest.getVersion();
            String methodName = rpcRequest.getMethodName();
            Class<?>[] parameterClass = rpcRequest.getParameterClass();
            Object[] parameters = rpcRequest.getParameters();
            String completeServiceName = completeServiceName(group, serviceName, version);

            Object publishObject = rpcProviderService.getPublishObject(completeServiceName);
            if(null == publishObject){
                logger.error("completeServiceName {} not found instance",completeServiceName);
                rpcResponse.setThrowable(new ProviderNotFoundInstanceException());
            }else{
                Class<?> forName = Class.forName(serviceName);
                Method invokeMethod = forName.getMethod(methodName, parameterClass);
                Object result = invokeMethod.invoke(publishObject, parameters);
                rpcResponse.setResult(result);
            }
        }catch (Exception e){
            logger.error("handler requestId {} failed",id,e);
            rpcResponse.setThrowable(e);
        }finally {
            channel.writeAndFlush(rpcResponse);
        }
    }
}
