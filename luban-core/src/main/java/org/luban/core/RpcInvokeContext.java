package org.luban.core;

import org.luban.registry.ServiceMeta;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.luban.constant.LubanCommon.DEFAULT_GROUP;
import static org.luban.constant.LubanCommon.DEFAULT_VERSION;

/**
 * 调用上下文
 * @author liguolin
 * 2019-04-02 15:13:32
 */
public class RpcInvokeContext {

    private Map<String, ServiceMeta> serviceMetaMap = new ConcurrentHashMap<>(16);

    public String defaultVersion = DEFAULT_VERSION;

    private String defaultGroup = DEFAULT_GROUP;


    public ServiceMeta getServiceMethodNameMeta(String declareMethodName){
        return serviceMetaMap.get(declareMethodName);
    }

    public ServiceMeta getServiceMeta(String serviceName){
        ServiceMeta serviceMeta = serviceMetaMap.get(serviceName);
        if(null == serviceMeta){
            serviceMeta = new ServiceMeta();
            serviceMeta.setServiceName(serviceName);
            serviceMeta.setGroup(defaultGroup);
            serviceMeta.setVersion(defaultVersion);
            serviceMetaMap.putIfAbsent(serviceName,serviceMeta);
        }
        return serviceMeta;
    }

    public void putRpcInvokeServiceMeta(String completeMethodName,String group,String serviceName,String version){
        ServiceMeta serviceMeta = new ServiceMeta();
        serviceMeta.setVersion(version);
        serviceMeta.setGroup(group);
        serviceMeta.setServiceName(serviceName);
        serviceMetaMap.put(completeMethodName,serviceMeta);
    }

    public void putRpcInvokeServiceMeta(String completeMethodName,String serviceName){
        putRpcInvokeServiceMeta(completeMethodName,defaultGroup,serviceName,defaultVersion);
    }


    public void setDefaultVersion(String defaultVersion) {
        this.defaultVersion = defaultVersion;
    }

    public void setDefaultGroup(String defaultGroup) {
        this.defaultGroup = defaultGroup;
    }
}
