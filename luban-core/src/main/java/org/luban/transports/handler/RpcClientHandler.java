package org.luban.transports.handler;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.luban.core.RpcInvokeFutures;
import org.luban.rpc.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author liguolin
 * @create 2018-11-21 19:29
 **/
public class RpcClientHandler extends SimpleChannelInboundHandler<RpcResponse> {

    private Logger logger = LoggerFactory.getLogger(RpcClientHandler.class);

    private RpcInvokeFutures rpcInvokeFutures;

    public RpcClientHandler(RpcInvokeFutures rpcInvokeFutures) {
        this.rpcInvokeFutures = rpcInvokeFutures;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcResponse rpcResponse) throws Exception {
        logger.info("receive msg from {} response is {}",channelHandlerContext.channel().remoteAddress().toString(), JSON.toJSON(rpcResponse));
        rpcInvokeFutures.putResult(rpcResponse.getId(),rpcResponse);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("netty exceptionCaught ");
        super.exceptionCaught(ctx, cause);
    }
}
