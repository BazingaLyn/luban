package org.luban.loadbalance;

import org.luban.registry.SubscribeResult;

import java.util.List;

/**
 * @author liguolin
 * 2019-04-02 09:57:08
 */
public interface LoadBalance {

    String select(List<SubscribeResult.ServiceEndpoint> serviceEndpoints);

}
