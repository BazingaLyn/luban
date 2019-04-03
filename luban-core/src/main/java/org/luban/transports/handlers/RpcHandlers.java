package org.luban.transports.handlers;


import io.netty.channel.ChannelHandler;

/**
 * @author liguolin
 * 2019-03-28 17:36:42
 */
public interface RpcHandlers {


    ChannelHandler encoder();


    ChannelHandler decoder();


    ChannelHandler heartbeat();


    ChannelHandler userHandler();
}
