package org.luban.registry;

import java.util.List;

/**
 * @author liguolin
 * 2019-04-01 10:48:58
 */
public interface RegistryService {


    void registerService(RegistryMeta registryMeta);


    void subscribeService(SubscribeMeta subscribeMeta) throws Exception;


    List<SubscribeResult> getSubscribeResult(SubscribeMeta subscribeMeta) throws Exception;

}
