package com.luban.registry;

public interface RegistryService {


    void registerService(RegistryMeta registryMeta);


    SubscribeResult subscribeService(SubscribeMeta subscribeMeta);

}
