package org.luban.meta;

/**
 *
 */
public class RpcMeta {

    private String serviceName;

    private String method;

    private Class<?>[] parametersClz;

    private Object[] parameters;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Class<?>[] getParametersClz() {
        return parametersClz;
    }

    public void setParametersClz(Class<?>[] parametersClz) {
        this.parametersClz = parametersClz;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }
}
