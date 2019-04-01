package org.luban.transports;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.luban.Endpoint;
import org.luban.rpc.RpcRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.luban.utils.NetUtil.string2SocketAddress;

/**
 *
 */
public class LubanRpcNettyClient implements Endpoint {

    private Logger logger = LoggerFactory.getLogger(LubanRpcNettyClient.class);

    private Map<String,Channel> channelRoutes = new ConcurrentHashMap<>(16);

    private Bootstrap bootstrap;
    private EventLoopGroup worker;
    private int nWorkers = 1;


    public LubanRpcNettyClient(){
        init();
    }

    @Override
    public void init() {
        worker = new NioEventLoopGroup(nWorkers);
        bootstrap = new Bootstrap();
        bootstrap.group(worker);
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

    public Object invokeSync(String host, int port, RpcRequest request, int timeoutMillis) {
        logger.info("invoke sync host {} port {} request is {} timeout millis is {}",host,port,request,timeoutMillis);
        String url = String.format("%s:%s",host,port);
        Channel availableChannel = getAvailableChannel(url);
        return null;
    }
}
