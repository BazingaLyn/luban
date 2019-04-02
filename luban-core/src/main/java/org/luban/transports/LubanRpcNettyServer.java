package org.luban.transports;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.luban.Endpoint;
import org.luban.core.RpcProviderService;
import org.luban.transports.codec.RpcCodec;
import org.luban.transports.handler.RpcServerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public LubanRpcNettyServer(int port, RpcProviderService rpcProviderService){
        this.port = port;
        this.rpcProviderService = rpcProviderService;
        init();
    }

    @Override
    public void init() {
        serverBootstrap = new ServerBootstrap();
        boss = new NioEventLoopGroup(1);
        worker = new NioEventLoopGroup(workerNum);
        serverBootstrap.group(boss,worker);
        serverBootstrap.channel(NioServerSocketChannel.class);
    }

    @Override
    public void start() {
        serverBootstrap.localAddress(port);

        serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new RpcCodec());
                ch.pipeline().addLast(new RpcServerHandler(rpcProviderService));
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
}
