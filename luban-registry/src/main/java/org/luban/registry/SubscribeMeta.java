package org.luban.registry;

/**
 * @author liguolin
 * 2019-03-27 14:31:57
 */
public class SubscribeMeta {

    private String group;

    private String serviceName;

    private String version;

    public SubscribeMeta(){

    }

    public SubscribeMeta(String group, String serviceName, String version) {
        this.group = group;
        this.serviceName = serviceName;
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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
