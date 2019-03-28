package org.luban.transports.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.luban.rpc.RpcResponse;

/**
 * @author liguolin
 * @create 2018-11-21 19:29
 **/
public class RpcClientHandler extends SimpleChannelInboundHandler<RpcResponse> {


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcResponse rpcResponse) throws Exception {

    }
}
