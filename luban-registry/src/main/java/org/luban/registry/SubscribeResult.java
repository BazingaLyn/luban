package org.luban.registry;


/**
 * @author liguolin
 * 2019-04-01 17:07:29
 */
public class SubscribeResult {

    private String serviceName;

    private ServiceEndpoint serviceEndpoint;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public ServiceEndpoint getServiceEndpoint() {
        return serviceEndpoint;
    }

    public void setServiceEndpoint(ServiceEndpoint serviceEndpoint) {
        this.serviceEndpoint = serviceEndpoint;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (this.serviceName == null ? 0 : this.serviceName.hashCode());
        result = prime * result + (this.serviceEndpoint == null ? 0 : this.serviceEndpoint.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        SubscribeResult other = (SubscribeResult) obj;

        if (this.serviceName == null) {
            return other.serviceName == null;
        }
        if (this.serviceEndpoint == null) {
            return other.serviceEndpoint == null;
        }
        return this.serviceEndpoint.equals(other.serviceEndpoint) && this.serviceEndpoint.equals(other.serviceEndpoint);
    }


    public final static class ServiceEndpoint {

        private String host;

        private int port;

        private int weight;

        public ServiceEndpoint(String host, int port, int weight) {
            this.host = host;
            this.port = port;
            this.weight = weight;
        }

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

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + this.port;
            result = prime * result + this.weight;
            result = prime * result + (this.host == null ? 0 : this.host.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            ServiceEndpoint other = (ServiceEndpoint) obj;
            if (this.port != other.port) {
                return false;
            }
            if (this.host == null) {
                return other.host == null;
            } else {
                return this.host.equals(other.host);
            }
        }
    }


}
