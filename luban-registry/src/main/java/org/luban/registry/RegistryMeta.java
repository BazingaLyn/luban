package org.luban.registry;


import java.util.List;

public class RegistryMeta {

    private String ip;

    private int port;

    private List<ServiceMeta> serviceMetaList;


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public List<ServiceMeta> getServiceMetaList() {
        return serviceMetaList;
    }

    public void setServiceMetaList(List<ServiceMeta> serviceMetaList) {
        this.serviceMetaList = serviceMetaList;
    }
}
