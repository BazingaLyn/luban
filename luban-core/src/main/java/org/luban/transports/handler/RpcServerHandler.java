package org.luban.transports.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.luban.rpc.RpcRequest;

/**
 * @author liguolin
 * @create 2018-11-21 19:29
 **/
public class RpcServerHandler extends SimpleChannelInboundHandler<RpcRequest> {


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcRequest rpcRequest) throws Exception {

    }
}
