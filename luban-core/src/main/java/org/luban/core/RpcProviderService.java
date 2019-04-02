package org.luban.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liguolin
 * 2019-04-02 11:39:35
 */
public class RpcProviderService {

    private Map<String,Object> serviceContainer = new ConcurrentHashMap<String, Object>();

    public void publishObject(String serviceName,Object o){
        serviceContainer.put(serviceName,o);
    }

    public Object getPublishObject(String serviceName){
        return serviceContainer.get(serviceName);
    }

}
