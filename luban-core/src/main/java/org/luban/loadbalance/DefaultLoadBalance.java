package org.luban.loadbalance;


import org.luban.registry.SubscribeResult;

import java.util.List;

/**
 * @author liguolin
 * 2019-04-02 10:08:48
 */
public class DefaultLoadBalance implements LoadBalance {


    @Override
    public String select(List<SubscribeResult.ServiceEndpoint> serviceEndpoints) {
        SubscribeResult.ServiceEndpoint serviceEndpoint = serviceEndpoints.get(0);
        return String.format("%s:%s",serviceEndpoint.getHost(),serviceEndpoint.getPort());
    }
}
