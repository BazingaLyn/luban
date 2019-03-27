package org.luban.registry;

/**
 * @author liguolin
 * 2019-03-27 14:31:57
 */
public class SubscribeMeta {

    private String group;

    private String serviceName;

    public SubscribeMeta(){

    }

    public SubscribeMeta(String group, String serviceName) {
        this.group = group;
        this.serviceName = serviceName;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
