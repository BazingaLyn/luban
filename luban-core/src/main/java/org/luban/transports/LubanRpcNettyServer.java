package org.luban.transports;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.luban.Endpoint;
import org.luban.core.RpcProviderService;
import org.luban.transports.config.NettyTransportConfig;
import org.luban.transports.handlers.RpcHandlers;
import org.luban.transports.handler.RpcServerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadFactory;

/**
 * @author liguolin
 * 2019-03-28 16:48:58
 */
public class LubanRpcNettyServer implements Endpoint {

    private Logger logger  = LoggerFactory.getLogger(LubanRpcNettyServer.class);

    private int port;

    private ServerBootstrap serverBootstrap;
    private EventLoopGroup boss;
    private EventLoopGroup worker;
    private RpcProviderService rpcProviderService;

    private int workerNum = Runtime.getRuntime().availableProcessors() << 1;

    private NettyTransportConfig nettyTransportConfig;

    public LubanRpcNettyServer(int port, RpcProviderService rpcProviderService, NettyTransportConfig nettyTransportConfig){
        this.port = port;
        this.nettyTransportConfig = nettyTransportConfig;
        this.rpcProviderService = rpcProviderService;
        init();
    }

    @Override
    public void init() {
        nettyTransportConfig.providerSide(rpcProviderService);
        serverBootstrap = new ServerBootstrap();
        boss = initEventLoopGroup(1);
        worker = initEventLoopGroup(workerNum);
        serverBootstrap.group(boss,worker);
        serverBootstrap.channel(isNativeEt() ? EpollServerSocketChannel.class : NioServerSocketChannel.class);
    }

    @Override
    public void start() {
        serverBootstrap.localAddress(port);

        serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(nettyTransportConfig.handlers().decoder());
                ch.pipeline().addLast(nettyTransportConfig.handlers().encoder());
                ch.pipeline().addLast(nettyTransportConfig.handlers().userHandler());
            }
        });
        try {
            this.serverBootstrap.bind().sync();
        } catch (InterruptedException e) {
            logger.error("LubanRpcNettyServer start server failed",e);
        }
    }

    @Override
    public void shutdown() {
        boss.shutdownGracefully();
        worker.shutdownGracefully();
    }

    private EventLoopGroup initEventLoopGroup(int workers) {
        return isNativeEt() ? new EpollEventLoopGroup(workers) : new NioEventLoopGroup(workers);
    }
}
