package org.luban.meta;

/**
 * @author liguolin
 * 2019-03-27 11:51:05
 */
public class ServiceMeta {

    private String serviceName;

    private int weight;

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
