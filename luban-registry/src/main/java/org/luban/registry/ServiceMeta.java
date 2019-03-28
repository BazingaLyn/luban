package org.luban.registry;

/**
 * @author liguolin
 * 2019-03-27 11:51:05
 */
public class ServiceMeta {

    private String serviceName;

    private String group;

    private int weight;

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
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
