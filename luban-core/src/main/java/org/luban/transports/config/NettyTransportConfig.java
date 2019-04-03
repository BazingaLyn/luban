package org.luban.transports.config;

import org.luban.core.RpcProviderService;
import org.luban.transports.handlers.RpcHandlers;

/**
 * @author liguolin
 * 2019年04月03日14:26:15
 */
public interface NettyTransportConfig {

    byte protocol();

    RpcHandlers handlers();

    void providerSide(RpcProviderService rpcProviderService);

}
