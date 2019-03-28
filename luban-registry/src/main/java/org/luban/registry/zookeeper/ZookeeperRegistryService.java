package org.luban.registry.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.luban.registry.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 *
 */
public class ZookeeperRegistryService implements RegistryService {


    private CuratorFramework client;

    private String zkAddress;

    static final int SESSION_OUTTIME = 5000;// ms

    String DEFAULT_NAMESPACE = "luban";

    private Map<String,List<SubscribeResult>> subscribeResultMap = new ConcurrentHashMap<String, List<SubscribeResult>>(128);


    public List<SubscribeResult> getSubsribeResult(String group,String serviceName){

        String parentServicePath = String.format("/%s/%s",group,serviceName);
        List<SubscribeResult> subscribeResults = subscribeResultMap.get(parentServicePath);
        if(subscribeResults != null && subscribeResults.size() > 0){
            return subscribeResults;
        }

        subscribeService(group,serviceName);

        return subscribeResultMap.get(group);

    }

    public ZookeeperRegistryService(String zkAddress){
        this.zkAddress = zkAddress;
        init();
    }

    private void init(){
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 5);

        client = CuratorFrameworkFactory.builder()
                         .connectString(this.zkAddress).sessionTimeoutMs(SESSION_OUTTIME)
                         .retryPolicy(retryPolicy)
                          .namespace(DEFAULT_NAMESPACE)
                         .build();
        client.start();
    }

    public void registerService(RegistryMeta registryMeta) {

        int port = registryMeta.getPort();
        String ip = registryMeta.getIp();
        if(registryMeta.getServiceMetaList()!=null && registryMeta.getServiceMetaList().size()>0){
            for(ServiceMeta serviceMeta:registryMeta.getServiceMetaList()){
                String group = serviceMeta.getGroup();
                String completeRegistryServiceName = String.format("/%s/%s/%s:%s/%s",group,serviceMeta.getServiceName(),ip,port,serviceMeta.getWeight());
                try {
                    Stat stat = client.checkExists().forPath(completeRegistryServiceName);
                    if(stat == null){
                        client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(completeRegistryServiceName);
                    }
                }catch (Exception e){

                }
            }
        }
    }

    private void subscribeService(String group,String serviceName){
        subscribeService(new SubscribeMeta(group,serviceName));
    }

    public void subscribeService(SubscribeMeta subscribeMeta) {

        String serviceParentPath = String.format("/%s/%s",subscribeMeta.getGroup(),subscribeMeta.getServiceName());
        try{

            List<String> childServiceMetas = client.getChildren().forPath(serviceParentPath);


        }catch (Exception e){

        }

    }
}
