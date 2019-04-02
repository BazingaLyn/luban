package org.luban.core;

import org.luban.Endpoint;
import org.luban.annotation.RpcService;
import org.luban.exception.ServiceHasMultiInstantiationException;
import org.luban.registry.RegistryMeta;
import org.luban.registry.RegistryService;
import org.luban.registry.ServiceMeta;
import org.luban.registry.zookeeper.ZookeeperRegistryService;
import org.luban.transports.LubanRpcNettyServer;
import org.luban.utils.NetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.luban.constant.LubanCommon.*;

/**
 * @author liguolin
 * 2019-03-28 16:30:21
 */
public class LubanRpcServer implements Endpoint,RpcServer {

    private Logger logger = LoggerFactory.getLogger(LubanRpcServer.class);

    private LubanRpcNettyServer lubanRpcNettyServer;

    private RegistryService registryService;

    private int serverPort;
    private String registryAddress;
    private String ip;
    private RpcProviderService rpcProviderService;


    public LubanRpcServer(int serverPort,String registryAddress){
        this.serverPort = serverPort;
        this.registryAddress = registryAddress;
        init();
    }


    public void init() {
        this.ip = NetUtil.getLocalAddress();
        registryService = new ZookeeperRegistryService(this.registryAddress);
        rpcProviderService = new RpcProviderService();
        lubanRpcNettyServer = new LubanRpcNettyServer(serverPort,rpcProviderService);
    }

    public void start() {
        lubanRpcNettyServer.start();
    }

    public void shutdown() {
        lubanRpcNettyServer.shutdown();
    }

    public void publishService(Object o) {

        Class<?>[] interfaces = o.getClass().getInterfaces();
        RpcService annotation = o.getClass().getAnnotation(RpcService.class);
        int weight = DEFAULT_SERVICE_WEIGHT;
        String group = DEFAULT_GROUP;
        String version = DEFAULT_VERSION;
        if(annotation != null){
            weight = annotation.weight();
            group = annotation.group();
            version = annotation.version();
        }

        if(interfaces == null || interfaces.length == 0){
            return;
        }

        List<ServiceMeta> serviceMetaList = new ArrayList<ServiceMeta>();

        for(Class clz : interfaces){
            ServiceMeta serviceMeta = new ServiceMeta();
            String serviceName = clz.getName();
            serviceMeta.setServiceName(serviceName);
            serviceMeta.setGroup(group);
            serviceMeta.setWeight(weight);
            serviceMeta.setVersion(version);

            String serviceNameUnique = completeServiceName(group,serviceName,version);
            Object currentService = rpcProviderService.getPublishObject(serviceNameUnique);
            if(currentService != null){
                throw new ServiceHasMultiInstantiationException(String.format("%s has more than one Instantiation"));
            }
            rpcProviderService.publishObject(serviceNameUnique,o);

            serviceMetaList.add(serviceMeta);
        }

        RegistryMeta registryMeta = new RegistryMeta();
        registryMeta.setIp(this.ip);
        registryMeta.setPort(this.serverPort);
        registryMeta.setServiceMetaList(serviceMetaList);

        this.registryService.registerService(registryMeta);

    }
}
