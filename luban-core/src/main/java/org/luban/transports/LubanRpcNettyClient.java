package org.luban.transports;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.luban.Endpoint;
import org.luban.constant.LubanCommon;
import org.luban.core.RpcInvokeFuture;
import org.luban.core.RpcInvokeFutures;
import org.luban.transports.config.NettyTransportConfig;
import org.luban.transports.handlers.RpcHandlers;
import org.luban.transports.handler.RpcClientHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.luban.constant.LubanCommon.DEFAULT_CREATE_CHANNEL_LOCK_TIME;
import static org.luban.utils.NetUtil.string2SocketAddress;

/**
 * @author liguolin
 * 2019-04-02 10:41:25
 */
public class LubanRpcNettyClient implements Endpoint {

    private Logger logger = LoggerFactory.getLogger(LubanRpcNettyClient.class);

    private Map<String,Channel> channelRoutes = new ConcurrentHashMap<>(16);

    private RpcInvokeFutures rpcInvokeFutures;

    private NettyTransportConfig nettyTransportConfig;

    private Bootstrap bootstrap;
    private EventLoopGroup worker;
    private int nWorkers = 1;


    public LubanRpcNettyClient(NettyTransportConfig nettyTransportConfig){
        this.nettyTransportConfig = nettyTransportConfig;
        init();
    }

    @Override
    public void init() {

        rpcInvokeFutures = new RpcInvokeFutures();

        worker = isNativeEt() ? new EpollEventLoopGroup(nWorkers) :new NioEventLoopGroup(nWorkers);

        bootstrap = new Bootstrap();
        bootstrap.option(ChannelOption.ALLOCATOR, ByteBufAllocator.DEFAULT).
                option(ChannelOption.MESSAGE_SIZE_ESTIMATOR, DefaultMessageSizeEstimator.DEFAULT)
                .option(ChannelOption.SO_REUSEADDR, true);


        bootstrap.group(worker).channel(isNativeEt() ? EpollSocketChannel.class : NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                ChannelPipeline p = socketChannel.pipeline();
                p.addLast(nettyTransportConfig.handlers().decoder());
                p.addLast(nettyTransportConfig.handlers().encoder());
//                p.addLast(new IdleStateChecker(timer, 0, WRITER_IDLE_TIME_SECONDS, 0));
//                p.addLast(new IdleStateChecker(timer, 0, WRITER_IDLE_TIME_SECONDS, 0));
                p.addLast(nettyTransportConfig.handlers().userHandler());
            }
        });
    }


    public Channel getAvailableChannel(String url) throws InterruptedException {

        Channel channel = channelRoutes.get(url);
        if(channel != null && channel.isActive()){
            return channel;
        }

        return doCreateAvailableChannel(url);

    }

    private final Lock createChannelLock = new ReentrantLock();


    private Channel doCreateAvailableChannel(String url) throws InterruptedException {

        Channel channel = channelRoutes.get(url);
        if(channel != null && channel.isActive()){
            return channel;
        }

        if(this.createChannelLock.tryLock(DEFAULT_CREATE_CHANNEL_LOCK_TIME, TimeUnit.MILLISECONDS)){
            try{
                boolean createNewConnection = false;
                channel = channelRoutes.get(url);
                if(channel != null){
                    if(channel.isActive()){
                        return channel;
                    }else{
                        channelRoutes.remove(url);
                        createNewConnection = true;
                    }
                }else{
                    createNewConnection = true;
                }

                if(createNewConnection){
                    bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, LubanCommon.DEFAULT_CONNECT_TIMEOUT_MILLIS);
                    ChannelFuture channelFuture = bootstrap.connect(string2SocketAddress(url));
                    channelFuture.awaitUninterruptibly();
                    if (!channelFuture.isDone()) {
                        String errMsg = "Create connection to " + url + " timeout!";
                        logger.warn(errMsg);
                        throw new Exception(errMsg);
                    }
                    if (channelFuture.isCancelled()) {
                        String errMsg = "Create connection to " + url + " cancelled by user!";
                        logger.warn(errMsg);
                        throw new Exception(errMsg);
                    }
                    if (!channelFuture.isSuccess()) {
                        String errMsg = "Create connection to " + url + " error!";
                        logger.warn(errMsg);
                        throw new Exception(errMsg, channelFuture.cause());
                    }
                    return channelFuture.channel();

                }
            }catch (Exception e){
                logger.error("doCreateAvailableChannel url {} failed",url,e);
            }finally {
                createChannelLock.unlock();
            }
        }
        return channel;
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
