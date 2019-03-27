package com.luban.registry;

import org.luban.meta.ServiceMeta;

import java.util.List;

public class RegistryMeta {

    private String ip;

    private int port;

    private String group;

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

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public List<ServiceMeta> getServiceMetaList() {
        return serviceMetaList;
    }

    public void setServiceMetaList(List<ServiceMeta> serviceMetaList) {
        this.serviceMetaList = serviceMetaList;
    }
}
