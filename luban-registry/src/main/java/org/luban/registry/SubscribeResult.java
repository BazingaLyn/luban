package org.luban.registry;

import java.util.List;

public class SubscribeResult {


    private String serviceName;

    private List<ServiceEndpoint> serviceEndpointList;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public List<ServiceEndpoint> getServiceEndpointList() {
        return serviceEndpointList;
    }

    public void setServiceEndpointList(List<ServiceEndpoint> serviceEndpointList) {
        this.serviceEndpointList = serviceEndpointList;
    }

    public class ServiceEndpoint {

        private String host;

        private int port;

        private int weight;

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }
    }


}
