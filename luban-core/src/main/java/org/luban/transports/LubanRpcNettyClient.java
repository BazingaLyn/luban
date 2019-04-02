package org.luban.transports;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.luban.Endpoint;
import org.luban.core.RpcInvokeFuture;
import org.luban.core.RpcInvokeFutures;
import org.luban.rpc.RpcRequest;
import org.luban.transports.codec.RpcCodec;
import org.luban.transports.handler.RpcClientHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.luban.utils.NetUtil.string2SocketAddress;

/**
 * @author liguolin
 * 2019-04-02 10:41:25
 */
public class LubanRpcNettyClient implements Endpoint {

    private Logger logger = LoggerFactory.getLogger(LubanRpcNettyClient.class);

    private Map<String,Channel> channelRoutes = new ConcurrentHashMap<>(16);

    private RpcInvokeFutures rpcInvokeFutures;

    private Bootstrap bootstrap;
    private EventLoopGroup worker;
    private int nWorkers = 1;


    public LubanRpcNettyClient(){
        init();
    }

    @Override
    public void init() {

        rpcInvokeFutures = new RpcInvokeFutures();

        worker = new NioEventLoopGroup(nWorkers);
        bootstrap = new Bootstrap();
        bootstrap.group(worker).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                ChannelPipeline p = socketChannel.pipeline();
                p.addLast(new RpcCodec());
                p.addLast(new RpcClientHandler(rpcInvokeFutures));
            }
        });
    }


    public Channel getAvailableChannel(String url){

        Channel channel = channelRoutes.get(url);
        if(channel != null && channel.isActive()){
            return channel;
        }

        return doCreateAvailableChannel(url);

    }

    private Channel doCreateAvailableChannel(String url) {
        ChannelFuture channelFuture = bootstrap.connect(string2SocketAddress(url));
        return channelFuture.channel();
    }



    @Override
    public void start() {

    }

    @Override
    public void shutdown() {
        worker.shutdownGracefully();
    }

    public void putCurrentInvokeFuture(long requestId, RpcInvokeFuture rpcInvokeFuture) {
        this.rpcInvokeFutures.put(requestId,rpcInvokeFuture);
    }

    public void removeCurrentInvokeFuture(long requestId){
        this.rpcInvokeFutures.removeInvokeFuture(requestId);
    }
}
