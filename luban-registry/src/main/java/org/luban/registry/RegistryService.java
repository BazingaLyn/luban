package org.luban.registry;

public interface RegistryService {


    void registerService(RegistryMeta registryMeta);


    void subscribeService(SubscribeMeta subscribeMeta);

}
