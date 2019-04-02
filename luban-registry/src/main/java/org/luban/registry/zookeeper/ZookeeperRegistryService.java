package org.luban.registry.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.luban.registry.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static org.luban.constant.LubanCommon.DEFAULT_MAX_SERVICE_WEIGHT;
import static org.luban.constant.LubanCommon.DEFAULT_MIN_SERVICE_WEIGHT;
import static org.luban.registry.zookeeper.RegistryEvent.ADD;
import static org.luban.registry.zookeeper.RegistryEvent.DELETE;


/**
 * @author liguolin
 * 2019-04-01 13:30:10
 *
 */
public class ZookeeperRegistryService implements RegistryService {

    private Logger logger = LoggerFactory.getLogger(ZookeeperRegistryService.class);

    private CuratorFramework client;

    private String zkAddress;

    static final int SESSION_OUTTIME = 5000;// ms

    String DEFAULT_NAMESPACE = "luban";

    private Map<String, List<SubscribeResult>> subscribeResultMap = new ConcurrentHashMap<>(128);

    private Map<String,Boolean> hasRegistryZkListeners = new ConcurrentHashMap<>(128);

    public List<SubscribeResult> getSubscribeResult(SubscribeMeta subscribeMeta) throws Exception {

        String parentServicePath = String.format("/%s/%s/%s",subscribeMeta.getGroup(),subscribeMeta.getServiceName(),subscribeMeta.getVersion());
        List<SubscribeResult> subscribeResults = subscribeResultMap.get(parentServicePath);
        if(subscribeResults != null){
            return subscribeResults;
        }
        subscribeResultMap.put(parentServicePath,new ArrayList<>());
        subscribeService(subscribeMeta);

        return subscribeResultMap.get(parentServicePath);

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
                String serviceName = serviceMeta.getServiceName();
                String version = serviceMeta.getVersion();
                int weight = serviceMeta.getWeight();
                String completeRegistryServiceName = String.format("/%s/%s/%s/%s:%s:%s",
                                                                    group,
                                                                    serviceName,
                                                                    version,
                                                                    ip,
                                                                    port,
                                                                    weight);
                try {
                    Stat stat = client.checkExists().forPath(completeRegistryServiceName);
                    if(stat == null){
                        client.create().creatingParentsIfNeeded().
                                withMode(CreateMode.EPHEMERAL).
                                forPath(completeRegistryServiceName);
                    }
                }catch (Exception e){
                }
            }
        }
    }

    public void subscribeService(SubscribeMeta subscribeMeta) throws Exception {

        String serviceParentPath = String.format("/%s/%s/%s",subscribeMeta.getGroup(),subscribeMeta.getServiceName(),subscribeMeta.getVersion());

        //如果注册过，则不需要再注册
        List<SubscribeResult> subscribeResults = subscribeResultMap.get(serviceParentPath);
        if(subscribeResults != null && subscribeResults.size() > 0){
            return;
        }

        List<String> registryMetaList = client.getChildren().forPath(serviceParentPath);
        if(registryMetaList != null && registryMetaList.size() > 0){
            for(String eachRegistryMeta : registryMetaList){
                modifySubscribeMeta(serviceParentPath,String.format("%s/%s",serviceParentPath,eachRegistryMeta),ADD);
            }
        }

        addZkChildrenPathListener(serviceParentPath);
    }

    private void addZkChildrenPathListener(String serviceParentPath) throws Exception {


        Boolean hasRegistryZkListener = hasRegistryZkListeners.get(serviceParentPath);
        if(hasRegistryZkListener == null || !hasRegistryZkListener){

            hasRegistryZkListeners.put(serviceParentPath,true);

            PathChildrenCache childrenCache = new PathChildrenCache(client, serviceParentPath, true);
            childrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
            childrenCache.getListenable().addListener(
                    (client, event) -> {
                        switch (event.getType()) {
                            case CHILD_ADDED:
                                logger.debug("child_added {} this event will be ignore",event.getData().getPath());
                                modifySubscribeMeta(serviceParentPath,event.getData().getPath(),ADD);
                                break;
                            case CHILD_REMOVED:
                                logger.debug("child_removed {} this event will be ignore",event.getData().getPath());
                                modifySubscribeMeta(serviceParentPath,event.getData().getPath(),DELETE);
                                break;
                            case CHILD_UPDATED:
                                logger.debug("child_updated {} this event will be ignore",event.getData().getPath());
                                break;
                            default:
                                break;
                        }
                    });
        }


    }

    private void modifySubscribeMeta(String serviceParentPath,String childrenPath, RegistryEvent event) {

        List<SubscribeResult> subscribeResults = null;
        switch (event){
            case ADD:

                subscribeResults = subscribeResultMap.get(serviceParentPath);
                if(subscribeResults == null){
                    subscribeResults = new ArrayList<>();
                }
                SubscribeResult subscribeResult = buildSubscribeResult(serviceParentPath,childrenPath);
                if(null != subscribeResult && !subscribeResults.contains(subscribeResult)){
                    subscribeResults.add(subscribeResult);
                }
                subscribeResultMap.put(serviceParentPath,subscribeResults);
                break;
            case DELETE:
                subscribeResults = subscribeResultMap.get(serviceParentPath);
                if(subscribeResults != null && subscribeResults.size() > 0){
                    SubscribeResult deleteSubscribeResult = buildSubscribeResult(serviceParentPath,childrenPath);
                    subscribeResults.remove(deleteSubscribeResult);
                    subscribeResultMap.put(serviceParentPath,subscribeResults);
                }
                break;
        }

    }

    private SubscribeResult buildSubscribeResult(String serviceParentPath,String childService) {

        childService = childService.replace(serviceParentPath+"/", "");
        SubscribeResult subscribeResult = null;

        try{
            String[] registryMetas = childService.split(":");
            if(registryMetas.length == 3){
                String ip = registryMetas[0];
                int port = Integer.parseInt(registryMetas[1]);
                int weight = Integer.parseInt(registryMetas[2]);
                weight  = weight < DEFAULT_MIN_SERVICE_WEIGHT ?
                        DEFAULT_MIN_SERVICE_WEIGHT :
                        weight > DEFAULT_MAX_SERVICE_WEIGHT ? DEFAULT_MAX_SERVICE_WEIGHT : weight;

                subscribeResult = new SubscribeResult();
                subscribeResult.setServiceName(serviceParentPath);
                subscribeResult.setServiceEndpoint(new SubscribeResult.ServiceEndpoint(ip,port,weight));
            }
        }catch (Exception e){
            logger.info("childService {} format is error,ignore this record",childService);
        }
        return subscribeResult;
    }
}
