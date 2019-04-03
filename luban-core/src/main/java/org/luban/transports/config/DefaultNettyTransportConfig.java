package org.luban.transports.config;

import org.luban.core.RpcProviderService;
import org.luban.transports.handlers.DefaultRpcHandlers;
import org.luban.transports.handlers.RpcHandlers;
import org.luban.transports.protocol.Protocol;

/**
 * @author liguolin
 * 2019年04月03日15:45:30
 */
public class DefaultNettyTransportConfig implements NettyTransportConfig {

    private RpcProviderService rpcProviderService;

    @Override
    public byte protocol() {
        return Protocol.DEFAULT.getCode();
    }

    @Override
    public RpcHandlers handlers() {
        return new DefaultRpcHandlers();
    }

    @Override
    public void providerSide(RpcProviderService rpcProviderService) {
        this.rpcProviderService = rpcProviderService;
    }
}
