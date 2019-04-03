package org.luban.transports.handlers;

import io.netty.channel.ChannelHandler;
import org.luban.transports.decoder.DefaultRpcDecoder;
import org.luban.transports.encoder.DefaultRpcEncoder;

/**
 * @author liguolin
 * 2019-04-03 15:49:40
 */
public class DefaultRpcHandlers implements RpcHandlers {

    @Override
    public ChannelHandler encoder() {
        return new DefaultRpcEncoder();
    }

    @Override
    public ChannelHandler decoder() {
        return new DefaultRpcDecoder();
    }

    @Override
    public ChannelHandler heartbeat() {
        return null;
    }

    @Override
    public ChannelHandler userHandler() {
        return null;
    }
}
